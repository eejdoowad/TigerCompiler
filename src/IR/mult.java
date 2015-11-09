package IR;

import AST.SemanticSymbol;

public class mult extends binop {

    public mult(Operand left, Operand right, Operand result){
        super(left, right, result);
    }
    public String toString(){
        return  "mult, " + left + ", " + right + ", " + result;
    }
}
