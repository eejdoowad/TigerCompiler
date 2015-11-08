package IR;

import AST.SemanticSymbol;

public class sub extends binop {

    public sub(SemanticSymbol symbolLeft, SemanticSymbol symbolRight){
        super(symbolLeft, symbolRight);
        op = BinOpType.SUB;
    }
}
