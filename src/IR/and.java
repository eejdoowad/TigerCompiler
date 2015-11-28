package IR;

public class and extends binop {

    public and(Operand left, Operand right, Operand result){
        super(left, right, result);
    }
    public String toString(){
        return  "and, " + left + ", " + right + ", " + result;
    }
}
