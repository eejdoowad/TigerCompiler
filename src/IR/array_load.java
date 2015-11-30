package IR;

import java.util.ArrayList;

public class array_load extends regularInstruction {

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

    public Var def(){
        if (left instanceof Var) return (Var)left;
        else return null;
    }

    public ArrayList<Var> use(){
        ArrayList<Var> uses = new ArrayList<>();
        if (var instanceof Var) uses.add((Var)var);
        if (index instanceof Var) uses.add((Var)index);
        return uses;
    }


    public String toString(){
        return "array_load, " + left + ", " + var + ", " + index;
    }
}
