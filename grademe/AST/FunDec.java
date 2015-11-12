package AST;

import java.util.ArrayList;

public class FunDec extends Node {
    public SemanticSymbol function;
    public ArrayList<Stat> stats = new ArrayList<>();

    public void accept(Visitor v) { v.visit(this); }
}
