

// This class represents a grammar symbol
// (i'm considering renaming it to GrammarSymbol, so we don't get confused when we add a symbol table)

// A Symbol can take one of 3 forms:
//     1. NonTerminal
//     2. Terminal
//     3. Epsilon
//     4. ActionSymbol

// Epsilon is separated from Terminal because the parser treats them differently

//     1. If a Terminal is at the top of the parse stack, the parser will attempt
//        to match it to the token returned by the scanner
//     2. If Epsilon is at the top of the parse stack, the parser immediately pops
//        it off the stack
//     3. If a NonTerminal is at the top of the parse stack, the parser uses the
//        parsetable to determine the rule to use to replace the NonTerminal
//     4. If an ActionSymbol is at the top of the stack, the parser manipulates
//        the SemanticRecord and SymbolTable according to the action type

public abstract class Symbol {
    public String symbol; // The string users interpret as the symbol

}
