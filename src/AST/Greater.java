package AST;

public class Greater extends ComparisonBinOp{
    public void accept(Visitor v) { v.visit(this); }
}
