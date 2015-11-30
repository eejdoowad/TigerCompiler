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

    public Var def(){
        return null;
    }

    public ArrayList<Var> use(){
        ArrayList<Var> uses = new ArrayList<>();
        if (left instanceof Var) uses.add((Var)left);
        if (right instanceof Var) uses.add((Var)right);
        return uses;
    }
}
