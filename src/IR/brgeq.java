package IR;

public class brgeq extends branch {

    public brgeq(Operand left, Operand right, LabelOp labelOp){
        super(left, right, labelOp);
    }
    public String toString(){
        return  "brgeq, " + left + ", " + right + ", " + labelOp;
    }
}
