package AST;

import SemanticAnalyzer.SemanticSymbol;

public class TypeDec extends Node {

    public SemanticSymbol newType;

    public void accept(Visitor v) { v.visit(this); }
}
