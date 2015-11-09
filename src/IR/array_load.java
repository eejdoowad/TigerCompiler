package IR;

import AST.SemanticSymbol;

public class array_load extends IR {

    // the l-value is always Named
    // wouldn't make sense to assign to a temp
    public Operand left;
    // the right hand side could be anything
    public Operand var;
    public Operand index;

    public array_load(Operand left, NamedVar var, Operand index){
        this.left = left;
        this.var = var;
        this.index = index;
    }

    public String toString(){
        return "array_load, " + left + ", " + var + ", " + index;
    }
}
