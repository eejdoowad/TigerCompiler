package IR;

import java.util.ArrayList;

public class call extends jumpLabel {

    public LabelOp fun;
    public ArrayList<Operand> args;

    public call(LabelOp fun, ArrayList<Operand> args){
        this.fun = fun;
        this.args = args;
    }

    public Var def(){
        return null;
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
        String out = "call, " + fun;
        for (Operand arg : args){
            out += (", " + arg);
        }
        return out;
    }
}
