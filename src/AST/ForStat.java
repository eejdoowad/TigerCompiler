package AST;


import java.util.ArrayList;

public class ForStat extends Stat{
    public ID loopvar;
    public Expr start;
    public Expr end;
    public ArrayList<Stat> stats = new ArrayList<Stat>();
}
