package AST;

public class Sub extends ArithmeticBinOp {
    public void accept(Visitor v) { v.visit(this); }
}
