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
    private boolean semanticError = false;

    // Counter incremented every time a loop is entered and decremented when one is left
    // Used to determine if a break is legal.
    private int loopCounter = 0;

    // Current function (used to analyze return statements
    private SemanticSymbol currentFunction = null;

    // Error variable and type to use as placeholders when errors cause actual symbols to not be found
    private SemanticSymbol errorType;
    private SemanticSymbol errorVar;
    private SemanticSymbol errorFunc;

    private int currentLine = 0; // Line associated with current node

    public TigerSemanticAnalysis() {
        root = null;
        symbolTable = new SymbolTable();
        semanticStack = new ArrayDeque<>();

        // Init error type
        errorType = new SemanticSymbol("errorType", SemanticSymbol.SymbolClass.TypeDecleration);
        errorType.setSymbolType(SemanticSymbol.SymbolType.SymbolError);
        errorType.setArraySize(0);

        // Init error variable
        errorVar = new SemanticSymbol("errorVar", SemanticSymbol.SymbolClass.VarDeclaration);
        errorVar.setSymbolType(errorType);

        // Init error function
        errorFunc = new SemanticSymbol("errorFunc", SemanticSymbol.SymbolClass.FunctionDeclatation);
        errorFunc.setSymbolType(errorType);
    }

    // Private helper for semantic errors
    private void error(String message) {
        System.out.println(message);
        semanticError = true;
    }

    // Semantic errors with line number
    private void error(String message, Node cause) {
        System.out.println("Line " + cause.lineNumber + ": " + message);
        semanticError = true;
    }

    // Returns whether a semantic error occured
    public boolean isSemanticError() {
        return semanticError;
    }

    // Sets current line
    public void setCurrentLine(int line) {
        currentLine = line;
    }

    // Creates root AST node. Nothing special.
    public void semaProgramStart() {
        root = new Program();
        root.lineNumber = currentLine;
    }

    public Program semaProgramEnd() {
        while (!semanticStack.isEmpty()) {
            Node node = semanticStack.removeLast();
            if (node instanceof Stat) {
                root.stats.add((Stat)node);
            } else if (node instanceof TypeDec) {
                root.typeDecs.add((TypeDec)node);
            } else if (node instanceof VarDec) {
                root.varDecs.add((VarDec)node);
            } else if (node instanceof FunDec) {
                root.funDecs.add((FunDec)node);
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
        node.lineNumber = currentLine;
        semanticStack.addFirst(node);
    }

    // Same as int lit but for a float instead
    public void semaFloatLit(String lit) {
        float value = Float.parseFloat(lit);
        AST.FloatLit node = new FloatLit();
        node.val = value;
        node.type = symbolTable.get("float");
        node.lineNumber = currentLine;
        semanticStack.addFirst(node);
    }

    public void semaIdentifier(String lit) {
        AST.ID node = new ID();
        node.name = lit;
        node.lineNumber = currentLine;
        semanticStack.addFirst(node);
    }

    public void semaTypeDeclaration(){
        ID exisitingType = (ID)semanticStack.removeFirst();
        ID newType = (ID)semanticStack.removeFirst();

        // Make sure new type is not already defined
        if (symbolTable.get(newType.name) != null) {
            error("Semantic error: " + newType.name + " is already defined", newType);
            return;
        }

        // Create new type declaration node
        TypeDec node = new TypeDec();

        // Case 1: new type is an array with a temporary type already made
        if (exisitingType.name.charAt(0) == '$') {
            // Look it up, rename it
            SemanticSymbol type = symbolTable.get(exisitingType.name);
            if (type == null) {
                // This failing indicates a software bug
                error("Semantic error: lookup of temporary " + exisitingType.name + " failed", exisitingType);
                return;
            }
            symbolTable.rename(type, newType.name);
            node.newType = type;
        } else if (exisitingType.name.equals("int")) {
            // Case 2: new type is an int
            SemanticSymbol type = new SemanticSymbol(newType.name, SemanticSymbol.SymbolClass.TypeDecleration);
            type.setSymbolType(SemanticSymbol.SymbolType.SymbolInt);
            type.setArraySize(0);
            symbolTable.put(newType.name, type);
            node.newType = type;
        } else if (exisitingType.name.equals("float")) {
            // Case 3: new type is a float
            SemanticSymbol type = new SemanticSymbol(newType.name, SemanticSymbol.SymbolClass.TypeDecleration);
            type.setSymbolType(SemanticSymbol.SymbolType.SymbolFloat);
            type.setArraySize(0);
            symbolTable.put(newType.name, type);
            node.newType = type;
        } else {
            // Case 4: new type is an alias of another custom type

            // Create the new type to alias the lookuped type
            SemanticSymbol type = new SemanticSymbol(newType.name, SemanticSymbol.SymbolClass.TypeDecleration);
            type.setArraySize(0);

            // Lookup
            SemanticSymbol lookup = symbolTable.get(exisitingType.name);
            if (lookup == null) {
                error("Semantic error: " + exisitingType.name + " is not a defined type", exisitingType);
                // Alias the error type if failed
                type.setSymbolType(errorType);
            } else if (lookup.getSymbolClass() != SemanticSymbol.SymbolClass.TypeDecleration) {
                error("Semantic error: " + exisitingType.name + " is not a defined type", exisitingType);
                type.setSymbolType(errorType);
            } else {
                type.setSymbolType(lookup);
            }

            symbolTable.put(newType.name, type);
            node.newType = type;
        }
        node.lineNumber = currentLine;
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
            error("Semantic Error: " + type.name + " does not name a valid type", type);
            typeSymbol = errorType;
        }
        if (typeSymbol.getSymbolClass() != SemanticSymbol.SymbolClass.TypeDecleration) {
            error("Semantic Error: " + type.name + " does not name a valid type", type);
            typeSymbol = errorType;
        }

        // Create new symbol table entries for each new variable
        for (ID var : varNames) {
            if (symbolTable.get(var.name) != null) {
                error("Semantic Error: variable " + var.name + " is already defined", var);
                // Rename the id to an error name that can never be referenced
                // This will keep function parameter counts what the programmer wanted
                var.name = "$error" + tempIncrement;
                tempIncrement++;
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
                if (typeSymbol.getSymbolTypeReference().getSymbolType() != SemanticSymbol.SymbolType.SymbolError) {
                    error("Semantic error: " + typeSymbol.getName() + " is not a 1st order derived type", type);
                }
                // Remove initializer if it is invalid
                initializer = null;
            } else {
                // Check float and convert to int if needed
                if (initializer instanceof FloatLit) {
                    if (typeSymbol.getName().equals("int")) {
                        // Convert to int by grammar conversion rules
                        float val = ((FloatLit) initializer).val;
                        initializer = new IntLit();
                        ((IntLit) initializer).val = (int) val;
                    } else if (typeSymbol.getSymbolType() != SemanticSymbol.SymbolType.SymbolFloat) {
                        error("Semantic Error: Attempted to assign float to integer variables", initializer);
                        initializer = null;
                    }
                }

                // Type check int constant values
                if (initializer instanceof IntLit) {
                    if (typeSymbol.getSymbolType() != SemanticSymbol.SymbolType.SymbolInt) {
                        error("Semantic Error: Attempted to assign int to float values", initializer);
                        initializer = null;
                    }
                }
            }
        }

        // Create and push AST node
        VarDec node = new VarDec();
        node.type = typeSymbol;
        node.vars = varSymbols;
        node.init = initializer;
        node.lineNumber = type.lineNumber;
        semanticStack.addFirst(node);
    }

    // Analysis for an array type declaration. Creates a temporary
    // type and pushes an identifier that references it to the
    // semantic stack
    public void semaArrayType() {
        ID type = (ID)semanticStack.removeFirst();
        IntLit literal = (IntLit)semanticStack.removeFirst();
        if (literal.val <= 0) {
            error("Semantic error: Attempted to create array type with size <= 0", literal);
            // Give it an array size of 1 anyways because we are nice
            literal.val = 1;
        }

        String tempName = "$temp" + tempIncrement;
        tempIncrement++;

        SemanticSymbol newType = new SemanticSymbol(tempName, SemanticSymbol.SymbolClass.TypeDecleration);
        if (type.name.equals("int")) {
            newType.setSymbolType(SemanticSymbol.SymbolType.SymbolInt);
        } else if (type.name.equals("float")) {
            newType.setSymbolType(SemanticSymbol.SymbolType.SymbolFloat);
        } else {
            error("Semantic error: Array must be of type int or float", type);
            newType.setSymbolType(SemanticSymbol.SymbolType.SymbolError);
        }
        newType.setArraySize(literal.val);
        symbolTable.put(tempName, newType);

        // ID reference to this new type
        ID reference = new ID();
        reference.name = tempName;
        reference.lineNumber = type.lineNumber;
        semanticStack.addFirst(reference);
    }

    public void semaVariableReference(String name) {
        SemanticSymbol lookup = symbolTable.get(name);
        if (lookup == null || lookup.getSymbolClass() != SemanticSymbol.SymbolClass.VarDeclaration) {
            error("Semantic error: " + name + " is not a declared variable");
            lookup = errorVar;
        }

        VarReference node = new VarReference();
        node.reference = lookup;
        node.index = null;
        node.type = lookup.getSymbolTypeReference();
        node.lineNumber = currentLine;
        semanticStack.addFirst(node);
    }

    public void semaVariableReferenceIndex() {
        Expr index = (Expr) semanticStack.removeFirst();
        VarReference variable = (VarReference) semanticStack.peekFirst();
        if (!index.type.getName().equals("int")) {
            error("Semantic error: Array index must be of type int", index);
        }
        if (variable.type.getArraySize() <= 0) {
            error("Semantic error: Type " + variable.type.getName() + " is not an array type", variable);
            return;
        }
        if (variable.type.getSymbolType() == SemanticSymbol.SymbolType.SymbolInt) {
            variable.type = symbolTable.get("int");
        } else if (variable.type.getSymbolType() == SemanticSymbol.SymbolType.SymbolFloat) {
            variable.type = symbolTable.get("float");
        } else if (variable.type.getSymbolType() == SemanticSymbol.SymbolType.SymbolError) {
            variable.type = errorType;
        } else {
            variable.type = variable.type.getSymbolTypeReference();
        }
        variable.index = index;
    }

    // Ensures topmost variable reference is not an array
    public void semaVariableReferenceArrayCheck() {
        VarReference var = (VarReference)semanticStack.peekFirst();
        if (var.type.getArraySize() > 0) {
            error("Semantic error: " + var.reference.getName() + " is an array but is not indexed into", var);
            // Downgrade the type to the base to stop future problems
            if (var.type.getSymbolType() == SemanticSymbol.SymbolType.SymbolInt) {
                var.type = symbolTable.get("int");
            } else if (var.type.getSymbolType() == SemanticSymbol.SymbolType.SymbolFloat) {
                var.type = symbolTable.get("float");
            } else if (var.type.getSymbolType() == SemanticSymbol.SymbolType.SymbolError) {
                var.type = errorType;
            } else {
                var.type = var.type.getSymbolTypeReference();
            }
        }
    }

    // Returns whether one type can be implicitly converted to another
    private boolean semaCanConvertType(SemanticSymbol src, SemanticSymbol dst, Node fault) {
        // Error type can be converted to or from anything
        if (src == errorType || dst == errorType) {
            return true;
        }
        if (src != dst) {
            if (src.getName().equals("float") && dst.getName().equals("int")) {
                error("Semantic error: cannot convert float to int", fault);
                return false;
            }
            if (src.getName().equals("int")) {
                if (dst.getInferredPrimitive() != SemanticSymbol.SymbolType.SymbolInt) {
                    error("Semantic error: cannot assign int to type " + dst.getName(), fault);
                    return false;
                }
            } else if (src.getName().equals("float")) {
                if (dst.getInferredPrimitive() != SemanticSymbol.SymbolType.SymbolFloat) {
                    error("Semantic error: cannot assign float to type " + dst.getName(), fault);
                    return false;
                }
            } else {
                error("Semantic error: " + src.getName() + " and " + dst.getName() + " are incompatible types", fault);
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
            error("Semantic error: " + variableID.name + " is not a declared variable", variableID);
            variable = errorVar;
        }
        if (variable.getSymbolTypeReference().getArraySize() <= 0 && index != null) {
            error("Semantic error: " + variableID.name + " is not of an array type", variableID);
            // Index is null now lol
            index = null;
        }

        SemanticSymbol baseType = variable.getSymbolTypeReference();
        if (index != null) {
            if (!index.type.getName().equals("int")) {
                error("Semantic error: Array index must be of type int", index);
            }
        }
        // If type is an array, get the base type for type checking
        if (baseType.getArraySize() > 0 || index != null) {
            if (variable.getSymbolTypeReference().getSymbolType() == SemanticSymbol.SymbolType.SymbolInt) {
                baseType = symbolTable.get("int");
            } else if (variable.getSymbolTypeReference().getSymbolType() == SemanticSymbol.SymbolType.SymbolFloat) {
                baseType = symbolTable.get("float");
            } else if (variable.getSymbolTypeReference().getSymbolType() == SemanticSymbol.SymbolType.SymbolError) {
                baseType = errorType;
            } else {
                baseType = variable.getSymbolTypeReference().getSymbolTypeReference();
            }
        }

        if (!semaCanConvertType(assignment.type, baseType, assignment)) {
        }
        AssignStat node = new AssignStat();
        node.left = variable;
        node.right = assignment;
        node.index = index;
        node.lineNumber = variableID.lineNumber;
        semanticStack.addFirst(node);
    }

    // Returns whether one type can be implicitly converted to another without giving an error
    private boolean semaCanConvertTypeNoError(SemanticSymbol src, SemanticSymbol dst) {
        // Error type can be converted to or from anything
        if (src == errorType || dst == errorType) {
            return true;
        }
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
            // If there is an error type try to infer the possibly nonerror type
            if (left.type.getSymbolType() != SemanticSymbol.SymbolType.SymbolError) {
                node.type = left.type;
            } else {
                node.type = right.type;
            }
            node.convertLeft = false;
        } else if (semaCanConvertTypeNoError(left.type, right.type)) {
            node.left = left;
            node.right = right;
            if (right.type.getSymbolType() != SemanticSymbol.SymbolType.SymbolError) {
                node.type = right.type;
            } else {
                node.type = left.type;
            }
            node.convertLeft = true;
        } else {
            error("Semantic error: type mismatch between " + left.type.getName() + " and " + right.type.getName(), left);
            node.left = left;
            node.right = right;
            // No good guess on what to infer so error type
            node.type = errorType;
            node.convertLeft = false;
        }
        node.lineNumber = left.lineNumber;

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
            error("Semantic error: type mismatch between " + left.type.getName() + " and " + right.type.getName(), left);
            node.left = left;
            node.right = right;
        }
        node.type = symbolTable.get("int");
        node.lineNumber = left.lineNumber;

        semanticStack.addFirst(node);
    }

    public void semaLogicBinOp(LogicBinOp node) {
        // get the left and right
        Expr right = (Expr)semanticStack.removeFirst();
        Expr left = (Expr)semanticStack.removeFirst();

        // Make sure they are both integers
        if (!left.type.getName().equals("int") || !right.type.getName().equals("int")) {
            error("Logical comparison operator only acts on integers", left);
        }
        node.left = left;
        node.right = right;
        node.type = errorType;
        node.lineNumber = left.lineNumber;

        semanticStack.addFirst(node);
    }

    public void semaIfStart() {
        // get the expression
        Expr cond = (Expr)semanticStack.removeFirst();

        // condition must be an integer type
        if (!cond.type.getName().equals("int")) {
            error("Semantic error: condition must be an integer", cond);
        }

        // Build the node
        IfStat node = new IfStat();
        node.cond = cond;
        node.finalized = false;
        node.lineNumber = currentLine;
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
        node.lineNumber = currentLine;
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

    public void semaWhileStart() {
        Expr cond = (Expr)semanticStack.removeFirst();

        // condition must be an integer type
        if (!cond.type.getName().equals("int")) {
            error("Semantic error: condition must be an integer", cond);
        }

        // Increment loop counter
        loopCounter++;

        // Build the node
        WhileStat node = new WhileStat();
        node.cond = cond;
        node.finalized = false;
        node.lineNumber = currentLine;
        semanticStack.addFirst(node);
    }

    public void semaWhileBlock() {
        Deque<Stat> statements = new ArrayDeque<>();
        while (!semanticStack.isEmpty()) {
            Stat statement = (Stat)semanticStack.peekFirst();
            if (statement instanceof WhileStat) {
                if (!((WhileStat)statement).finalized) {
                    break;
                }
            }
            statements.addFirst((Stat)semanticStack.removeFirst());
        }

        // Decrement loop counter
        loopCounter--;

        // Add the statements
        WhileStat node = (WhileStat)semanticStack.peekFirst();
        for (Stat stat : statements) {
            node.stats.add(stat);
        }
        node.finalized = true;
    }

    public void semaForStart() {
        Expr to = (Expr)semanticStack.removeFirst();
        Expr from = (Expr)semanticStack.removeFirst();
        ID varID = (ID)semanticStack.removeFirst();

        // Perform a lookup first
        SemanticSymbol variable = symbolTable.get(varID.name);
        if (variable == null || variable.getSymbolClass() != SemanticSymbol.SymbolClass.VarDeclaration) {
            error("Semantic error: " + varID.name + " is not a defined variable", varID);
            variable = errorVar;
        }

        // Type check
        if (!semaCanConvertType(to.type, variable.getSymbolTypeReference(), to) ||
                !semaCanConvertType(from.type, variable.getSymbolTypeReference(), from)) {
        }

        // Loop counter is incremented
        loopCounter++;

        // Build the for statement
        ForStat node = new ForStat();
        node.start = from;
        node.end = to;
        node.var = variable;
        node.finalized = false;
        node.lineNumber = currentLine;
        semanticStack.addFirst(node);
    }

    public void semaForBlock() {
        Deque<Stat> statements = new ArrayDeque<>();
        while (!semanticStack.isEmpty()) {
            Stat statement = (Stat)semanticStack.peekFirst();
            if (statement instanceof ForStat) {
                if (!((ForStat)statement).finalized) {
                    break;
                }
            }
            statements.addFirst((Stat)semanticStack.removeFirst());
        }

        // Decrement loop counter
        loopCounter--;

        // Add the statements
        ForStat node = (ForStat) semanticStack.peekFirst();
        for (Stat stat : statements) {
            node.stats.add(stat);
        }
        node.finalized = true;
    }

    public void semaBreak() {
        BreakStat node = new BreakStat();
        node.lineNumber = currentLine;
        if (loopCounter <= 0) {
            error("Semantic error: break statement must be inside a loop", node);
            return;
        }

        semanticStack.addFirst(node);
    }

    // And so functions begin...
    public void semaFunctionStart() {
        ID name = (ID)semanticStack.removeFirst();
        if (symbolTable.get(name.name) != null) {
            error("Semantic error: " + name.name + "is already defined", name);
            name.name = "$errorFunc" + tempIncrement;
            tempIncrement++;
        }

        // Create symbol table entry for this function
        SemanticSymbol symbol = new SemanticSymbol(name.name, SemanticSymbol.SymbolClass.FunctionDeclatation);
        symbolTable.put(name.name, symbol);
        currentFunction = symbol;

        // Enter function scope
        symbolTable.beginScope();

        // Create AST node
        FunDec node = new FunDec();
        node.function = symbol;
        node.lineNumber = currentLine;
        semanticStack.addFirst(node);
    }

    public void semaFunctionArgs() {
        // Get the declarations off the stack
        Deque<VarDec> declarations = new ArrayDeque<>();
        while (semanticStack.peekFirst() instanceof VarDec) {
            declarations.addFirst((VarDec)semanticStack.removeFirst());
        }

        // Extract the symbols into a list
        ArrayList<SemanticSymbol> args = new ArrayList<>();
        for (VarDec dec : declarations) {
            args.add(dec.vars.get(0));
        }

        // If there are args put them in the function symbol
        if (args.size() > 0) {
            currentFunction.setFunctionParameters(args);
        }
    }

    public void semaFunctionReturnType() {
        ID type = (ID)semanticStack.removeFirst();
        SemanticSymbol symbol = symbolTable.get(type.name);
        if (symbol == null || symbol.getSymbolClass() != SemanticSymbol.SymbolClass.TypeDecleration) {
            error("Semantic error: " + type.name + " does not name a defined type", type);
            symbol = errorType;
        }
        currentFunction.setFunctionReturnType(symbol);
    }

    public void semaFunctionBlock() {
        Deque<Stat> statements = new ArrayDeque<>();
        while (semanticStack.peekFirst() instanceof Stat) {
            statements.addFirst((Stat)semanticStack.removeFirst());
        }

        // Add the statements
        FunDec node = (FunDec) semanticStack.peekFirst();
        for (Stat stat : statements) {
            node.stats.add(stat);
        }
        currentFunction = null;
        symbolTable.endScope();
    }

    public void semaReturn() {
        Expr ret = (Expr)semanticStack.removeFirst();
        ReturnStat node = new ReturnStat();
        node.lineNumber = currentLine;
        if (currentFunction == null) {
            error("Semantic error: return cannot appear outside a function block", node);
            return;
        }
        if (currentFunction.getFunctionReturnType() == null) {
            error("Semantic error: function with no return type cannot return", node);
            return;
        }
        if (!semaCanConvertType(ret.type, currentFunction.getFunctionReturnType(), ret)) {
            return;
        }
        node.retVal = ret;
        semanticStack.addFirst(node);
    }

    public void semaFunctionCall() {
        Deque<Expr> args = new ArrayDeque<>();
        while (semanticStack.peekFirst() instanceof Expr) {
            args.addFirst((Expr)semanticStack.removeFirst());
        }
        ID functionID = (ID)semanticStack.removeFirst();

        // Attempt a lookup
        SemanticSymbol function = symbolTable.get(functionID.name);
        if (function == null || function.getSymbolClass() != SemanticSymbol.SymbolClass.FunctionDeclatation) {
            error("Semantic error: " + functionID.name + " is not a defined function", functionID);
            function = errorFunc;
        }

        FunCall call = new FunCall();
        call.func = function;
        call.type = function.getFunctionReturnType();

        // Compare argument count
        if (function.getFunctionParameters() == null) {
            if (args.size() != 0 && function != errorFunc) {
                error("Semantic error: " + functionID.name + " does not take any parameters", functionID);
            }
        } else if (function.getFunctionParameters().size() != args.size()) {
            error("Semantic error: attempt to call " + functionID.name + " with invalid number of parameters", functionID);
        } else {
            // type check and add the parameters
            for (SemanticSymbol arg : function.getFunctionParameters()) {
                Expr ex = args.removeFirst();
                if (!semaCanConvertType(ex.type, arg.getSymbolTypeReference(), ex)) {
                }
                call.args.add(ex);
            }
        }
        call.lineNumber = functionID.lineNumber;
        semanticStack.addFirst(call);
    }

    public void semaProcedureCall() {
        // Get the function and wrap it into a procedure statement
        FunCall call = (FunCall)semanticStack.removeFirst();
        ProcedureStat node = new ProcedureStat();
        node.funCall = call;
        node.lineNumber = call.lineNumber;
        semanticStack.addFirst(node);
    }
}
