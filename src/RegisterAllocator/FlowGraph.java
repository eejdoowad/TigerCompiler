package RegisterAllocator;

import IR.*;
import Util.*;
import java.util.ArrayList;

// A Control Flow Graph Used for liveness analysis
// Implemented as a Graph where nodes are of subtype BasicBlock

//1. First statement in the Program is a leader
//2. Any statement that is the target of a branch is a leader
//3. Any statement immediate following a branch or return statement is a leader

public class FlowGraph extends Graph{

    public FlowGraph(ArrayList<IR> instructions){

        // First find leaders
        ArrayList<Integer> leaders = new ArrayList<>();
        for (int i = 0; i < instructions.size(); i++){

            // An instruction is a leader if it is the target of a branch instruction
            // This is denoted by an instruction following a label
            // Our implementation allow no longer allows multiple consecutive labels,
            // But this code handles that case
            if (instructions.get(i) instanceof Label){
                int nextInstruction = i + 1;
                if (nextInstruction < instructions.size()){
                    if (!(instructions.get(nextInstruction) instanceof Label)){
                        leaders.add(nextInstruction);
                        i = nextInstruction; // avoid making it a leader twice
                    }
                }
            }
            if (instructions.get(i) instanceof controlFlowInstruction){
                int nextInstruction = i + 1;
                if (nextInstruction < instructions.size()){
                    if (!(instructions.get(nextInstruction) instanceof Label)){
                        leaders.add(nextInstruction);
                        i = nextInstruction;
                    }
                }
            }
        }

        // Then create a basic block for each leader
        // With appropriate instructions
        BasicBlock entry = new BasicBlock();
        BasicBlock block = entry;
        for (int i = 0; i < instructions.size(); i++){
            if (leaders.contains(i)){
                BasicBlock next = new BasicBlock();
                block.addSucc(next);
                block = next;
            }
            // Only add to basic block if not a label
            if (instructions.get(i) instanceof instruction){
                block.addInstruction(instructions.get(i));
            }
        }
        BasicBlock exit = new BasicBlock();
        block.addSucc(exit);


        // Now add edges between basic blocks

    }

    public Operand def(){
        return null;
    }

    public ArrayList<Operand> use(){
        return null;
    }


    public void addNode(Node n) {

    }
}
