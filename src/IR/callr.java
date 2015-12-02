package IR;

import java.util.ArrayList;

public class callr extends callInstruction {

    public Operand retVal;

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

    public void replaceDef(Var old, Register n){
        if (retVal == old) retVal = n;
        else System.out.println("ERROR: callr.replaceDef()");
    }
    public void replaceUses(Var old, Register n){
        for (int i = 0; i < args.size(); i++){
            if (args.get(i) == old){
                args.remove(i);
                args.add(i, n);
            }
        }
    }

    public String toString(){
        String out = "callr, " + retVal + ", " + fun;
        for (Operand arg : args){
            out += (", " + arg);
        }
        return out;
    }

    public void accept(IRVisitor v) { v.visit(this); }
}
