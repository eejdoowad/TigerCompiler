package IR;

public class goTo extends jumpLabel {


    public goTo(LabelOp labelOp){
        this.labelOp = labelOp;
    }

    public String toString(){
        return "goto, " + labelOp.label.name;
    }
}
