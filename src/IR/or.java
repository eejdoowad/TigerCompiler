package IR;

import AST.SemanticSymbol;

public class or extends binop {

    public or(SemanticSymbol symbolLeft, SemanticSymbol symbolRight){
        super(symbolLeft, symbolRight);
        op = BinOpType.OR;
    }
}
