package IR;

public class or extends binop {

    public or(Operand left, Operand right, Operand result){
        super(left, right, result, true);
    }
    public String op(){
        return  "or";
    }
    public void accept(IRVisitor v) { v.visit(this); }
}
