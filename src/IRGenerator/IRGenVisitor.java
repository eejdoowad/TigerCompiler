package IRGenerator;

import Config.Config;
import AST.*;
import IR.*;
import SemanticAnalyzer.SemanticSymbol;

import java.util.ArrayList;


public class IRGenVisitor implements Visitor {

    public void debugPrompt(String str){
        if (Config.DEBUG_IRCODEGEN)
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
        for (FunDec d : n.funDecs){
            d.accept(this);
        }
        emit(new UniqueLabel("main"));
        for (VarDec d : n.varDecs){
            d.accept(this);
        }
        for (Stat s : n.stats){
            s.accept(this);
        }
//        emit(new UniqueLabel("initialization"));
//        for (VarDec d : n.varDecs){
//            d.accept(this);
//        }
//        emit(new UniqueLabel("main"));
//        for (Stat s : n.stats){
//            s.accept(this);
//        }
        emit(new ret(null));
    }
    // no action done by IR CodeGen for TypeDecs
    public void visit(TypeDec n){
        debugPrompt("TypeDec");
    }
    // generate assignments for initialized values
    public void visit(VarDec n){
        debugPrompt("VarDec");

        if (n.init != null){

            n.init.accept(this);
            Operand right = context.getRetVal();

            for (SemanticSymbol var : n.vars){
                NamedVar left = new NamedVar(var.getName());
                if (var.isArray()){
                    IntImmediate arraySize = new IntImmediate(var.getArraySize());
                    emit(new array_assign(left, arraySize, right));
                }
                else {
                    emit(new assign(left, right));
                }
            }
        }
    }
    public void visit(FunDec n){
        debugPrompt("VarDec");

        Label funLabel = new UniqueLabel(n.function.getName());
        emit(funLabel);

        for (Stat s : n.stats){
            s.accept(this);
        }

        if (n.function.getFunctionReturnType() == null){
            emit(new ret(null));
        }
    }

    public void visit(FunCall n){
        debugPrompt("FunCall");

        ArrayList<Operand> args = new ArrayList<>();

        for (Expr arg : n.args){
            arg.accept(this);
            args.add(context.getRetVal());
        }

        LabelOp fun = new LabelOp(new UniqueLabel(n.func.getName()));

        // Check for return value
        if (n.type == null){
            emit(new call(fun, args));
        }
        else {
            TempVar t = new TempVar();
            emit(new callr(fun, t, args));
            context.setRetVal(t);
        }
    }

    public void visit(Param n){
        debugPrompt("Param");

        System.out.println("WHY U HERE PARAM!");
        System.exit(1);
        // GOOD FOR NOTHING
    }



    public void visit(AssignStat stat){
        debugPrompt("AssignStat");

        NamedVar left = new NamedVar(stat.left.getName());
        stat.right.accept(this);
        Operand right = context.getRetVal();

        // Generate correct type of assignment/store
        if (stat.left.isArray()){
            // no index, so generate array_assign
            if (stat.index == null){
                IntImmediate arrSize = new IntImmediate(stat.left.getArraySize());
                emit(new array_assign(left, arrSize, right));
            }
            // index, so generate array_store
            else {
                stat.index.accept(this);
                Operand index = context.getRetVal();
                emit(new array_store(left, index, right));
            }
        }
        // Normal non-array named variable, generate normal assign
        else {
            IR last = instructions.get(instructions.size()-1);
            // If the last IR is a binop whose result will be assigned, we can do a little optimization
            if (last instanceof binop && ((binop)last).result == right) {
                ((binop)last).result = left;
            } else {
                emit(new assign(left, right));
            }
        }
    }
    public void visit(BreakStat stat){
        debugPrompt("BreakStat");
        LabelOp breakLabelOp = new LabelOp(context.breakLabels.peek());
        emit(new goTo(breakLabelOp));
    }
    public void visit(ReturnStat stat){
        debugPrompt("ReturnStat");

        stat.retVal.accept(this);
        Operand retVal = context.getRetVal();

        emit(new ret(retVal));
    }
    public void visit(IfStat stat){
        debugPrompt("IfStat");

        SharedLabel ifFalse = new SharedLabel("if_false");

        context.setFalseLabel(ifFalse);
        stat.cond.accept(this);

        for (Stat s : stat.trueStats){
            s.accept(this);
        }

        // Not always emitted. only used if there is an else block
        SharedLabel afterElse = new SharedLabel("after_else");

        // There is an else block so skip it
        if (stat.falseStats != null){
            emit(new goTo(new LabelOp(afterElse)));
        }

        emit(ifFalse);

        // There is an else block
        if (stat.falseStats != null){
            for (Stat s : stat.falseStats){
                s.accept(this);
            }
            emit(afterElse);
        }
    }
    public void visit(ForStat stat){
        debugPrompt("ForStat");

        SharedLabel before = new SharedLabel("before_for");
        SharedLabel after = new SharedLabel("after_for");

        stat.start.accept(this);
        Operand startIndex = context.getRetVal();
        stat.end.accept(this);
        Operand endIndex = context.getRetVal();

        NamedVar loopVar = new NamedVar(stat.var.getName());

        emit(new assign(loopVar, startIndex));
        emit(before);
        emit(new brgeq(loopVar, endIndex, new LabelOp(after)));

        context.breakLabels.push(after);
        for (Stat s : stat.stats){
            s.accept(this);
        }
        context.breakLabels.pop();

        emit(new add(loopVar, loopVar, new IntImmediate(1)));
        emit(new goTo(new LabelOp(before)));
        emit(after);
    }
    public void visit(WhileStat stat){
        debugPrompt("WhileStat");

        SharedLabel before = new SharedLabel("before_while");
        SharedLabel after = new SharedLabel("after_while");
        emit(before);

        context.setFalseLabel(after);
        stat.cond.accept(this);

        context.breakLabels.push(after);
        for (Stat s : stat.stats){
            s.accept(this);
        }
        context.breakLabels.pop();

        emit(new goTo(new LabelOp(before)));
        emit(after);
    }
    public void visit(ProcedureStat stat){
        debugPrompt("ProcedureStat");

        stat.funCall.accept(this);
        if (stat.funCall.func.getFunctionReturnType() != null){
            context.getRetVal(); // discard return value
        }
    }

