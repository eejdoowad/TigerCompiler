package AST;

import SemanticAnalyzer.SemanticSymbol;

import java.util.ArrayList;

public class AssignStat extends Stat {

    public SemanticSymbol left; // what you assign to
    public Expr index; // array index if applicable
    public Expr right;  // the expression on the right hand side

    public String type(){return "AssignStat";}
    public void accept(Visitor v) { v.visit(this); }
    public ArrayList<Node> children(){
        ArrayList<Node> children = new ArrayList<>();
        children.add(left);
        if (index != null)
            children.add(index);
        children.add(right);
        return children;
    }
    public ArrayList<String> attr(){
        ArrayList<String> attr = new ArrayList<>();
        return attr;
    }

}
