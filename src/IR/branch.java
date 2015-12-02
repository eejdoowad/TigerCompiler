package IR;

import java.util.ArrayList;

public abstract class branch extends controlFlowInstruction {

    public Operand left;
    public Operand right;
    public LabelOp labelOp;

    public branch(Operand left, Operand right, LabelOp labelOp){
        this.left = left;
        this.right = right;
        this.labelOp = labelOp;
    }
    public branch(Operand left, Operand right, LabelOp labelOp, boolean isInteger){
        this.left = left;
        this.right = right;
        this.labelOp = labelOp;
        this.isInteger = isInteger;
    }

    public Var def(){
        return null;
    }

    public ArrayList<Var> use(){
        ArrayList<Var> uses = new ArrayList<>();
        if (left instanceof Var) uses.add((Var)left);
        if (right instanceof Var) uses.add((Var)right);
        return uses;
    }

    public void replaceDef(Var old, Register n){
        System.out.println("ERROR branch.replaceDef()");
    }
    public void replaceUses(Var old, Register n){
        if (left == old) left = n;
        if (right == old) right = n;
    }

    public abstract String op();

    public String toString(){
        return op() + (isInt() ? "" : "f") + ", " + left + ", " + right + ", " + labelOp;
    }
}
