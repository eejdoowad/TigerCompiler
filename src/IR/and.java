package IR;

public class and extends binop {

    public and(Operand left, Operand right, Operand result){
        super(left, right, result);
    }
    public String op(){
        return  "and";
    }
    public void accept(IRVisitor v) { v.visit(this); }
}
