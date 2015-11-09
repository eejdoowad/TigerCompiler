package AST;


import java.util.ArrayList;

public class ForStat extends Stat{
    public SemanticSymbol var;
    public Expr start;
    public Expr end;
    public ArrayList<Stat> stats = new ArrayList<Stat>();
    public boolean finalized = false;

    public void accept(Visitor v) { v.visit(this); }
}
