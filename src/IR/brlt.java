package IR;

public class brlt extends branch {

    public brlt(Operand left, Operand right, LabelOp labelOp){
        super(left, right, labelOp);
    }
    public brlt(Operand left, Operand right, LabelOp labelOp, boolean isInteger){
        super(left, right, labelOp, isInteger);
    }
    public String op(){
        return  "brlt";
    }
    public void accept(IRVisitor v) { v.visit(this); }
}
