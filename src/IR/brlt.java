package IR;

public class brlt extends branch {

    public brlt(Operand left, Operand right, LabelOp labelOp){
        super(left, right, labelOp);
    }
    public String toString(){
        return  "brlt, " + left + ", " + right + ", " + labelOp;
    }
    public void accept(IRVisitor v) { v.visit(this); }
}
