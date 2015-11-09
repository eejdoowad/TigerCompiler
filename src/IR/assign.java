package IR;

import AST.SemanticSymbol;

public class assign extends IR {

    // there is no scenario in which the l-value is a temporary
    public NamedVar var;
    // the right hand side could be anything
    public Operand right;

    public assign(NamedVar var, Operand right){
        this.var = var;
        this.right = right;
    }

    public String toString(){
        return "assign, " + var + ", " + right;
    }
}
