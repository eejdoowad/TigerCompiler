package AST;

public class AssignStat extends Stat{

    public LValue left; // what you assign to
    public Expr right;  // the expression on the right hand side

}
