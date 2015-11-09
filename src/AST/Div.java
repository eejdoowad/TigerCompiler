package AST;

public class Div extends ArithmeticBinOp {

    public void accept(Visitor v) { v.visit(this); }
}
