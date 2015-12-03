package IR;

import java.util.ArrayList;


public class store extends instruction {
    public Register src;
    public Var dst;

    public store(Register src, Var dst, boolean isInteger) {
        this.dst = dst;
        this.src = src;
        this.isInteger = isInteger;
    }

    public Var def() {
        return null;
    }

    public ArrayList<Var> use() {
        ArrayList<Var> uses = new ArrayList<>();
        uses.add(dst);
        return uses;
    }

    // ACTUALLY THESE ARE STUPID AND SHOULD NEVER BE REACHED
    public void replaceDef(Var old, Register n){
        System.out.println("ERROR store.replaceDef()");
    }
    public void replaceUses(Var old, Register n){
       System.out.println("ERROR store.replaceDef store");
    }


    public String toString() {
        return "store" + (!isInteger ? "f" : "") + ", " + src.toString() + ", " + dst.toString();
    }

    public void accept(IRVisitor i) {
        i.visit(this);
    }
}
