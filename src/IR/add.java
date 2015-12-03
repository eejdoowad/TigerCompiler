package IR;

import IR.IRVisitor;

public class add extends binop {

    public add(Operand left, Operand right, Operand result, Boolean isInteger){
        super(left, right, result, isInteger);
    }

    public String op(){ return "add"; }
    public void accept(IRVisitor v) { v.visit(this); }
}
