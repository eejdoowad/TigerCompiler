package RegisterAllocator;

import IR.*;
import java.util.ArrayList;
import java.util.HashSet;

import Util.DiNode;

// A Basic block is a stream of instructions that always execute together
// A Basic block has a start label, however some basic blocks have no associated label
// e.g. an instruciton immediately following a goto instruction

public class BasicBlock extends DiNode {

    private ArrayList<IR> instructions = new ArrayList<>();


    private ArrayList<HashSet<Var>> live = new ArrayList<>();
    public void initLiveness(){
        for (int i = 0; i <= size(); i++){
            live.add(new HashSet<Var>());
        }
    }
    // get live variables before instruction i
    public HashSet<Var> in(int i){
        return live.get(i);
    }
    // get live variables after instruction i
    public HashSet<Var> out(int i){
        return live.get(i+1);
    }
    // get in live variables of whole block (equal to liveness of first instruction in)
    public HashSet<Var> blockIn(){
        if (size() == 0) return null;
        else return live.get(0);
    }
    // get out live variables of whole block (equal to liveness of last instruction out)
    public HashSet<Var> blockOut(){
        if (size() == 0) return null;
        else return live.get(size());
    }

    // the index into the original instruction stream where the BB starts
    public int startIndex;


    public Label startLabel;

    public BasicBlock(int startIndex){
        this.startIndex = startIndex;
    }
    public BasicBlock(Label startLabel, int startIndex){
        this.startLabel = startLabel;
        this.startIndex = startIndex;
    }

    public void addInstruction(IR instruction){
        instructions.add(instruction);
    }

    public IR getInstruction(int i){
        return instructions.get(i);
    }

    public ArrayList<IR> instructions(){
        return instructions;
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
//
//    public ArrayList<Var> use(int i){
//        return instructions.get(i).use();
//    }

    public String toString(){
        return "BB: " + ((startLabel == null) ? "unnamed" : startLabel);
    }

}
