import IR.Label;
import IR.Operand;
import IR.SharedLabel;

import java.util.Stack;

class IRGenVisitorContext {
    private Operand retVal = null;
    private Label falseLabel = null; // propogated down to condition statements
    public Stack<SharedLabel> breakLabels = new Stack<>();

    public void setRetVal(Operand retVal){
        if (this.retVal != null){
            if (Config.DEBUG && Config.DEBUG_IRCODEGEN) System.out.println("ERROR: Attempted to set non-null retVal");
            //System.exit(1);
        }
        else{
            this.retVal = retVal;
        }
    }
    public Operand getRetVal(){
        if (retVal == null){
            if (Config.DEBUG && Config.DEBUG_IRCODEGEN) System.out.println("ERROR: Attempted to get null retVal");
            //System.exit(1);
            return null; // silence error
        }
        else{
            Operand localRetVal = retVal;
            retVal = null;
            return localRetVal;
        }
    }

    public void setFalseLabel(Label falseLabel){
        if (this.falseLabel != null){
            if (Config.DEBUG && Config.DEBUG_IRCODEGEN) System.out.println("ERROR: Attempted to set non-null falseLabel");
            //System.exit(1);
        }
        else{
            if (Config.DEBUG && Config.DEBUG_IRCODEGEN) System.out.println("SET falseLabel");
            this.falseLabel = falseLabel;
        }
    }
    public Label getFalseLabel() {
        if (falseLabel == null) {
            if (Config.DEBUG && Config.DEBUG_IRCODEGEN) System.out.println("ERROR: Attempted to get null falseLabel");
            //System.exit(1);
            return null; // silence error
        } else {
            if (Config.DEBUG && Config.DEBUG_IRCODEGEN) System.out.println("GET falseLabel");
            Label localFalseLabel = falseLabel;
            falseLabel = null;
            return localFalseLabel;
        }
    }
}
