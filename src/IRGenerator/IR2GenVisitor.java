package IRGenerator;

import Config.Config;
import AST.*;
import IR.*;
import SemanticAnalyzer.SemanticSymbol;

import java.util.ArrayList;


public class IR2GenVisitor implements Visitor {

    private ASTRoot ast;
    private ArrayList<IR> instructions = new ArrayList<>();
    private IRGenVisitorContext context = new IRGenVisitorContext();
    private boolean inFunction = false;

    public IR2GenVisitor(ASTRoot ast){
        this.ast = ast;
    }

    public ArrayList<IR> generateIR(){
        ast.accept(this);
        return instructions;
    }

    private void debugPrompt(String str){
        if (Config.DEBUG_IRCODEGEN)
            System.out.println("Visiting (" + str + ")");
    }

    public static boolean intResult(Operand l, Operand r){
        if (l.isInt() && r.isInt())
            return true;
        return false;
    }

    private TempFloatVar genIntToFloat(Operand op){
        TempFloatVar dest = TempFloatVar.gen(inFunction);
        emit(new intToFloat(op, dest));
        return dest;
    }

    public void emit(IR instruction){
        instructions.add(instruction);
    }

    public void visit(AST.ASTRoot n){
        debugPrompt("Program");

        for (TypeDec d : n.typeDecs){
            d.accept(this);
        }
        for (FunDec d : n.funDecs){
            d.accept(this);
        }
        emit(FunctionLabel.generate("main"));
        for (VarDec d : n.varDecs){
            d.accept(this);
        }
        for (Stat s : n.stats){
            s.accept(this);
        }

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
                NamedVar left = NamedVar.generateNamedVar(var);
                if (var.isArray()){
                    IntImmediate arraySize = new IntImmediate(var.getArraySize());
                    emit(new array_assign(left, arraySize, right, left.isInt()));
                    TempIntVar temp = TempIntVar.gen(inFunction);
                    SharedLabel label = new SharedLabel(left.name + "_assign");
                    if (right.isInt() && !left.isInt()) {
                        TempFloatVar conv = TempFloatVar.gen(inFunction);
                        emit(new intToFloat(right, conv));
                        right = conv;
                    }
                    emit(new assign(temp, new IntImmediate(0), true));
                    emit(label);
                    emit(new array_store(left, temp, right, left.isInt()));
                    emit(new add(temp, new IntImmediate(1), temp, true));
                    emit(new brneq(temp, arraySize, new LabelOp(label), true));
                }
                else {
                    emit(new assign(left, right, left.isInt()));
                }
            }
        }
    }
    public void visit(FunDec n){
        debugPrompt("FunDec");

        FunctionPrologue prologue = (FunctionPrologue) FunctionPrologue.generate("_" + n.function.getName());
        for (SemanticSymbol s : n.function.getFunctionParameters()) {
            NamedVar var = NamedVar.generateNamedVar(s);
            var.isLocal = true;
            prologue.arguments.add(var);
        }
        emit(prologue);

        inFunction = true;
        for (Stat s : n.stats){
            s.accept(this);
        }

        inFunction = false;

        Label epilogueLabel = new SharedLabel("_" + n.function.getName() + "_epilogue");
        prologue.epilogueLabel = (SharedLabel)epilogueLabel;
        emit(epilogueLabel);
        emit(new FunctionEpilogue());
    }

    public void visit(FunCall n){
        debugPrompt("FunCall");

        ArrayList<Operand> args = new ArrayList<>();

        for (Expr arg : n.args){
            arg.accept(this);
            args.add(context.getRetVal());
        }

        LabelOp fun = new LabelOp(FunctionLabel.generate("_" + n.func.getName()));

        // Check for return value
        if (n.type == null){
            emit(new call(fun, args));
        }
        else {
            TempVar t = TempVar.gen(n.type.getInferredPrimitive(), inFunction);
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

        NamedVar left = NamedVar.generateNamedVar(stat.left);
        stat.right.accept(this);
        Operand right = context.getRetVal();

        // Generate correct type of assignment/store
        if (stat.left.isArray()){
            // no index, so generate array_assign
            if (stat.index == null){
                IntImmediate arrSize = new IntImmediate(stat.left.getArraySize());
                emit(new array_assign(left, arrSize, right, left.isInt()));
            }
            // index, so generate array_store
            else {
                stat.index.accept(this);
                Operand index = context.getRetVal();
                emit(new array_store(left, index, right, left.isInt()));
            }
        }
        // Normal non-array named variable, generate normal assign
        else {
            if (stat.left.isFloatPrimitive() && right.isInt()) {
                if (right instanceof IntImmediate) {
                    TempIntVar t = TempIntVar.gen(inFunction);
                    emit(new assign(t, right, true));
                    right = t;
                }
                TempFloatVar temp = TempFloatVar.gen(inFunction);
                emit(new intToFloat(right, temp));
                right = temp;
            }
            emit(new assign(left, right, stat.left.isIntPrimitive()));
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
        if (retVal.isInt() && stat.type.isFloatPrimitive()) {
            if (retVal instanceof IntImmediate) {
                TempIntVar t = TempIntVar.gen(inFunction);
                emit(new assign(t, retVal, true));
                retVal = t;
            }
            TempFloatVar temp = TempFloatVar.gen(inFunction);
            emit(new intToFloat(retVal, temp));
            retVal = temp;
        }

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

        NamedVar loopVar = NamedVar.generateNamedVar(stat.var);

        emit(new assign(loopVar, startIndex, loopVar.isInt()));
        emit(before);
        emit(new brgeq(loopVar, endIndex, new LabelOp(after), loopVar.isInt()));

        context.breakLabels.push(after);
        for (Stat s : stat.stats){
            s.accept(this);
        }
        context.breakLabels.pop();

        emit(new add(loopVar, new IntImmediate(1), loopVar, loopVar.isInt()));
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
            context.setRetVal(NamedVar.generateNamedVar(n.reference));
        }
        else {
            n.index.accept(this);
            Operand index = context.getRetVal();
            TempVar left = TempVar.gen(n.reference.getInferredPrimitive(), inFunction);
            NamedVar array = NamedVar.generateNamedVar(n.reference);
            emit(new array_load(left, array, index, left.isInt()));
            context.setRetVal(left);
        }
    }
    public void visit(IntLit n){
        debugPrompt("IntLit");
        context.setRetVal(new IntImmediate(n.val));
    }
    public void visit(FloatLit n){
        debugPrompt("FloatLit");
        TempFloatVar dst = TempFloatVar.gen(inFunction);
        emit(new movfi(new FloatImmediate(n.val), dst));
        context.setRetVal(dst);
    }

    // passed as one element array to sidestep pass by value
    // want to modify what left and right point to
    public TempVar processBinOp(BinOp n, Operand[] left, Operand[] right){
        n.left.accept(this);
        left[0] = context.getRetVal();
        n.right.accept(this);
        right[0] = context.getRetVal();

        if (!right[0].isInt() && left[0].isInt()){
            left[0] = genIntToFloat(left[0]);
        }
        if (!left[0].isInt() && right[0].isInt()){
            right[0] = genIntToFloat(right[0]);
        }
        return TempVar.gen(left[0], right[0], inFunction);
    }

    public void visit(Add n){
        debugPrompt("Add");

        Operand[] left = new Operand[1], right = new Operand[1];
        TempVar result = processBinOp(n, left, right);

        emit(new add(left[0], right[0], result, intResult(left[0], right[0])));

        context.setRetVal(result);
    }
    public void visit(Sub n){
        debugPrompt("Sub");

        Operand[] left = new Operand[1], right = new Operand[1];
        TempVar result = processBinOp(n, left, right);

        emit(new sub(left[0], right[0], result, intResult(left[0], right[0])));
        context.setRetVal(result);
    }
    public void visit(Mult n){
        debugPrompt("Mult");

        Operand[] left = new Operand[1], right = new Operand[1];
        TempVar result = processBinOp(n, left, right);

        emit(new mult(left[0], right[0], result, intResult(left[0], right[0])));
        context.setRetVal(result);
    }
    public void visit(Div n){
        debugPrompt("Div");

        Operand[] left = new Operand[1], right = new Operand[1];
        TempVar result = processBinOp(n, left, right);

        emit(new div(left[0], right[0], result, intResult(left[0], right[0])));
        context.setRetVal(result);
    }

    public void visit(And n){
        debugPrompt("And");

        Operand[] left = new Operand[1], right = new Operand[1];
        TempVar result = processBinOp(n, left, right);

        emit(new and(left[0], right[0], result));
        context.setRetVal(result);
    }
    public void visit(Or n){
        debugPrompt("Or");

        Operand[] left = new Operand[1], right = new Operand[1];
        TempVar result = processBinOp(n, left, right);

        emit(new or(left[0], right[0], result));
        context.setRetVal(result);
    }

    public void processCompBinOp(BinOp n, Operand[] left, Operand[] right){
        n.left.accept(this);
        left[0] = context.getRetVal();
        n.right.accept(this);
        right[0] = context.getRetVal();

        if (!right[0].isInt() && left[0].isInt()){
            left[0] = genIntToFloat(left[0]);
        }
        if (!left[0].isInt() && right[0].isInt()){
            right[0] = genIntToFloat(right[0]);
        }
    }

    public void visit(Eq n){
        debugPrompt("Eq");

        Label falseLabel = context.getFalseLabel();

        Operand[] left = new Operand[1], right = new Operand[1];
        processCompBinOp(n, left, right);

        emit(new brneq(left[0], right[0], new LabelOp(falseLabel), intResult(left[0], right[0])));

    }
    public void visit(Neq n){
        debugPrompt("Neq");

        Label falseLabel = context.getFalseLabel();

        Operand[] left = new Operand[1], right = new Operand[1];
        processCompBinOp(n, left, right);

        emit(new breq(left[0], right[0], new LabelOp(falseLabel), intResult(left[0], right[0])));
    }
    public void visit(Greater n){
        debugPrompt("Greater");

        Label falseLabel = context.getFalseLabel();

        Operand[] left = new Operand[1], right = new Operand[1];
        processCompBinOp(n, left, right);

        emit(new brleq(left[0], right[0], new LabelOp(falseLabel), intResult(left[0], right[0])));
    }
    public void visit(GreaterEq n){
        debugPrompt("GreaterEq");

        Label falseLabel = context.getFalseLabel();

        Operand[] left = new Operand[1], right = new Operand[1];
        processCompBinOp(n, left, right);

        emit(new brlt(left[0], right[0], new LabelOp(falseLabel), intResult(left[0], right[0])));
    }
    public void visit(Lesser n){
        debugPrompt("Lesser");

        Label falseLabel = context.getFalseLabel();

        Operand[] left = new Operand[1], right = new Operand[1];
        processCompBinOp(n, left, right);

        emit(new brgeq(left[0], right[0], new LabelOp(falseLabel), intResult(left[0], right[0])));
    }
    public void visit(LesserEq n){
        debugPrompt("LesserEq");

        Label falseLabel = context.getFalseLabel();

        Operand[] left = new Operand[1], right = new Operand[1];
        processCompBinOp(n, left, right);

        emit(new brgt(left[0], right[0], new LabelOp(falseLabel), intResult(left[0], right[0])));
    }
}

