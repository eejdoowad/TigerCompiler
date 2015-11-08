package IR;

// note that labels are also classed as instructions

public class Label extends IR {

//    public enum LabelType {
//        MAIN("main"),
//        FUN("sub"),
//        PRELOOP("mult"),
//        POSTLOOP("div"),
//        AND("and"),
//        OR("or");
//        private final String str;
//        private LabelType(String s) { str = s; }
//    }
//    public LabelType label;

    public static int labelNum = 0;
    public int id; // each label should be uniquely identified I think...

    public String name;

    public Label(String name){
        this.name = name + id;
        id = labelNum++;
    }

    public String toString(){
        return name;
    }
}
