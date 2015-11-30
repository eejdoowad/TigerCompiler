package IR;

public class or extends binop {

    public or(Operand left, Operand right, Operand result){
        super(left, right, result);
    }
    public String toString(){
        return  "or, " + left + ", " + right + ", " + result;
    }
    public void accept(IRVisitor v) { v.visit(this); }
}
