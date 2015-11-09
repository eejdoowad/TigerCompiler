package AST;


// ID should not appear in the final AST at all. It is
// just a temporary pushed by the parser before the
// semantic analyzer does a lookup

public class ID extends Node{

    public String name;

    public void accept(Visitor v) { v.visit(this); }
}