    public void visit(ID n){
        debugPrompt("ID");

        // should never get here
        System.out.println("WHY VISIT ID NODE!!!");
        System.exit(1);
    }
    public void visit(VarReference n){
        debugPrompt("VarReference");
        // normal non-array access
        if (n.index == null){
            context.setRetVal(new NamedVar(n.reference.getName()));
        }
        else {
            n.index.accept(this);
            Operand index = context.getRetVal();
            TempVar left = new TempVar();
            NamedVar array = new NamedVar(n.reference.getName());
            emit(new array_load(left, array, index));
            context.setRetVal(left);
        }
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

        n.left.accept(this);
        Operand left = context.getRetVal();
        n.right.accept(this);
        Operand right = context.getRetVal();

        TempVar result = new TempVar();
        emit(new sub(left, right, result));
        context.setRetVal(result);
    }
    public void visit(Mult n){
        debugPrompt("Mult");

        n.left.accept(this);
        Operand left = context.getRetVal();
        n.right.accept(this);
        Operand right = context.getRetVal();

        TempVar result = new TempVar();
        emit(new mult(left, right, result));
        context.setRetVal(result);
    }
    public void visit(Div n){
        debugPrompt("Div");

        n.left.accept(this);
        Operand left = context.getRetVal();
        n.right.accept(this);
        Operand right = context.getRetVal();

        TempVar result = new TempVar();
        emit(new div(left, right, result));
        context.setRetVal(result);
    }

    public void visit(And n){
        debugPrompt("And");

        n.left.accept(this);
        Operand left = context.getRetVal();
        n.right.accept(this);
        Operand right = context.getRetVal();

        TempVar result = new TempVar();
        emit(new and(left, right, result));
        context.setRetVal(result);
    }
    public void visit(Or n){
        debugPrompt("Or");

        n.left.accept(this);
        Operand left = context.getRetVal();
        n.right.accept(this);
        Operand right = context.getRetVal();

        TempVar result = new TempVar();
        emit(new or(left, right, result));
        context.setRetVal(result);
    }

    public void visit(Eq n){
        debugPrompt("Eq");

        Label falseLabel = context.getFalseLabel();

        n.left.accept(this);
        Operand left = context.getRetVal();
        n.right.accept(this);
        Operand right = context.getRetVal();

        emit(new brneq(left, right, new LabelOp(falseLabel)));

    }
    public void visit(Neq n){
        debugPrompt("Neq");

        Label falseLabel = context.getFalseLabel();

        n.left.accept(this);
        Operand left = context.getRetVal();
        n.right.accept(this);
        Operand right = context.getRetVal();

        emit(new breq(left, right, new LabelOp(falseLabel)));
    }
    public void visit(Greater n){
        debugPrompt("Greater");

        Label falseLabel = context.getFalseLabel();

        n.left.accept(this);
        Operand left = context.getRetVal();
        n.right.accept(this);
        Operand right = context.getRetVal();

        emit(new brleq(left, right, new LabelOp(falseLabel)));
    }
    public void visit(GreaterEq n){
        debugPrompt("GreaterEq");

        Label falseLabel = context.getFalseLabel();

        n.left.accept(this);
        Operand left = context.getRetVal();
        n.right.accept(this);
        Operand right = context.getRetVal();

        emit(new brlt(left, right, new LabelOp(falseLabel)));
    }
    public void visit(Lesser n){
        debugPrompt("Lesser");

        Label falseLabel = context.getFalseLabel();

        n.left.accept(this);
        Operand left = context.getRetVal();
        n.right.accept(this);
        Operand right = context.getRetVal();

        emit(new brgeq(left, right, new LabelOp(falseLabel)));
    }
    public void visit(LesserEq n){
        debugPrompt("LesserEq");

        Label falseLabel = context.getFalseLabel();

        n.left.accept(this);
        Operand left = context.getRetVal();
        n.right.accept(this);
        Operand right = context.getRetVal();

        emit(new brgt(left, right, new LabelOp(falseLabel)));
    }
}

