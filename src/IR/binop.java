package IR;

import AST.SemanticSymbol;

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
