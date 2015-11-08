package IR;

public class goTo extends IR {

    Label label;

    public goTo(Label label){
        this.label = label;
    }

    public String toString(){
        return "goto, " + label.name;
    }
}
