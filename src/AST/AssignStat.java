package AST;

public class AssignStat extends Stat{

    LValue left; // what you assign to
    Expr right;  // the expression on the right hand side

}
