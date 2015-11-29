package IR;

public class goTo extends controlFlowInstruction {

    LabelOp labelOp;

    public goTo(LabelOp labelOp){
        this.labelOp = labelOp;
    }

    public String toString(){
        return "goto, " + labelOp.label.name;
    }
}
