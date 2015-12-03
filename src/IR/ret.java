package IR;

import java.util.ArrayList;

public class ret extends jump {

    public Operand retVal;

    public ret(Operand retVal){
        this.isInteger = (retVal == null ? true : retVal.isInteger);
        this.retVal = retVal;

    }

    public Var def(){
        return null;
    }

    public ArrayList<Var> use(){
        ArrayList<Var> uses = new ArrayList<>();
        if (retVal instanceof Var) uses.add((Var)retVal);
        return uses;
    }

    public void replaceDef(Var old, Register n){
        System.out.println("ERROR ret.replaceDef()");
    }
    public void replaceUses(Var old, Register n){
        if (retVal == old) retVal = n;
    }

    public String toString(){
        return  "return, " + (retVal == null ? "" : retVal) + ", ,";
    }
    public void accept(IRVisitor v) { v.visit(this); }
}
