package IR;


public class TempFloatVar extends TempVar {

    public static int num = 0;
    public int id;

    // default to int
    protected TempFloatVar(){
        isInteger = false;
        id = num++;
        name = "$f" + id;
    }

    public static TempFloatVar gen(){
        TempFloatVar var = new TempFloatVar();
        getVars().add(var);
        getNames().put(var.name, var);
        return var;
    }

}
