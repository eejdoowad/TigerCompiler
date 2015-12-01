package IR;

import SemanticAnalyzer.SemanticSymbol;

public class TempIntVar extends TempVar {

    public static int num = 0;
    public int id;

    // default to int
    protected TempIntVar(){
        isInteger = true;
        id = num++;
        name = "__t" + id;
    }

    public static TempIntVar gen(){
        TempIntVar var = new TempIntVar();
        getVars().add(var);
        getNames().put(var.name, var);
        return var;
    }

}
