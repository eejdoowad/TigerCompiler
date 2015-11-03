package AST;

import java.util.ArrayList;

public class FunCall extends Node {

    public ID func;
    public ArrayList<Expr> args = new ArrayList<Expr>();

}
