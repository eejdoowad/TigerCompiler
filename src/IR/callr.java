package IR;

import java.util.ArrayList;

public class callr extends jumpLabel {

    public LabelOp fun;
    public Operand retVal;
    public ArrayList<Operand> args;

    public callr(LabelOp fun, Operand retVal, ArrayList<Operand> args){
        this.fun = fun;
        this.retVal = retVal;
        this.args = args;
    }

    public Var def(){
        if (retVal instanceof Var) return (Var)retVal;
        else return null;
    }

    public ArrayList<Var> use(){
        ArrayList<Var> uses = new ArrayList<>();
        for (Operand arg : args){
            if (arg instanceof Var){
                uses.add((Var)arg);
            }
        }
        return uses;
    }

    public String toString(){
        String out = "callr, " + retVal + ", " + fun;
        for (Operand arg : args){
            out += (", " + arg);
        }
        return out;
    }
}
