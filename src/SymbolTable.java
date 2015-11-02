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

//    beginScope();
//    endScope();
//    put();
//    get();

}


//• variables: type, procedure level, frame offset
//• types: type descriptor, data size/alignment
//• constants: type, value
//• procedures: formals (names/types), result type, block information (local
//decls.), frame size


//Rule 1: Use an identifier only if defined in enclosing scope
//Rule 2: Do not declare identifiers of the same kind with
//identical names more than once in the same scope