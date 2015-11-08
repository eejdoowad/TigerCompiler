package IR;

import AST.SemanticSymbol;

public class brneq extends branch {

    public brneq(SemanticSymbol symbolLeft, SemanticSymbol symbolRight) {
        super(symbolLeft, symbolRight);
        type = BranchType.BRNEQ;
    }

}
