package IR;

import AST.SemanticSymbol;

public abstract class branch extends IR {

    public enum BranchType {
        BREQ("breq"), BRNEQ("brneq"), BRLT("brlt"), BRGT("brgt"), BRLEQ("brleq"), BRGEQ("brgeq");
        private final String str;
        private BranchType(String s) { str = s; }
    }
    public BranchType type;

    public SemanticSymbol symbolLeft, symbolRight;

    public branch(SemanticSymbol symbolLeft, SemanticSymbol symbolRight) {
        this.symbolLeft = symbolLeft;
        this.symbolRight = symbolRight;
    }

    public String toString(){
        return type.str + ", " + symbolLeft.getName() + ", " + symbolRight.getName();
    }
}
