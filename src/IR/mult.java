package IR;

public class mult extends binop {

    public mult(Operand left, Operand right, Operand result){
        super(left, right, result);
    }
    public String toString(){
        return  "mult, " + left + ", " + right + ", " + result;
    }
    public void accept(IRVisitor v) { v.visit(this); }
}
