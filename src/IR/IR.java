package IR;


import java.util.ArrayList;

public abstract class IR {
    public abstract String toString();
    public abstract Var def();
    public abstract ArrayList<Var> use();
    public abstract void accept(IRVisitor v);
}
