package IR;

public class assign extends regularInstruction {

    // the l-value is always Named
    // wouldn't make sense to assign to a temp
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
