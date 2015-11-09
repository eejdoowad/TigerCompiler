package AST;

import IR.*;

import java.util.ArrayList;

public class IRGenVisitor implements Visitor {

    // AST representation output by parser
    public Program program;
    // IROC representation output by IRGenerator
    public ArrayList<IR> instructions = new ArrayList<>();
    public void emit(IR instruction){
        instructions.add(instruction);
    }

//    public Temporary generateRetVal;

    public void visit(Program n){
        for (TypeDec d : n.typeDecs){
            d.accept(this);
        }
        for (VarDec d : n.varDecs){
            d.accept(this);
        }
        for (FunDec d : n.funDecs){
            d.accept(this);
        }
        for (Stat s : n.stats){
            s.accept(this);
        }
    }
    // no action done by IR CodeGen for TypeDecs
    public void visit(TypeDec n){

    }
    // generate assignments for initialized values
    public void visit(VarDec n){
        if (n.init != null){
            for (SemanticSymbol var : n.vars){
                if (var.isArray()){
//                    emit(new array_assign());
                }
                else {
//                    emit(new assign());
                }
            }
        }
    }
    public void visit(FunDec n){

    }

    public void visit(FunCall n){
//    x = f(expr1, expr2, ...);
// becomes
//    a = generate(f)
//    foreach expr-i
//    ti = generate(expri)
//    emit( push ti )
//
//    emit( call_jump a )
//    emit( x = get_result )

    }
    public void visit(Param n){

    }



    public void visit(AssignStat stat){

    }
    public void visit(BreakStat stat){

    }
    public void visit(ReturnStat stat){

    }
    public void visit(IfStat stat){

    }
    public void visit(ForStat stat){

    }
    public void visit(WhileStat stat){
//    while (expr)
//      statement;
// becomes
//    E = new_label()
//    T = new_label()
//    emit( T: )
//    t = generate(expr)
//    emit( ifnot_goto t, E )
//    generate(statement)
//    emit( goto T )
//    emit( E: )

        Label before = new Label("before_while");
        Label after = new Label("after_while");
        emit(before);
        // handle generating condition branch
        // may have to modify accept method
        // that or pass as global variables
        // if global, consider set and get methods
        // where the get
        // 1. checks if null reference (error)
        // 2. sets to null reference on completion
        stat.cond.accept(this);
        // handle generating while body statements
        for (Stat s : stat.stats){
            s.accept(this);
        }
        emit(new goTo(before));
        emit(after);
    }
    public void visit(ProcedureStat stat){

    }

    public void visit(ID n){
        // should never get here
    }
    public void visit(VarReference n){
        //
    }
    public void visit(IntLit n){

    }
    public void visit(FloatLit n){

    }


    public void visit(Add n){

    }
    public void visit(Sub n){

    }
    public void visit(Mult n){

    }
    public void visit(Div n){

    }

    public void visit(And n){

    }
    public void visit(Or n){

    }

    public void visit(Eq n){

    }
    public void visit(Neq n){

    }
    public void visit(Greater n){

    }
    public void visit(GreaterEq n){

    }
    public void visit(Lesser n){

    }
    public void visit(LesserEq n){

    }
}
