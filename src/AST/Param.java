package AST;

public class Param extends Node{

    public ID name;
    public ID type; // not sure about this

    public void accept(Visitor v) { v.visit(this); }
}
