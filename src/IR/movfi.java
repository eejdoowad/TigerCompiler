package IR;

import java.util.ArrayList;

/**
 * Created by ian on 12/1/15.
 */
public class movfi extends instruction {
    public FloatImmediate src;
    public Operand dst;

    public movfi(FloatImmediate src, Var dst) {
        this.src = src;
        this.dst = dst;
        this.isInteger = false;
    }

    public Var def() {
        return null;
    }

    public ArrayList<Var> use() {
        ArrayList<Var> uses = new ArrayList<>();
        if (dst instanceof Var) {
            uses.add((Var)dst);
        }
        return uses;
    }

    public String toString() {
        return "movfi, " + src + ", " + dst;
    }

    public void accept(IRVisitor v) {
        v.visit(this);
    }
}
