package RegisterAllocator;

import IR.*;
import java.util.ArrayList;
import Util.Node;

// A Basic block is a stream of instructions that always execute together

public class BasicBlock extends Node{

    public ArrayList<IR> instructions = new ArrayList<>();

    public BasicBlock(){

    }

    public void addInstruction(IR instruction){
        instructions.add(instruction);
    }


}
