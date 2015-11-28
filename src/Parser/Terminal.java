package Parser;
// A Terminal Symbol corresponds to a token of a particular type
// all Terminals hardcoded for now based on enum TokenType


import Parser.Symbol;

public class Terminal extends Symbol {

    public TokenType type;

    public Terminal(TokenType type){
        this.symbol = type.toString();
        this.type = type; // for now use type for the symbol....
    }

    public String toString(){
        return this.symbol;
    }

}
