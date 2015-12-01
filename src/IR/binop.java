package IR;


// I'm not sure anymore if the instruction should have a semantic Symbol
// as its left and right members


// Now i'm thinking perhaps we should make a new class Operand
// where an operand is either

import java.util.ArrayList;

public abstract class binop extends regularInstruction {

    public Operand left;
    public Operand right;
    public Operand result;

    public binop(Operand left, Operand right, Operand result){
        this.left = left;
        this.right = right;
        this.result = result;
    }

    public binop(Operand left, Operand right, Operand result, Boolean isInteger){
        this.left = left;
        this.right = right;
        this.result = result;
        this.isInteger = isInteger;
    }

    public Var def(){
        return (Var)result;
    }

    public ArrayList<Var> use(){
        ArrayList<Var> uses = new ArrayList<>();
        if (left instanceof Var) uses.add((Var)left);
        if (right instanceof Var) uses.add((Var)right);
        return uses;
    }
    public abstract String op();
    public String toString(){
        return  op() + (isInt() ? "" : "f" ) + ", " + left + ", " + right + ", " + result;
    }
}
