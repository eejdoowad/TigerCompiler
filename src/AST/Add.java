package AST;

public class Add extends ArithmeticBinOp {
    public void accept(Visitor v) { v.visit(this); }
}
