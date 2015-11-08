package IR;

import AST.SemanticSymbol;

public class breq extends branch{

    public breq(SemanticSymbol symbolLeft, SemanticSymbol symbolRight) {
        super(symbolLeft, symbolRight);
        type = BranchType.BREQ;
    }
}
