package AST;

import java.util.ArrayList;

public class WhileStat extends Stat {

    public Expr cond;
    public ArrayList<Stat> stats = new ArrayList<Stat>();
    public boolean finalized = false;

    public void accept(Visitor v) { v.visit(this); }
}
