package IR;

import AST.SemanticSymbol;

public class brleq extends branch{

    public brleq(Operand left, Operand right, LabelOp labelOp){
        super(left, right, labelOp);
    }
    public String toString(){
        return  "brleq, " + left + ", " + right + ", " + labelOp;
    }
}
