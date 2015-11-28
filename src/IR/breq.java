package IR;

public class breq extends branch{

    public breq(Operand left, Operand right, LabelOp labelOp){
        super(left, right, labelOp);
    }
    public String toString(){
        return  "breq, " + left + ", " + right + ", " + labelOp;
    }
}
