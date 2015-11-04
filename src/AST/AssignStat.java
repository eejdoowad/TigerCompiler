package AST;

public class AssignStat extends Stat {

    public SemanticSymbol left; // what you assign to
    public Expr right;  // the expression on the right hand side
}
