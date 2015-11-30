package IR;

import java.util.ArrayList;

public class goTo extends jumpLabel {


    public goTo(LabelOp labelOp){
        this.labelOp = labelOp;
    }

    public Var def(){
        return null;
    }

    public ArrayList<Var> use(){
        ArrayList<Var> uses = new ArrayList<>();
        return uses;
    }

    public String toString(){
        return "goto, " + labelOp.label.name;
    }
    public void accept(IRVisitor v) { v.visit(this); }
}
