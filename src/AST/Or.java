package AST;

public class Or extends LogicBinOp {

    public void accept(Visitor v) { v.visit(this); }
}
