package AST;

public class ProcedureStat extends Stat {

    public FunCall funCall;

    public void accept(Visitor v) { v.visit(this); }
}
