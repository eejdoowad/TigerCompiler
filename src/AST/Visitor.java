package AST;

public interface Visitor {
    public void visit(Program n);
    public void visit(TypeDec n);
    public void visit(VarDec n);
    public void visit(FunDec n);

    public void visit(FunCall n);
    public void visit(Param n);



    public void visit(AssignStat stat);
    public void visit(BreakStat stat);
    public void visit(ReturnStat stat);
    public void visit(IfStat stat);
    public void visit(ForStat stat);
    public void visit(WhileStat stat);
    public void visit(ProcedureStat stat);

    public void visit(ID n);
    public void visit(VarReference n);
    public void visit(IntLit n);
    public void visit(FloatLit n);


    public void visit(Add n);
    public void visit(Sub n);
    public void visit(Mult n);
    public void visit(Div n);

    public void visit(And n);
    public void visit(Or n);

    public void visit(Eq n);
    public void visit(Neq n);
    public void visit(Greater n);
    public void visit(GreaterEq n);
    public void visit(Lesser n);
    public void visit(LesserEq n);
}
