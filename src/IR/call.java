package IR;

import java.util.ArrayList;

public class call extends callInstruction {

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

    public void replaceDef(Var old, Register n){
        System.out.println("ERROR call.replaceDef()");
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
        String out = "call, " + fun;
        for (Operand arg : args){
            out += (", " + arg);
        }
        return out;
    }

    public void accept(IRVisitor v) { v.visit(this); }
}
