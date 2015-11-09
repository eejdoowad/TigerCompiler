package AST;

public class Lesser extends ComparisonBinOp {
    public void accept(Visitor v) { v.visit(this); }
}
