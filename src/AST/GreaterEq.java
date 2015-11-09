package AST;

public class GreaterEq extends ComparisonBinOp {
    public void accept(Visitor v) { v.visit(this); }
}
