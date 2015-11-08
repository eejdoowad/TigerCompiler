package IR;

import AST.SemanticSymbol;

public class brgeq extends branch {


    public brgeq(SemanticSymbol symbolLeft, SemanticSymbol symbolRight) {
        super(symbolLeft, symbolRight);
        type = BranchType.BRGEQ;
    }
}
