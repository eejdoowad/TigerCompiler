package AST;

import java.util.ArrayList;

public class IntLit extends Const {
    public int val;

    public String type(){return "IntLit";}
    public void accept(Visitor v) { v.visit(this); }
    public ArrayList<Node> children(){
        ArrayList<Node> children = new ArrayList<>();
        return children;
    }
    public ArrayList<String> attr(){
        ArrayList<String> attr = new ArrayList<>();
        attr.add("" + val);
        return attr;
    }
}
