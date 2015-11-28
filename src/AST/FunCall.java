package AST;

import SemanticAnalyzer.SemanticSymbol;

import java.util.ArrayList;

public class FunCall extends Expr {

    public SemanticSymbol func;
    public ArrayList<Expr> args = new ArrayList<>();

    public void accept(Visitor v) { v.visit(this); }
}
