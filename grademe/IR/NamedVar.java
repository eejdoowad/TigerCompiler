package IR;

public class NamedVar extends Var {

    public String name;

    public NamedVar(String name){
        this.name = name;
    }

    public String toString(){
        return name;
    }
}
