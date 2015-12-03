package IR;

public class brgeq extends branch {

    public brgeq(Operand left, Operand right, LabelOp labelOp, boolean isInteger){
        super(left, right, labelOp, isInteger);
    }
    public String op(){
        return  "brgeq";
    }
    public void accept(IRVisitor v) { v.visit(this); }
}
