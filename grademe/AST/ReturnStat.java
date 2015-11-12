package AST;

public class ReturnStat extends Stat {

    public Expr retVal;

    public void accept(Visitor v) { v.visit(this); }
}
