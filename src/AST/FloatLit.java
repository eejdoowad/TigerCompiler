package AST;

public class FloatLit extends Const {

    public float val;

    public void accept(Visitor v) { v.visit(this); }
}
