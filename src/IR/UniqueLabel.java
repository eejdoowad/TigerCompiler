package IR;

import java.util.HashMap;

public class UniqueLabel extends Label {

    private static HashMap<String, UniqueLabel> uniqueLabels = new HashMap<>();

    private UniqueLabel(String name){
        this.name = name;
    }

    public static UniqueLabel generate(String name){
        if (uniqueLabels.containsKey(name)){
            return uniqueLabels.get(name);
        }
        else{
            UniqueLabel newLabel = new UniqueLabel(name);
            uniqueLabels.put(name, newLabel);
            return newLabel;
        }
    }
}
