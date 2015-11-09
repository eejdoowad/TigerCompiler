package IR;

import AST.SemanticSymbol;

public class div extends binop {

    public div(Operand left, Operand right, Operand result){
        super(left, right, result);
    }
    public String toString(){
        return  "div, " + left + ", " + right + ", " + result;
    }
}
