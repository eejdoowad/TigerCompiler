package AST;

public interface StringVisitor {
    public String visit(ASTRoot n);
    public String visit(TypeDec n);
    public String visit(VarDec n);
    public String visit(FunDec n);

    public String visit(FunCall n);
    public String visit(Param n);



    public String visit(AssignStat stat);
    public String visit(BreakStat stat);
    public String visit(ReturnStat stat);
    public String visit(IfStat stat);
    public String visit(ForStat stat);
    public String visit(WhileStat stat);
    public String visit(ProcedureStat stat);

    public String visit(ID n);
    public String visit(VarReference n);
    public String visit(IntLit n);
    public String visit(FloatLit n);


    public String visit(Add n);
    public String visit(Sub n);
    public String visit(Mult n);
    public String visit(Div n);

    public String visit(And n);
    public String visit(Or n);

    public String visit(Eq n);
    public String visit(Neq n);
    public String visit(Greater n);
    public String visit(GreaterEq n);
    public String visit(Lesser n);
    public String visit(LesserEq n);
}
