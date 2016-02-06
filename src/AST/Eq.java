package AST;

public class Eq extends ComparisonBinOp {
    public String type(){return "Equal";}
    public void accept(Visitor v) { v.visit(this); }
}
