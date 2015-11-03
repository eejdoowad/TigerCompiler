package AST;

import java.util.ArrayList;

public class IfStat extends Stat {

    public Expr cond;
    public ArrayList<Stat> trueStats = new ArrayList<Stat>();
    public ArrayList<Stat> falseStats = null; // must explicitly init for IF-ELSE

}
