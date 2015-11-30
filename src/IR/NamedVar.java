package IR;

import SemanticAnalyzer.SemanticSymbol;

public class NamedVar extends Var {

    public String name;

    public NamedVar(SemanticSymbol symbol){
        this.type = symbol.getInferredPrimitive();
        this.name = symbol.getName();
    }

    public String toString(){
        return name;
    }
}
