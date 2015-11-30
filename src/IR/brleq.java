package IR;

public class brleq extends branch{

    public brleq(Operand left, Operand right, LabelOp labelOp){
        super(left, right, labelOp);
    }
    public String toString(){
        return  "brleq, " + left + ", " + right + ", " + labelOp;
    }
    public void accept(IRVisitor v) { v.visit(this); }
}
