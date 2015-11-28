package IR;

import java.util.ArrayList;

public class call extends IR {

    public LabelOp fun;
    public ArrayList<Operand> args;

    public call(LabelOp fun, ArrayList<Operand> args){
        this.fun = fun;
        this.args = args;
    }
    public String toString(){
        String out = "call, " + fun;
        for (Operand arg : args){
            out += (", " + arg);
        }
        return out;
    }
}
