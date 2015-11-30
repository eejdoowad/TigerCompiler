package IR;

import IR.IRVisitor;

public class add extends binop {

    public add(Operand left, Operand right, Operand result){
        super(left, right, result);
    }
    public String toString(){
        return  "add, " + left + ", " + right + ", " + result;
    }
    public void accept(IRVisitor v) { v.visit(this); }
}
