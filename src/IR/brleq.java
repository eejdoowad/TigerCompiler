package IR;

public class brleq extends branch{

    public brleq(Operand left, Operand right, LabelOp labelOp, boolean isInteger){
        super(left, right, labelOp, isInteger);
    }
    public String op(){
        return  "brleq";
    }
    public void accept(IRVisitor v) { v.visit(this); }
}
