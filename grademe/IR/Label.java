package IR;

// note that labels are also classed as instructions

public abstract class Label extends IR {

    public String name;

    public String toString(){
        return name + ":";
    }
}
