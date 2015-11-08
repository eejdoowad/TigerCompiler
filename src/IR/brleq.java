package IR;

import AST.SemanticSymbol;

public class brleq extends branch{

    public brleq(SemanticSymbol symbolLeft, SemanticSymbol symbolRight) {
        super(symbolLeft, symbolRight);
        type = BranchType.BRLEQ;
    }
}
