package IR;

public class brneq extends branch {

    public brneq(Operand left, Operand right, LabelOp labelOp){
        super(left, right, labelOp);
    }

    public String toString(){
        return  "brneq, " + left + ", " + right + ", " + labelOp;
    }
}
