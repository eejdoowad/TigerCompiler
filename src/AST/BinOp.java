package AST;

import java.util.ArrayList;

public abstract class BinOp extends Expr {

    public Expr left;
    public Expr right;
    public boolean convertLeft; // true if left is converted to right's type. false otherwise
    public ArrayList<Node> children(){
        ArrayList<Node> children = new ArrayList<>();
        children.add(left);
        children.add(right);
        return children;
    }
    public ArrayList<String> attr(){
        ArrayList<String> attr = new ArrayList<>();
        return attr;
    }
}
