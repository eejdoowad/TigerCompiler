package AST;

import java.util.ArrayList;

public class IfStat extends Stat {
    public Expr cond;
    public ArrayList<Stat> trueStats = new ArrayList<Stat>();
    public ArrayList<Stat> falseStats = null; // must explicitly init for IF-ELSE
    public boolean finalized = false; // Set to true when analysis on it is done

    public String type(){return "IfStat";}
    public void accept(Visitor v) { v.visit(this); }
    public ArrayList<Node> children(){
        ArrayList<Node> children = new ArrayList<>();
        children.add(cond);
        children.addAll(trueStats);
        if (falseStats != null)
            children.addAll(falseStats);
        return children;
    }
    public ArrayList<String> attr(){
        ArrayList<String> attr = new ArrayList<>();
        return attr;
    }
}
