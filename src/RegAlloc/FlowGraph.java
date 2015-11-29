package RegAlloc;

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
            // Our implementation allows multiple consecutive labels,
            // So only a label followed by a non-label  should result in a new leader
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



        // Start by adding empty Entry Basic Block (seeing main label will add it)
        BasicBlock block = new BasicBlock();


        addNode(new BasicBlock());
    }

    public Operand def(){
        return null;
    }

    public ArrayList<Operand> use(){
        return null;
    }
}
