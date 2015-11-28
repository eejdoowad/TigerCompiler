package IR;

// represents assigning a single value to every element of an array
// var X : ArrayInt := 10;


public class array_assign extends IR {

    public NamedVar var;
    public IntImmediate num;
    public Operand right;

    public array_assign(NamedVar var, IntImmediate num, Operand right){
        this.var = var;
        this.num = num;
        this.right = right;
    }

    public String toString(){
        return "assign, " + var + ", " + num + ", " + right;
    }
}
