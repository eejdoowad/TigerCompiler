package IR;

import AST.SemanticSymbol;

public class mult extends binop {

    public mult(SemanticSymbol symbolLeft, SemanticSymbol symbolRight){
        super(symbolLeft, symbolRight);
        op = BinOpType.MULT;
    }
}
