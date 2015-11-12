package AST;

public class BreakStat extends Stat{

    public void accept(Visitor v) { v.visit(this); }
}
