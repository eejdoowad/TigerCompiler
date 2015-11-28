package Parser;// Well... The grammar has been augmented with action symbols in phase 2
// Action symbols are used in our implementation to:
//     1. Generate the Symbol Table
//     2. Generate the Abstract Syntax Tree (AST)
// Later on, the the AST is traversed for semantic checking and intermediate code generation

// Basically, when an action symbol is encountered, the parser manipulates the semantic record
// to build the AST, and if a new declaration, update the symbol table

// thoughts: should be enumerable, there is a finite set of action symbols


import Parser.ActionSymbolType;
import Parser.Symbol;

public class ActionSymbol extends Symbol {
    ActionSymbolType type;
    public ActionSymbol(ActionSymbolType type){
        this.symbol = type.toString();
        this.type = type; // for now use type for the symbol....
    }

    public String toString(){
        return this.symbol;
    }

}
