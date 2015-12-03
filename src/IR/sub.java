package IR;

public class sub extends binop {

    public sub(Operand left, Operand right, Operand result, Boolean isInteger){
        super(left, right, result, isInteger);
    }
    public String op(){
        return  "sub";
    }
    public void accept(IRVisitor v) { v.visit(this); }
}
