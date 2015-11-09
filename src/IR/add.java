package IR;

import AST.SemanticSymbol;

public class add extends binop {

    public add(Operand left, Operand right, Operand result){
        super(left, right, result);
    }
    public String toString(){
        return  "add, " + left + ", " + right + ", " + result;
    }
}
