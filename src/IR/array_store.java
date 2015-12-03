package IR;

import java.util.ArrayList;

public class array_store extends regularInstruction {

    // the l-value is always Named
    public Var var;
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

    public void replaceDef(Var old, Register n){
        if (right == old) right = n;
        else System.out.println("ERROR array_store.replaceDef()");
    }
    public void replaceUses(Var old, Register n){
        if (index == old) index = n;
    }


    public String toString(){
        return "array_store, " + var + ", " + index + ", " + right;
    }
    public void accept(IRVisitor v) { v.visit(this); }
}
