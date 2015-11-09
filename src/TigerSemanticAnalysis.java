import AST.*;
import AST.Node;

import javax.lang.model.element.VariableElement;
import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.concurrent.Exchanger;

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
        while (!semanticStack.isEmpty()) {
            Node node = semanticStack.removeLast();
            if (node instanceof Stat) {
                root.stats.add((Stat)node);
            }
        }
        return root;
    }

    // Converts a string literal to an int, makes an AST node,
    // and pushes to semantic stack
    public void semaIntLit(String lit) {
        int value = Integer.parseInt(lit);
        AST.IntLit node = new IntLit();
        node.val = value;
        node.type = symbolTable.get("int");
        semanticStack.addFirst(node);
    }

    // Same as int lit but for a float instead
    public void semaFloatLit(String lit) {
        float value = Float.parseFloat(lit);
        AST.FloatLit node = new FloatLit();
        node.val = value;
        node.type = symbolTable.get("float");
        semanticStack.addFirst(node);
    }

    public void semaIdentifier(String lit) {
        AST.ID node = new ID();
        node.name = lit;
        semanticStack.addFirst(node);
    }

    public void semaTypeDeclaration(){
        ID exisitingType = (ID)semanticStack.removeFirst();
        ID newType = (ID)semanticStack.removeFirst();

        // Make sure new type is not already defined
        if (symbolTable.get(newType.name) != null) {
            error("Semantic error: " + newType.name + " is already defined");
            return;
        }

        // Create new type declaration node
        TypeDec node = new TypeDec();

        // Case 1: new type is an array with a temporary type already made
        if (exisitingType.name.charAt(0) == '$') {
            // Look it up, rename it
            SemanticSymbol type = symbolTable.get(exisitingType.name);
            if (type == null) {
                error("Semantic error: lookup of temporary " + exisitingType.name + " failed");
                return;
            }
            symbolTable.rename(type, newType.name);
            node.newType = type;
        } else if (exisitingType.name.equals("int")) {
            // Case 2: new type is an int
            SemanticSymbol type = new SemanticSymbol(newType.name, SemanticSymbol.SymbolClass.TypeDecleration);
            type.setSymbolType(SemanticSymbol.SymbolType.SymbolInt);
            type.setArraySize(1);
            symbolTable.put(newType.name, type);
            node.newType = type;
        } else if (exisitingType.name.equals("float")) {
            // Case 3: new type is a float
            SemanticSymbol type = new SemanticSymbol(newType.name, SemanticSymbol.SymbolClass.TypeDecleration);
            type.setSymbolType(SemanticSymbol.SymbolType.SymbolFloat);
            type.setArraySize(1);
            symbolTable.put(newType.name, type);
            node.newType = type;
        } else {
            // Case 4: new type is an alias of another custom type
            SemanticSymbol lookup = symbolTable.get(exisitingType.name);
            if (lookup == null) {
                error("Semantic error: " + exisitingType.name + " is not a defined type");
                return;
            }
            if (lookup.getSymbolClass() != SemanticSymbol.SymbolClass.TypeDecleration) {
                error("Semantic error: " + exisitingType.name + " is not a defined type");
                return;
            }

            // Create the new type to alias the lookuped type
            SemanticSymbol type = new SemanticSymbol(newType.name, SemanticSymbol.SymbolClass.TypeDecleration);
            type.setSymbolType(lookup);
            type.setArraySize(1);
            symbolTable.put(newType.name, type);
            node.newType = type;
        }

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
            varNames.add(0, (ID)semanticStack.removeFirst()); // add to front
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

    public void semaAttachTypeDec(){
        root.typeDecs.add((AST.TypeDec) semanticStack.pop());
    }

    public void semaAttachVarDec(){
        root.varDecs.add((AST.VarDec)semanticStack.pop());
    }

    public void semaAttachFunDec(){
        root.funDecs.add((AST.FunDec)semanticStack.pop());
    }

    public void semaVariableReference(String name) {
        SemanticSymbol lookup = symbolTable.get(name);
        if (lookup == null || lookup.getSymbolClass() != SemanticSymbol.SymbolClass.VarDeclaration) {
            error("Semantic error: " + name + " is not a declared variable");
            return;
        }

        VarReference node = new VarReference();
        node.reference = lookup;
        node.index = null;
        node.type = lookup.getSymbolTypeReference();
        semanticStack.addFirst(node);
    }

    public void semaVariableReferenceIndex() {
        Expr index = (Expr)semanticStack.removeFirst();
        VarReference variable = (VarReference)semanticStack.peekFirst();
        if (!index.type.getName().equals("int")) {
            error("Semantic error: Array index must be of type int");
            return;
        }
        if (variable.type.getArraySize() <= 0) {
            error("Semantic error: Type " + variable.type.getName() + " is not an array type");
            return;
        }
        if (variable.type.getSymbolType() == SemanticSymbol.SymbolType.SymbolInt) {
            variable.type = symbolTable.get("int");
        } else if (variable.type.getSymbolType() == SemanticSymbol.SymbolType.SymbolFloat) {
            variable.type = symbolTable.get("float");
        } else {
            variable.type = variable.type.getSymbolTypeReference();
        }
        variable.index = index;
    }

    // Ensures topmost variable reference is not an array
    public void semaVariableReferenceArrayCheck() {
        VarReference var = (VarReference)semanticStack.peekFirst();
        if (var.type.getArraySize() > 0) {
            error("Semantic error: " + var.reference.getName() + " is an array but is not indexed into");
            return;
        }
    }

    // Returns whether one type can be implicitly converted to another
    private boolean semaCanConvertType(SemanticSymbol src, SemanticSymbol dst) {
        if (src != dst) {
            if (src.getName().equals("float") && dst.getName().equals("int")) {
                error("Semantic error: cannot convert float to int");
                return false;
            }
            if (src.getName().equals("int")) {
                if (dst.getInferredPrimitive() != SemanticSymbol.SymbolType.SymbolInt) {
                    error("Semantic error: cannot assign int to type " + dst.getName());
                    return false;
                }
            } else if (src.getName().equals("float")) {
                if (dst.getInferredPrimitive() != SemanticSymbol.SymbolType.SymbolFloat) {
                    error("Semantic error: cannot assign float to type " + dst.getName());
                    return false;
                }
            } else {
                error("Semantic error: " + src.getName() + " and " + dst.getName() + " are incompatible types");
                return false;
            }
        }
        return true;
    }

    public void semaAssign() {
        Expr assignment = (Expr)semanticStack.removeFirst();
        Expr index = null;
        if (semanticStack.peekFirst() instanceof Expr) {
            index = (Expr) semanticStack.removeFirst();
        }
        ID variableID = (ID)semanticStack.removeFirst();
        SemanticSymbol variable = symbolTable.get(variableID.name);
        if (variable == null || variable.getSymbolClass() != SemanticSymbol.SymbolClass.VarDeclaration) {
            error("Semantic error: " + variableID.name + " is not a declared variable");
            return;
        }
        if (variable.getSymbolTypeReference().getArraySize() > 0 && index == null) {
            error("Semantic error: " + variableID.name + " requires an array reference before assignment");
            return;
        }
        if (variable.getSymbolTypeReference().getArraySize() <= 0 && index != null) {
            error("Semantic error: " + variableID.name + " is not of an array type");
            return;
        }

        SemanticSymbol baseType = variable.getSymbolTypeReference();
        if (index != null) {
            if (!index.type.getName().equals("int")) {
                error("Semantic error: Array index must be of type int");
                return;
            }
            if (variable.getSymbolTypeReference().getSymbolType() == SemanticSymbol.SymbolType.SymbolInt) {
                baseType = symbolTable.get("int");
            } else if (variable.getSymbolTypeReference().getSymbolType() == SemanticSymbol.SymbolType.SymbolFloat) {
                baseType = symbolTable.get("float");
            } else {
                baseType = variable.getSymbolTypeReference().getSymbolTypeReference();
            }
        }

        if (!semaCanConvertType(assignment.type, baseType)) {
            return;
        }
        AssignStat node = new AssignStat();
        node.left = variable;
        node.right = assignment;
        node.index = index;
        semanticStack.addFirst(node);
    }

    // Returns whether one type can be implicitly converted to another without giving an error
    private boolean semaCanConvertTypeNoError(SemanticSymbol src, SemanticSymbol dst) {
        if (src != dst) {
            if (src.getName().equals("float") && dst.getName().equals("int")) {
                return false;
            }
            if (src.getName().equals("int")) {
                if (dst.getInferredPrimitive() != SemanticSymbol.SymbolType.SymbolInt) {
                    return false;
                }
            } else if (src.getName().equals("float")) {
                if (dst.getInferredPrimitive() != SemanticSymbol.SymbolType.SymbolFloat) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public void semaArithmeticBinOp(ArithmeticBinOp node) {
        // get the left and right
        Expr right = (Expr)semanticStack.removeFirst();
        Expr left = (Expr)semanticStack.removeFirst();

        // Test whether one can be converted to the other implicitly
        if (semaCanConvertTypeNoError(right.type, left.type)) {
            node.left = left;
            node.right = right;
            node.type = left.type;
            node.convertLeft = false;
        } else if (semaCanConvertTypeNoError(left.type, right.type)) {
            node.left = left;
            node.right = right;
            node.type = right.type;
            node.convertLeft = true;
        } else {
            error("Semantic error: type mismatch between " + left.type.getName() + " and " + right.type.getName());
            return;
        }

        semanticStack.addFirst(node);
    }

    // Similar to arithmetic ops but result is always an integer value
    public void semaComparisonBinOp(ComparisonBinOp node) {
        // get the left and right
        Expr right = (Expr)semanticStack.removeFirst();
        Expr left = (Expr)semanticStack.removeFirst();

        // Test whether one can be converted to the other implicitly
        if (semaCanConvertTypeNoError(right.type, left.type)) {
            node.left = left;
            node.right = right;
            node.convertLeft = false;
        } else if (semaCanConvertTypeNoError(left.type, right.type)) {
            node.left = left;
            node.right = right;
            node.convertLeft = true;
        } else {
            error("Semantic error: type mismatch between " + left.type.getName() + " and " + right.type.getName());
            return;
        }
        node.type = symbolTable.get("int");

        semanticStack.addFirst(node);
    }

    public void semaLogicBinOp(LogicBinOp node) {
        // get the left and right
        Expr right = (Expr)semanticStack.removeFirst();
        Expr left = (Expr)semanticStack.removeFirst();

        // Make sure they are both integers
        if (!left.type.getName().equals("int") || !right.type.getName().equals("int")) {
            error("Logical comparison operator only acts on integers");
            return;
        }
        node.left = left;
        node.right = right;
        node.type = left.type;

        semanticStack.addFirst(node);
    }

    public void semaIfStart() {
        // get the expression
        Expr cond = (Expr)semanticStack.removeFirst();

        // condition must be an integer type
        if (!cond.type.getName().equals("int")) {
            error("Semantic error: condition must be an integer");
            return;
        }

        // Build the node
        IfStat node = new IfStat();
        node.cond = cond;
        node.finalized = false;
        semanticStack.addFirst(node);
    }

    public void semaIfBlock() {
        Deque<Stat> statements = new ArrayDeque<>();
        while (!semanticStack.isEmpty()) {
            Stat statement = (Stat)semanticStack.peekFirst();
            // We break if we find an if statement that is not finalized (i.e under construction)
            // That is the if statement we are attaching everything to. If an if statement is finalized,
            // it is a nested if statement that will already be done and we don't want to attach to a
            // nested if statement
            if (statement instanceof IfStat) {
                if (!((IfStat)statement).finalized) {
                    break;
                }
            }
            // Otherwise it is a statement inside the if block
            // We use a stack to reverse the already reversed statements
            statements.addFirst((Stat)semanticStack.removeFirst());
        }

        // Add the statements
        IfStat node = (IfStat)semanticStack.peekFirst();
        for (Stat stat : statements) {
            node.trueStats.add(stat);
        }
        node.finalized = true;
    }

    public void semaElseStart() {
        IfStat node = (IfStat)semanticStack.peekFirst();

        // Statement is no longer finalized
        node.finalized = false;
        node.falseStats = new ArrayList<>();
    }

    // Very similar to analyzing an if block
    public void semaElseBlock() {
        Deque<Stat> statements = new ArrayDeque<>();
        while (!semanticStack.isEmpty()) {
            Stat statement = (Stat)semanticStack.peekFirst();
            if (statement instanceof IfStat) {
                if (!((IfStat)statement).finalized) {
                    break;
                }
            }
            statements.addFirst((Stat)semanticStack.removeFirst());
        }

        // Add the statements
        IfStat node = (IfStat)semanticStack.peekFirst();
        for (Stat stat : statements) {
            node.falseStats.add(stat);
        }
        node.finalized = true;
    }
}
