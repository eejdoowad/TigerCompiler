package IR;

public class div extends binop {

    public div(Operand left, Operand right, Operand result, Boolean isInteger){
        super(left, right, result, isInteger);
    }
    public String op(){
        return  "div";
    }
    public void accept(IRVisitor v) { v.visit(this); }
}
