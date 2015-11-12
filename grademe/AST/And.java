package AST;

public class And extends LogicBinOp {

    public void accept(Visitor v) { v.visit(this); }
}
