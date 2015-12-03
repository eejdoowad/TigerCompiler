package IR;

import java.util.HashMap;

public class FunctionLabel extends Label {

    protected static HashMap<String, FunctionLabel> uniqueLabels = new HashMap<>();

    protected FunctionLabel() {}
    protected FunctionLabel(String name){
        this.name = name;
    }

    public static FunctionLabel generate(String name){
        if (uniqueLabels.containsKey(name)){
            return uniqueLabels.get(name);
        }
        else{
            FunctionLabel newLabel = new FunctionLabel(name);
            uniqueLabels.put(name, newLabel);
            return newLabel;
        }
    }
    public void accept(IRVisitor v) { v.visit(this); }
}
