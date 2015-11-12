package AST;

public class Eq extends ComparisonBinOp {
    public void accept(Visitor v) { v.visit(this); }
}
