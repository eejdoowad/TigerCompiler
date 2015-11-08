package IR;

import AST.SemanticSymbol;

public class and extends binop {
    public and(SemanticSymbol symbolLeft, SemanticSymbol symbolRight){
        super(symbolLeft, symbolRight);
        op = BinOpType.AND;
    }
}
