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

    public static TempIntVar gen(boolean inFunction){
        TempIntVar var = new TempIntVar();
        var.isLocal = inFunction;
        getVars().add(var);
        getNames().put(var.name, var);
        return var;
    }

}
