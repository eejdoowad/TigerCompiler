package AST;

import SemanticAnalyzer.SemanticSymbol;

import java.util.ArrayList;

public class FunCall extends Expr {

    public SemanticSymbol func;
    public ArrayList<Expr> args = new ArrayList<>();

    public String type(){return "FunCall";}
    public void accept(Visitor v) { v.visit(this); }
    public ArrayList<Node> children(){
        ArrayList<Node> children = new ArrayList<>();
        children.add(func);
        children.addAll(args);
        return children;
    }
    public ArrayList<String> attr(){
        ArrayList<String> attr = new ArrayList<>();
        return attr;
    }
}
