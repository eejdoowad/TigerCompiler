package IR;

// represents assigning a single value to every element of an array
// var X : ArrayInt := 10;


import java.util.ArrayList;

public class array_assign extends regularInstruction {

    public NamedVar var;
    public IntImmediate count;
    public Operand val;

    public array_assign(NamedVar var, IntImmediate count, Operand val, boolean isInteger){
        this.var = var;
        this.count = count;
        this.val = val;
        this.isInteger = isInteger;
    }

    public Var def(){
        return null;
    }

    public ArrayList<Var> use(){
        return new ArrayList<>();
    }

    public void replaceDef(Var old, Register n){
    }
    public void replaceUses(Var old, Register n){
    }

    public String toString(){
        return "array_assign, " + var + ", " + count + ", " + val;
    }
    public void accept(IRVisitor v) { v.visit(this); }
}
