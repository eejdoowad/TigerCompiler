package IR;

import java.util.ArrayList;

public class assign extends regularInstruction {

    public Operand var;
    // the right hand side could be anything
    public Operand right;

    public assign(Operand var, Operand right){
        this.var = var;
        this.right = right;
    }

    public Var def(){
        return (Var)var;
    }

    public ArrayList<Var> use(){
        ArrayList<Var> uses = new ArrayList<>();
        if (right instanceof Var) uses.add((Var)right);
        return uses;
    }


    public String toString(){
        return "assign, " + var + ", " + right;
    }
    public void accept(IRVisitor v) { v.visit(this); }
}
