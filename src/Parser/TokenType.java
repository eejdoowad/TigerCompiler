package Parser;
// The TokenType enumerates the types possible syntactic categories
// in the tiger language.

// WE SHOULD SERIOUSLY CONSIDER RENAMING THIS BECAUSE A TOKENTYPE IS NOT A TOKEN
// Rather, A token is a lexeme-type pair, and possibly other info

public enum TokenType {
    NOACCEPT ("BAD", ""),
    COMMA ("COMMA", ","),
    COLON ("COLON", ":"),
    SEMI ("SEMI", ";"),
    LPAREN ("LPAREN", "("),
    RPAREN ("RPAREN", ")"),
    LBRACK ("LBRACK", "["),
    RBRACK ("RBRACK", "]"),
    LBRACE ("LBRACE", "{"),
    RBRACE ("RBRACE", "}"),
    PERIOD ("PERIOD", "."),
    PLUS ("PLUS", "+"),
    MINUS ("MINUS", "-"),
    MULT ("MULT", "*"),
    DIV ("DIV", "/"),
    EQ ("EQ", "="),
    NEQ ("NEQ", "<>"),
    LESSER ("LESSER", "<"),
    GREATER ("GREATER", ">"),
    LESSEREQ ("LESSEREQ", "<="),
    GREATEREQ ("GREATEREQ", ">="),
    AND ("AND", "&"),
    OR ("OR", "|"),
    ASSIGN ("ASSIGN", ":="),
    ID ("ID", "ID"),
    INTLIT ("INTLIT", "INTLIT"),
    FLOATLIT ("FLOATLIT", "FLOATLIT"),
    KARRAY ("ARRAY", "array"),
    KBREAK ("BREAK", "break"),
    KDO ("DO", "do"),
    KELSE ("ELSE", "else"),
    KFOR ("FOR", "for"),
    KFUNC ("FUNC", "function"),
    KIF ("IF", "if"),
    KIN ("IN", "in"),
    KINT ("INT", "int"),
    KFLOAT ("FLOAT", "float"),
    KLET ("LET", "let"),
    KOF ("OF", "of"),
    KTHEN ("THEN", "then"),
    KTO ("TO", "to"),
    KTYPE ("TYPE", "type"),
    KVAR ("VAR", "var"),
    KWHILE ("WHILE", "while"),
    KENDIF ("ENDIF", "endif"),
    KBEGIN ("BEGIN", "begin"),
    KEND ("END", "end"),
    KENDDO ("ENDDO", "enddo"),
    KRETURN ("RETURN", "return"),
    ENDOFFILE ("$", "$");

    private final String name;
    private final String lexeme;

    private TokenType(String s, String lexeme) {
        name = s;
        this.lexeme = lexeme;
    }

    @Override
    public String toString() {
        return this.name;
    }
    public String toLexeme() { return this.lexeme; }

    public boolean isSequencePoint(){
        switch(this){
            case SEMI :
            case KEND :
            case KBEGIN :
            case KLET:
            case KDO :
            case KTHEN :
            case KELSE :
            case KENDDO :
                return true;
            default :
                return false;
        }
    }
};