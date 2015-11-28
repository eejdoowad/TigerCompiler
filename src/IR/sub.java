package IR;

public class sub extends binop {

    public sub(Operand left, Operand right, Operand result){
        super(left, right, result);
    }
    public String toString(){
        return  "sub, " + left + ", " + right + ", " + result;
    }
}
