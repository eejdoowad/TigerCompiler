package AST;

public class IntLit extends Const {
    public int val;

    public void accept(Visitor v) { v.visit(this); }
}
