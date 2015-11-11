import AST.SemanticSymbol;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.ArrayList;
// Temp: keeping track of Phase II additions:
// 1. Symbol Table
// 2. AST and associated classes
// 3. Action Symbols <-- need to update parser and grammar
// 4. Semantic Record



// In the Tiger Language, there are exactly two scopes:
// 1. Global scope
// 2. Function Scope (each of which is nested in the gobal scope)

// All types, variables, and functions are declared globally before hand as in:
// let
//      type declarations
//      var declarations
//      function declarations
// in
//      statements
// end

// All declarations are sequential. As in, you must declare a type before you can use it to define another type
// All declarations are visible everywhere after their declaration (you can see a function from another function if
// the second function is declared after the function


// Make sure to add built in types (int and float) and functions (see proj1 description) on startup

// The SymbolTable is implemented as

public class SymbolTable {
    // Stack of symbol tables for each scope that is currently in use
    private Deque<HashMap<String, SemanticSymbol>> scopeStack;

    // List of symbol tables that have their scopes finalized
    private ArrayList<HashMap<String, SemanticSymbol>> finalizedScopes;

    public SymbolTable() {
        // Init stack and scope graveyard
        scopeStack = new ArrayDeque<>();
        finalizedScopes = new ArrayList<>();

        // Create the global symbol table and push to top of stack
        HashMap<String, SemanticSymbol> globalTable = new HashMap<>();
        scopeStack.addFirst(globalTable);

        // Create int and float types
        SemanticSymbol intSymbol = new SemanticSymbol("int", SemanticSymbol.SymbolClass.TypeDecleration);
        intSymbol.setSymbolType(SemanticSymbol.SymbolType.SymbolInt);
        intSymbol.setArraySize(0);
        SemanticSymbol floatSymbol = new SemanticSymbol("float", SemanticSymbol.SymbolClass.TypeDecleration);
        floatSymbol.setSymbolType(SemanticSymbol.SymbolType.SymbolFloat);
        floatSymbol.setArraySize(0);
        put("int", intSymbol);
        put("float", floatSymbol);

        // Standard library functions
        SemanticSymbol printi = new SemanticSymbol("printi", SemanticSymbol.SymbolClass.FunctionDeclatation);
        printi.setFunctionReturnType(null);
        SemanticSymbol num = new SemanticSymbol("num", SemanticSymbol.SymbolClass.VarDeclaration);
        num.setSymbolType(intSymbol);
        ArrayList<SemanticSymbol> args = new ArrayList<>();
        args.add(num);
        printi.setFunctionParameters(args);
        put("printi", printi);
    }

    // Enters a new scope
    public void beginScope() {
        HashMap<String, SemanticSymbol> newScope = new HashMap<>();
        scopeStack.addFirst(newScope);
    }

    // Leaves a scope
    public void endScope() {
        if (scopeStack.size() <= 1) {
            System.out.println("Error: Attempting to pop global scope");
            return;
        }

        // Add top of stack to finalized list of symbol tables
        finalizedScopes.add(scopeStack.removeFirst());
    }

    // Adds a symbol in the current scope
    public void put(String name, SemanticSymbol symbol) {
        scopeStack.peekFirst().put(name, symbol);
    }

    // Performs a lookup of a symbol in all the active scopes
    // Returns the symbol closest to the current scope
    public SemanticSymbol get(String name) {
        SemanticSymbol symbol = null;
        for (HashMap<String, SemanticSymbol> scope : scopeStack) {
            SemanticSymbol temp = scope.get(name);
            if (temp != null) {
                symbol = temp;
            }
        }
        return symbol;
    }

    // Renames a symbol in the symbol table. Useful for when temporaries are assigned a name
    // Symbols internal name is only replaced when symbol is found
    // Returns true on success, false otherwise
    public boolean rename(SemanticSymbol symbol, String newName) {
        // Find the scope the symbol is in
        HashMap<String, SemanticSymbol> table = null;
        for (HashMap<String, SemanticSymbol> scope : scopeStack) {
            SemanticSymbol temp = scope.get(symbol.getName());
            if (temp != null && temp == symbol) {
                table = scope;
                break;
            }
        }
        if (table != null) {
            table.remove(symbol.getName());
            table.put(newName, symbol);
            symbol.setName(newName);
            return true;
        }
        return false;
    }
}


//• variables: type, procedure level, frame offset
//• types: type descriptor, data size/alignment
//• constants: type, value
//• procedures: formals (names/types), result type, block information (local
//decls.), frame size


//Rule 1: Use an identifier only if defined in enclosing scope
//Rule 2: Do not declare identifiers of the same kind with
//identical names more than once in the same scope