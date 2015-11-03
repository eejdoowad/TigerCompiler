import AST.*;
import AST.Node;
import jdk.nashorn.internal.ir.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

// Performs semantic analysis via inputs from the parser and outputs
// an AST upon successful analysis
// While the parser parses in a top-down fashion, the analyzer will build
// the AST in a bottom-up shift-reduce fashion where AST nodes are pushed
// to the stack from semantic terminals which are individually checked for
// semantics and then reduced to a head AST node where the symbols are
// attached.
public class TigerSemanticAnalysis {
    // Root of the AST under construction
    private Program root;

    // Symbol table
    private SymbolTable symbolTable;

    // semantic stack that AST nodes are pushed onto
    private Deque<Node> semanticStack;

    // Increment for assigning temporary symbols
    private int tempIncrement = 0;

    // Set to true if semantic error occurs
    boolean semanticError = false;

    public TigerSemanticAnalysis() {
        root = null;
        symbolTable = new SymbolTable();
        semanticStack = new ArrayDeque<>();
    }

    // Private helper for semantic errors
    private void error(String message) {
        System.out.println(message);
        semanticError = true;
    }

    // Returns whether a semantic error occured
    public boolean isSemanticError() {
        return semanticError;
    }

    // Creates root AST node. Nothing special.
    public void semaProgramStart() {
        root = new Program();
    }

    public Program semaProgramEnd() {
        // TODO: Attach remaining nodes on stack to root node
        return root;
    }

    // Converts a string literal to an int, makes an AST node,
    // and pushes to semantic stack
    public void semaIntLit(String lit) {
        int value = Integer.parseInt(lit);
        AST.IntLit node = new IntLit();
        node.val = value;
        semanticStack.addFirst(node);
    }

    // Same as int lit but for a float instead
    public void semaFloatLit(String lit) {
        float value = Float.parseFloat(lit);
        AST.FloatLit node = new FloatLit();
        node.val = value;
        semanticStack.addFirst(node);
    }

    public void semaIdentifier(String lit) {
        AST.ID node = new ID();
        node.name = lit;
        semanticStack.addFirst(node);
    }

    public void semaVarDeclaration() {
        Node top = semanticStack.removeFirst();
        Const initializer = null;
        ID type;
        ArrayList<ID> varNames = new ArrayList<>();
        ArrayList<SemanticSymbol> varSymbols = new ArrayList<>();
        if (top instanceof IntLit || top instanceof FloatLit) {
            initializer = (Const)top;
            type = (ID)semanticStack.removeFirst();
        } else {
            type = (ID)top;
        }

        while (semanticStack.peekFirst() instanceof ID) {
            varNames.add((ID)semanticStack.removeFirst());
        }

        // Perform a type lookup
        SemanticSymbol typeSymbol = symbolTable.get(type.name);
        if (typeSymbol == null) {
            error("Semantic Error: " + type.name + " does not name a valid type");
            return;
        }
        if (typeSymbol.getSymbolClass() != SemanticSymbol.SymbolClass.TypeDecleration) {
            error("Semantic Error: " + type.name + " does not name a valid type");
            return;
        }

        // Create new symbol table entries for each new variable
        for (ID var : varNames) {
            if (symbolTable.get(var.name) != null) {
                error("Semantic Error: variable " + var.name + " is already defined");
                return;
            }

            SemanticSymbol newSym = new SemanticSymbol(var.name, SemanticSymbol.SymbolClass.VarDeclaration);
            newSym.setSymbolType(typeSymbol);
            symbolTable.put(var.name, newSym);
            varSymbols.add(newSym);
        }

        // Type check initialization
        if (initializer != null) {
            // First check if type is of first order
            if (typeSymbol.getSymbolType() == SemanticSymbol.SymbolType.SymbolCustom) {
                error("Semantic error: " + typeSymbol.getName() + " is not a 1st order derived type");
                return;
            }

            // Check float and convert to int if needed
            if (initializer instanceof FloatLit) {
                if (typeSymbol.getName().equals("int")) {
                    // Convert to int by grammar conversion rules
                    float val = ((FloatLit)initializer).val;
                    initializer = new IntLit();
                    ((IntLit)initializer).val = (int)val;
                } else if (typeSymbol.getSymbolType() != SemanticSymbol.SymbolType.SymbolFloat) {
                    error("Semantic Error: Attempted to assign float to integer variables");
                    return;
                }
            }

            // Type check int constant values
            if (initializer instanceof IntLit) {
                if (typeSymbol.getSymbolType() != SemanticSymbol.SymbolType.SymbolInt) {
                    error("Semantic Error: Attempted to assign int to float values");
                    return;
                }
            }
        }

        // Create and push AST node
        VarDec node = new VarDec();
        node.type = typeSymbol;
        node.vars = varSymbols;
        node.init = initializer;
        semanticStack.addFirst(node);
    }

    // Analysis for an array type declaration. Creates a temporary
    // type and pushes an identifier that references it to the
    // semantic stack
    public void semaArrayType() {
        ID type = (ID)semanticStack.removeFirst();
        IntLit literal = (IntLit)semanticStack.removeFirst();
        if (literal.val <= 0) {
            error("Semantic error: Attempted to create array type with size <= 0");
            return;
        }

        String tempName = "$temp" + tempIncrement;
        tempIncrement++;

        SemanticSymbol newType = new SemanticSymbol(tempName, SemanticSymbol.SymbolClass.TypeDecleration);
        if (type.name.equals("int")) {
            newType.setSymbolType(SemanticSymbol.SymbolType.SymbolInt);
        } else if (type.name.equals("float")) {
            newType.setSymbolType(SemanticSymbol.SymbolType.SymbolFloat);
        } else {
            error("Semantic error: Array must be of type int or float");
            return;
        }
        newType.setArraySize(literal.val);
        symbolTable.put(tempName, newType);

        // ID reference to this new type
        ID reference = new ID();
        reference.name = tempName;
        semanticStack.addFirst(reference);
    }
}
