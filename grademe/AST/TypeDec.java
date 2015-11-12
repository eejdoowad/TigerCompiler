package AST;

public class TypeDec extends Node {

    public SemanticSymbol newType;

    public void accept(Visitor v) { v.visit(this); }
}
