package IR;


import java.util.ArrayList;

public abstract class IR {
    public abstract String toString();
    public abstract Var def();
    public abstract ArrayList<Var> use();
    public abstract void accept(IRVisitor v);
    public abstract void replaceDef(Var old, Register n);
    public abstract void replaceUses(Var old, Register n);
}
