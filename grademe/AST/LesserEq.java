package AST;

public class LesserEq extends ComparisonBinOp {
    public void accept(Visitor v) { v.visit(this); }
}
