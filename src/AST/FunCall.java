package AST;

import java.util.ArrayList;

public class FunCall extends Expr {

    public SemanticSymbol func;
    public ArrayList<Expr> args = new ArrayList<Expr>();

    public void accept(Visitor v) { v.visit(this); }
}
