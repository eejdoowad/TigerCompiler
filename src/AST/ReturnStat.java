package AST;

import SemanticAnalyzer.SemanticSymbol;

public class ReturnStat extends Stat {

    public SemanticSymbol type;
    public Expr retVal;

    public void accept(Visitor v) { v.visit(this); }
}
