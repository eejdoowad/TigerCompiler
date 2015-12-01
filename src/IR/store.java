package IR;

import java.util.ArrayList;

/**
 * Created by ian on 12/1/15.
 */
public class store extends instruction {
    public Register src;
    public Var dst;

    public store(Register src, Var dst, boolean isInt) {
        this.dst = dst;
        this.src = src;
        this.isInteger = isInt;
    }

    public Var def() {
        return null;
    }

    public ArrayList<Var> use() {
        ArrayList<Var> uses = new ArrayList<>();
        uses.add(dst);
        return uses;
    }

    public String toString() {
        return "store" + (!isInteger ? "f" : "") + ", " + src.toString() + ", " + dst.toString();
    }

    public void accept(IRVisitor i) {
        i.visit(this);
    }
}
