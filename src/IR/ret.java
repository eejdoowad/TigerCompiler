package IR;

public class ret extends IR {

    Operand retVal;

    public ret(Operand retVal){
        this.retVal = retVal;

    }
    public String toString(){
        return  "return, " + (retVal == null ? "" : retVal) + ", ,";
    }
}
