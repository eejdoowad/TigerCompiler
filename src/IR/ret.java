package IR;

import AST.SemanticSymbol;

public class ret extends IR {

    SemanticSymbol retVal;

    public ret(SemanticSymbol retVal){
        this.retVal = retVal;

    }
    public String toString(){
        return "return," + retVal.getName();
    }
}
