package AST;

public class Neq extends ComparisonBinOp {
    public String type(){return "NotEqual";}
    public void accept(Visitor v) { v.visit(this); }
}
