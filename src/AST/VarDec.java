package AST;

import SemanticAnalyzer.SemanticSymbol;

import java.util.ArrayList;

public class VarDec extends Node {

    public ArrayList<SemanticSymbol> vars = new ArrayList<>();
    public SemanticSymbol type;
    public Const init;

    public String type(){return "VarDec";}
    public void accept(Visitor v) { v.visit(this); }
    public ArrayList<Node> children(){
        ArrayList<Node> children = new ArrayList<>();
        children.addAll(vars);
        children.add(type);
        children.add(init);
        return children;
    }
    public ArrayList<String> attr(){
        ArrayList<String> attr = new ArrayList<>();
        return attr;
    }
}
