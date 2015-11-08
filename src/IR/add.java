package IR;

import AST.SemanticSymbol;

public class add extends binop {

    public add(SemanticSymbol symbolLeft, SemanticSymbol symbolRight){
        super(symbolLeft, symbolRight);
        op = BinOpType.ADD;
    }
}
