package IR;

// note that labels are also classed as instructions

import java.util.ArrayList;

public abstract class Label extends IR {

    public String name;

    // Labels don't define or use any variables
    public Var def(){
        return null;
    }
    public  ArrayList<Var> use(){
        return new ArrayList<>();
    }

    public String toString(){
        return name + ":";
    }
}
