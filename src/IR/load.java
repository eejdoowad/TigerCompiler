package IR;

import java.util.ArrayList;

/**
 * Created by ian on 12/1/15.
 */
public class load extends instruction {
    public Register dst;
    public Var src;

    public load(Register dst, Var src, boolean isInt) {
        this.dst = dst;
        this.src = src;
        this.isInteger = isInt;
    }

    public Var def() {
        return null;
    }

    public ArrayList<Var> use() {
        ArrayList<Var> uses = new ArrayList<>();
        uses.add(src);
        return uses;
    }

    // ACTUALLY THESE ARE STUPID AND SHOULD NEVER BE REACHED
    public void replaceDef(Var old, Register n){
        System.out.println("ERROR load.replaceDef() ");
    }
    public void replaceUses(Var old, Register n){
        System.out.println("ERROR load.replaceDef");
    }

    public String toString() {
        return "load" + (!isInteger ? "f" : "") + ", " + dst.toString() + ", " + src.toString();
    }

    public void accept(IRVisitor i) {
        i.visit(this);
    }
}
