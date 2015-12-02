package IR;

import java.util.ArrayList;

// Just an invisible marker marking the end of a function so that
// the epilogue can be inserted
public class FunctionEpilogue extends instruction {
    public Var def() {
        return null;
    }

    public ArrayList<Var> use() {
        return new ArrayList<>();
    }

    public void replaceDef(Var old, Register n){
        System.out.println("ERROR NO SUPPORT functionEpilogue.replaceDef()");
    }
    public void replaceUses(Var old, Register n){
        System.out.println("ERROR NO SUPPORT FunctionEpilogue.replaceDef()");
    }


    public void accept(IRVisitor v) {
        v.visit(this);
    }

    public String toString() {
        return "";
    }
}
