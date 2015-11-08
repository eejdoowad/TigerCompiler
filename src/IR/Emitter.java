package IR;

import java.util.ArrayList;

public class Emitter {

    // note that labels are also instructions
    private ArrayList<IR> instructions = new ArrayList<>();

    public void emit(IR instruction){
        instructions.add(instruction);
    }

    public String toString(){
        String out = "";
        for (IR i : instructions){
            out += i.toString();
        }
        return out;
    }
}
