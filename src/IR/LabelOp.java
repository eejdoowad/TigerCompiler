package IR;

public class LabelOp extends Operand {

    public Label label;

    public LabelOp(Label label){
        this.label = label;
    }

    public String toString(){
        return label.name;
    }
}
