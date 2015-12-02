package IR;

import java.util.ArrayList;

public class assign extends regularInstruction {

    public Operand var;
    // the right hand side could be anything
    public Operand right;

    public assign(Operand var, Operand right) {
        this.var = var;
        this.right = right;
        this.isInteger = true;
    }

    public assign(Operand var, Operand right, boolean isInt) {
        this.var = var;
        this.right = right;
        this.isInteger = isInt;
    }

    public Var def(){
        return (Var)var;
    }

    public ArrayList<Var> use(){
        ArrayList<Var> uses = new ArrayList<>();
        if (right instanceof Var) uses.add((Var)right);
        return uses;
    }

    public void replaceDef(Var old, Register n){
        if (var == old) var = n;
        else System.out.println("ERROR assign.replaceDef()");
    }
    public void replaceUses(Var old, Register n){
        if (right == old) right = n;
    }

    public String toString(){
        return (isInt() ? "assign, " : "assignf, ") + var + ", " + right;
    }
    public void accept(IRVisitor v) { v.visit(this); }
}
