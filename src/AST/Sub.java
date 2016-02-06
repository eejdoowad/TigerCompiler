package AST;

public class Sub extends ArithmeticBinOp {
    public String type(){return "Minus";}
    public void accept(Visitor v) { v.visit(this); }
}
