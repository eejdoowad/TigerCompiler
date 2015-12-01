package IR;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

public abstract class Var extends Operand {
    public String name;
    private static LinkedHashSet<Var> vars = new LinkedHashSet<>();
    private static HashMap<String, Var> names = new HashMap<>();
    public static LinkedHashSet<Var> getVars(){
        return vars;
    }
    public static HashMap<String, Var> getNames(){
        return names;
    }


}
