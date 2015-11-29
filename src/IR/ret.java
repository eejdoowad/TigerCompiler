package IR;

public class ret extends controlFlowInstruction {

    Operand retVal;

    public ret(Operand retVal){
        this.retVal = retVal;

    }
    public String toString(){
        return  "return, " + (retVal == null ? "" : retVal) + ", ,";
    }
}
