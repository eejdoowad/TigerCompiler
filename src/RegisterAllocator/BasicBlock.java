package RegisterAllocator;

import IR.*;
import java.util.ArrayList;
import Util.Node;

// A Basic block is a stream of instructions that always execute together
// A Basic block has a start label, however some basic blocks have no associated label
// e.g. an instruciton immediately following a goto instruction

public class BasicBlock extends Node{

    private ArrayList<IR> instructions = new ArrayList<>();
    public Label startLabel;

    public BasicBlock(){

    }
    public BasicBlock(Label startLabel){
        this.startLabel = startLabel;
    }

    public void addInstruction(IR instruction){
        instructions.add(instruction);
    }

    public IR getInstruction(int i){
        return instructions.get(i);
    }

    public IR lastInstruction(){
        if (instructions.size() == 0) return null;
        else return instructions.get(instructions.size() - 1);
    }

    public int size(){
        return instructions.size();
    }

    public Var def(int i){
        return instructions.get(i).def();
    }

    public ArrayList<Var> use(int i){
        return instructions.get(i).use();
    }

    public String toString(){
        return "BB: " + ((startLabel == null) ? "unnamed" : startLabel);
    }

}
