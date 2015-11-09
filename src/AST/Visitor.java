package AST;

public interface Visitor {
    public void visit(Program n);
    public void visit(TypeDec n);
    public void visit(VarDec n);
    public void visit(FunDec n);
    public void visit(Stat n);



}
