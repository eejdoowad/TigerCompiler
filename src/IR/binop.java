package IR;

import AST.SemanticSymbol;


// I'm not sure anymore if the instruction should have a semantic Symbol
// as its left and right members


// Now i'm thinking perhaps we should make a new class Operand
// where an operand is either

public abstract class binop extends IR {

    public enum BinOpType {
        ADD("add"), SUB("sub"), MULT("mult"), DIV("div"), AND("and"), OR("or");
        private final String str;
        private BinOpType(String s) { str = s; }
    }
    public BinOpType op;

    public SemanticSymbol symbolLeft, symbolRight;

    public binop(SemanticSymbol symbolLeft, SemanticSymbol symbolRight){
        this.symbolLeft = symbolLeft;
        this.symbolRight = symbolRight;
    }

    public String toString(){
        return op.str + ", " + symbolLeft.getName() + ", " + symbolRight.getName();
    }
}
