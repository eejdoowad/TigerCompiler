package IR;

import AST.SemanticSymbol;


// I'm not sure anymore if the instruction should have a semantic Symbol
// as its left and right members


// Now i'm thinking perhaps we should make a new class Operand
// where an operand is either

public abstract class binop extends IR {

    public Operand left;
    public Operand right;
    public Operand result;

    public binop(Operand left, Operand right, Operand result){
        this.left = left;
        this.right = right;
        this.result = result;
    }
}
