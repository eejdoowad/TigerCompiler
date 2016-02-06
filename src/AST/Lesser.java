package AST;

public class Lesser extends ComparisonBinOp {
    public String type(){return "Less";}
    public void accept(Visitor v) { v.visit(this); }
}
