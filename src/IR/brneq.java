package IR;

public class brneq extends branch {

    public brneq(Operand left, Operand right, LabelOp labelOp, boolean isInteger){
        super(left, right, labelOp, isInteger);
    }
    public String op(){
        return  "brneq";
    }
    public void accept(IRVisitor v) { v.visit(this); }
}
