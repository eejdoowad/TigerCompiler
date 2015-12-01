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
        if (dst instanceof Var) return (Var)dst;
        else return null;
    }

    public ArrayList<Var> use() {
        ArrayList<Var> uses = new ArrayList<>();

        // TODO: Figure out how to add this to data memory
        return uses;
    }

    public String toString() {
        return "movfi, " + src + ", " + dst;
    }

    public void accept(IRVisitor v) {
        v.visit(this);
    }
}
