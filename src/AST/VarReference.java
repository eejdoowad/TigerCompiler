package AST;

// ID should not appear in the final AST at all. It is
// just a temporary pushed by the parser before the
// semantic analyzer does a lookup

//  So a + b would have a plus node
// with two VarReference children

import SemanticAnalyzer.SemanticSymbol;

import java.util.ArrayList;

public class VarReference extends Expr {
    // Reference to variable symbol that this variable represents
    public SemanticSymbol reference;

    // Expression for index into if one exists
    public Expr index;

    public String type(){return "Variable";}
    public void accept(Visitor v) { v.visit(this); }
    public ArrayList<Node> children(){
        ArrayList<Node> children = new ArrayList<>();
        if (index != null)
            children.add(index);
        return children;
    }
    public ArrayList<String> attr(){
        ArrayList<String> attr = new ArrayList<>();
        attr.add(reference.getName());
        return attr;
    }
}
