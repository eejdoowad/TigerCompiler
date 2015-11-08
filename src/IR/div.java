package IR;

import AST.SemanticSymbol;

public class div extends binop {
    public div(SemanticSymbol symbolLeft, SemanticSymbol symbolRight){
        super(symbolLeft, symbolRight);
        op = BinOpType.DIV;
    }
}
