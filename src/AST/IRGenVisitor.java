package AST;

import IR.*;
import jdk.nashorn.internal.runtime.regexp.joni.Config;

import java.util.ArrayList;


class IRGenVisitorContext {
    private Operand retVal = null;

    public void setRetVal(Operand retVal){
        if (this.retVal != null){
            System.out.println("ERROR: Attempted to set non-null retVal");
            //System.exit(1);
        }
        else{
            System.out.println("SET RETVAL");
            this.retVal = retVal;
        }
    }
    public Operand getRetVal(){
        if (retVal == null){
            System.out.println("ERROR: Attempted to get null retVal");
            //System.exit(1);
            return null; // silence error
        }
        else{
            System.out.println("GET RETVAL");
            return retVal;
        }
    }
}

public class IRGenVisitor implements Visitor {

    private boolean DEBUGIR = true;
    public void debugPrompt(String str){
        if (DEBUGIR)
            System.out.println("Visiting (" + str + ")");
    }

    // AST representation output by parser
    public Program program;
    // IROC representation output by IRGenerator
    public ArrayList<IR> instructions = new ArrayList<>();
    public void emit(IR instruction){
        instructions.add(instruction);
    }

    private IRGenVisitorContext context = new IRGenVisitorContext();

//    public Temporary generateRetVal;

    public void visit(Program n){
        debugPrompt("Program");

        for (TypeDec d : n.typeDecs){
            d.accept(this);
        }
        for (VarDec d : n.varDecs){
            d.accept(this);
        }
        for (FunDec d : n.funDecs){
            d.accept(this);
        }
        emit(new Label("main"));
        for (Stat s : n.stats){
            s.accept(this);
        }
    }
    // no action done by IR CodeGen for TypeDecs
    public void visit(TypeDec n){
        debugPrompt("TypeDec");
    }
    // generate assignments for initialized values
    public void visit(VarDec n){
        debugPrompt("VarDec");

        if (n.init != null){

            Operand right;
            if (n.type.isIntPrimitive()){
                right = new IntImmediate(((AST.IntLit)n.init).val);
            }
            else if (n.type.isFloatPrimitive()){
                right = new FloatImmediate(((AST.FloatLit)n.init).val);
            }
            else {
                System.out.println("!!!!!!!!!! WHAT ARE YOU");
                System.exit(1);
                right = new FloatImmediate(0.123456789f); // silence warning
            }

            for (SemanticSymbol var : n.vars){
                if (var.isArray()){
                    System.out.println("MISSING ARRAY_ASSIGN: FIX SEMANALYSIS");
                    //emit(new array_assign());
                }
                else {
                    NamedVar left = new NamedVar(var.getName());
                    emit(new assign(left, right));
                }
            }
        }
    }
    public void visit(FunDec n){
        debugPrompt("VarDec");

    }

    public void visit(FunCall n){
        debugPrompt("FunCall");

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
        debugPrompt("Param");

    }



    public void visit(AssignStat stat){
        debugPrompt("AssignStat");

        // First generate right expression
        stat.right.accept(this);
        Operand right = context.getRetVal();

        // Then generate l-value assignment
        if (stat.left.isArray()){
            // no index, so generate array_assign
            if (stat.index == null){
// TODO
            }
            // index, so generate array_store
            else {
                NamedVar left = new NamedVar(stat.left.getName());
                stat.index.accept(this);
                Operand index = context.getRetVal();
                emit(new array_store(left, index, right));
            }
        }
        // Normal non-array named variable, generate normal assign
        else {
            NamedVar left = new NamedVar(stat.left.getName());
            emit(new assign(left, right));
        }
    }
    public void visit(BreakStat stat){
        debugPrompt("BreakStat");

    }
    public void visit(ReturnStat stat){
        debugPrompt("ReturnStat");

    }
    public void visit(IfStat stat){
        debugPrompt("IfStat");

    }
    public void visit(ForStat stat){
        debugPrompt("ForStat");

    }
    public void visit(WhileStat stat){
        debugPrompt("WhileStat");

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
        debugPrompt("ProcedureStat");

    }

    public void visit(ID n){
        debugPrompt("ID");

        // should never get here
        System.out.println("WHY VISIT ID NODE!!!");
        System.exit(1);
    }
    public void visit(VarReference n){
        debugPrompt("VarReference");
        //
    }
    public void visit(IntLit n){
        debugPrompt("IntLit");
        context.setRetVal(new IntImmediate(n.val));
    }
    public void visit(FloatLit n){
        debugPrompt("FloatLit");
        context.setRetVal(new FloatImmediate(n.val));
    }


    public void visit(Add n){
        debugPrompt("Add");

        n.left.accept(this);
        Operand left = context.getRetVal();
        n.right.accept(this);
        Operand right = context.getRetVal();

        TempVar result = new TempVar();
        emit(new add(left, right, result));
        context.setRetVal(result);
    }
    public void visit(Sub n){
        debugPrompt("Sub");

    }
    public void visit(Mult n){
        debugPrompt("Mult");

    }
    public void visit(Div n){
        debugPrompt("Div");

    }

    public void visit(And n){
        debugPrompt("And");

    }
    public void visit(Or n){
        debugPrompt("Or");

    }

    public void visit(Eq n){
        debugPrompt("Eq");

    }
    public void visit(Neq n){
        debugPrompt("Neq");

    }
    public void visit(Greater n){
        debugPrompt("Greater");

    }
    public void visit(GreaterEq n){
        debugPrompt("GreaterEq");

    }
    public void visit(Lesser n){
        debugPrompt("Lesser");

    }
    public void visit(LesserEq n){
        debugPrompt("LesserEq");

    }
}
