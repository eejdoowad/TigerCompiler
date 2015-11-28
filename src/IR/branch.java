package IR;

public abstract class branch extends IR {

    public Operand left;
    public Operand right;
    public LabelOp labelOp;

    public branch(Operand left, Operand right, LabelOp labelOp){
        this.left = left;
        this.right = right;
        this.labelOp = labelOp;
    }
}
