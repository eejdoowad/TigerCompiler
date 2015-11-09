package AST;

public class AssignStat extends Stat {

    public SemanticSymbol left; // what you assign to
    public Expr index; // array index if applicable
    public Expr right;  // the expression on the right hand side

    public void accept(Visitor v) { v.visit(this); }

}
