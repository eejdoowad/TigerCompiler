package IR;

import AST.SemanticSymbol;

public class or extends binop {

    public or(Operand left, Operand right, Operand result){
        super(left, right, result);
    }
    public String toString(){
        return  "or, " + left + ", " + right + ", " + result;
    }
}
