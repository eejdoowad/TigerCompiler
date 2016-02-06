package AST;

import java.util.ArrayList;

public class Param extends Node {

    public ID name;
    public ID type; // not sure about this

    public String type(){return "Param";}
    public void accept(Visitor v) { v.visit(this); }
    public ArrayList<Node> children(){
        ArrayList<Node> children = new ArrayList<>();
        children.add(name);
        children.add(type);
        return children;
    }
    public ArrayList<String> attr(){
        ArrayList<String> attr = new ArrayList<>();
        return attr;
    }
}
