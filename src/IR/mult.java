package IR;

public class mult extends binop {

    public mult(Operand left, Operand right, Operand result){
        super(left, right, result);
    }
    public mult(Operand left, Operand right, Operand result, Boolean isInteger){
        super(left, right, result, isInteger);
    }
    public String op(){
        return  "mult";
    }
    public void accept(IRVisitor v) { v.visit(this); }
}
