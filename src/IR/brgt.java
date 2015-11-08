package IR;

import AST.SemanticSymbol;

public class brgt extends branch {

    public brgt(SemanticSymbol symbolLeft, SemanticSymbol symbolRight) {
        super(symbolLeft, symbolRight);
        type = BranchType.BRGT;
    }
}
