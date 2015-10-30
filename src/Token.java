// A token consists of:
// 1. A lexeme (The actual string or name read by the scanner)
// 2. A Token Type (the syntactic category of the input word)
// 3. Other metadata like line number


// NOTE, A Token is NOT equivalent to a NonTerminal... it can contain much more data
// for example line location and token number
// THOUGH, this interpretatin needs review

public class Token {

    public TokenType type;
    public int line;
    public int number;

    public Token(TokenType type, int line, int number) {
        this.type = type;
        this.line = line;
        this.number = number;
    }

    public String toString(){
        return "(" + type + ", line " + line + ", num " + number + ")";
    }
}
