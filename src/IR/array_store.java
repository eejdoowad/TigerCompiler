package IR;

public class array_store extends IR {

    // the l-value is always Named
    // wouldn't make sense to assign to a temp
    public NamedVar var;
    //
    public Operand index;
    // the right hand side could be anything
    public Operand right;

    public array_store(NamedVar var, Operand index, Operand right){
        this.var = var;
        this.index = index;
        this.right = right;
    }

    public String toString(){
        return "array_store, " + var + ", " + index + ", " + right;
    }
}
