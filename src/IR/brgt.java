package IR;

public class brgt extends branch {

    public brgt(Operand left, Operand right, LabelOp labelOp){
        super(left, right, labelOp);
    }
    public brgt(Operand left, Operand right, LabelOp labelOp, boolean isInteger){
        super(left, right, labelOp, isInteger);
    }
    public String op(){
        return  "brgt";
    }
    public void accept(IRVisitor v) { v.visit(this); }
}
