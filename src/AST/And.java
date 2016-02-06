package AST;

public class And extends LogicBinOp {
    public String type(){return "And";}
    public void accept(Visitor v) { v.visit(this); }
}
