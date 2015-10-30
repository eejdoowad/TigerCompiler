
// The TokenType enumerates the types possible syntactic categories
// in the tiger language.

// WE SHOULD SERIOUSLY CONSIDER RENAMING THIS BECAUSE A TOKENTYPE IS NOT A TOKEN
// Rather, A token is a lexeme-type pair, and possibly other info

public enum TokenType {
    NOACCEPT ("BAD"),
    COMMA ("COMMA"),
    COLON ("COLON"),
    SEMI ("SEMI"),
    LPAREN ("LPAREN"),
    RPAREN ("RPAREN"),
    LBRACK ("LBRACK"),
    RBRACK ("RBRACK"),
    LBRACE ("LBRACE"),
    RBRACE ("RBRACE"),
    PERIOD ("PERIOD"),
    PLUS ("PLUS"),
    MINUS ("MINUS"),
    MULT ("MULT"),
    DIV ("DIV"),
    EQ ("EQ"),
    NEQ ("NEQ"),
    LESSER ("LESSER"),
    GREATER ("GREATER"),
    LESSEREQ ("LESSEREQ"),
    GREATEREQ ("GREATEREQ"),
    AND ("AND"),
    OR ("OR"),
    ASSIGN ("ASSIGN"),
    ID ("ID"),
    INTLIT ("INTLIT"),
    FLOATLIT ("FLOATLIT"),
    KARRAY ("ARRAY"),
    KBREAK ("BREAK"),
    KDO ("DO"),
    KELSE ("ELSE"),
    KFOR ("FOR"),
    KFUNC ("FUNC"),
    KIF ("IF"),
    KIN ("IN"),
    KINT ("INT"),
    KFLOAT ("FLOAT"),
    KLET ("LET"),
    KOF ("OF"),
    KTHEN ("THEN"),
    KTO ("TO"),
    KTYPE ("TYPE"),
    KVAR ("VAR"),
    KWHILE ("WHILE"),
    KENDIF ("ENDIF"),
    KBEGIN ("BEGIN"),
    KEND ("END"),
    KENDDO ("ENDDO"),
    KRETURN ("RETURN"),
    ENDOFFILE ("$");

    private final String name;

    private TokenType(String s) {
        name = s;
    }

    @Override
    public String toString() {
        return this.name;
    }

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