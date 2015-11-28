package IR;

public class div extends binop {

    public div(Operand left, Operand right, Operand result){
        super(left, right, result);
    }
    public String toString(){
        return  "div, " + left + ", " + right + ", " + result;
    }
}
