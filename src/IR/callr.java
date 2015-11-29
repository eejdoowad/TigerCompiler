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
    public String toString(){
        String out = "callr, " + retVal + ", " + fun;
        for (Operand arg : args){
            out += (", " + arg);
        }
        return out;
    }
}
