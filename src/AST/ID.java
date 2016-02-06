package AST;


// ID should not appear in the final AST at all. It is
// just a temporary pushed by the parser before the
// semantic analyzer does a lookup

import java.util.ArrayList;

public class ID extends Node{

    public String name;

    public String type(){return "ID";}
    public void accept(Visitor v) { v.visit(this); }
    public ArrayList<Node> children(){
        ArrayList<Node> children = new ArrayList<>();
        return children;
    }
    public ArrayList<String> attr(){
        ArrayList<String> attr = new ArrayList<>();
        attr.add("" + name);
        return attr;
    }
}
