package IR;

import java.util.ArrayList;

public class array_store extends regularInstruction {

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

    public Var def(){
        if (right instanceof Var) return (Var)right;
        else return null;
    }

    public ArrayList<Var> use(){
        ArrayList<Var> uses = new ArrayList<>();
        if (var instanceof Var) uses.add((Var)var);
        if (index instanceof Var) uses.add((Var)index);
        return uses;
    }

    public String toString(){
        return "array_store, " + var + ", " + index + ", " + right;
    }
    public void accept(IRVisitor v) { v.visit(this); }
}
