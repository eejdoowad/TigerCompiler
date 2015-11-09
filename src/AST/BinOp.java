package AST;

public abstract class BinOp extends Expr {

    public Expr left;
    public Expr right;
    public boolean convertLeft; // true if left is converted to right's type. false otherwise
}
