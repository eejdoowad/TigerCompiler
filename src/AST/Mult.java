package AST;

public class Mult extends ArithmeticBinOp {
    public void accept(Visitor v) { v.visit(this); }
}
