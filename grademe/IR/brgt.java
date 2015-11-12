package IR;

import AST.SemanticSymbol;

public class brgt extends branch {

    public brgt(Operand left, Operand right, LabelOp labelOp){
        super(left, right, labelOp);
    }
    public String toString(){
        return  "brgt, " + left + ", " + right + ", " + labelOp;
    }
}
