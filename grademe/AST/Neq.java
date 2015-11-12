package AST;

public class Neq extends ComparisonBinOp {

    public void accept(Visitor v) { v.visit(this); }
}
