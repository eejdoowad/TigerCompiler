package IR;

import AST.SemanticSymbol;

public class brlt extends branch {

    public brlt(SemanticSymbol symbolLeft, SemanticSymbol symbolRight) {
        super(symbolLeft, symbolRight);
        type = BranchType.BRLT;
    }

}
