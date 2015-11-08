package IR;

import AST.SemanticSymbol;

public class assign extends IR {


    public SemanticSymbol symbolLeft, symbolRight;

    public assign(SemanticSymbol symbolLeft, SemanticSymbol symbolRight){
        this.symbolLeft = symbolLeft;
        this.symbolRight = symbolRight;
    }

    public String toString(){
        return "assign, " + symbolLeft.getName() + ", " + symbolRight.getName();
    }
}
