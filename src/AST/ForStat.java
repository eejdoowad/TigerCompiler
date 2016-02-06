package AST;


import SemanticAnalyzer.SemanticSymbol;

import java.util.ArrayList;

public class ForStat extends Stat{
    public SemanticSymbol var;
    public Expr start;
    public Expr end;
    public ArrayList<Stat> stats = new ArrayList<>();
    public boolean finalized = false;

    public String type(){return "ForStat";}
    public void accept(Visitor v) { v.visit(this); }
    public ArrayList<Node> children(){
        ArrayList<Node> children = new ArrayList<>();
        children.add(var);
        children.add(start);
        children.add(end);
        children.addAll(stats);
        return children;
    }
    public ArrayList<String> attr(){
        ArrayList<String> attr = new ArrayList<>();
        return attr;
    }
}
