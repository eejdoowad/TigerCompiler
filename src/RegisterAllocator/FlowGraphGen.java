package RegisterAllocator;

import IR.*;

import java.util.ArrayList;

public class FlowGraphGen {

    // I generate a control flow graph for every function
    // main is a function
    // call and callr are like binop instructions that always connect to the next instruction
    // ret is a one way ticket out
    public static ArrayList<FlowGraph> generate(ArrayList<IR> instructions){

        // First seperate instruction stream for each function
        ArrayList<ArrayList<IR>> functionInstructions = new ArrayList<>();
        ArrayList<IR> current = new ArrayList<>();

        for (IR instruction : instructions){
            if (instruction instanceof UniqueLabel){
                current = new ArrayList<>();
                functionInstructions.add(current);
            }
            current.add(instruction);
        }

        // Now generate a flow graph for each function
        ArrayList<FlowGraph> out = new ArrayList<>();
        for (ArrayList<IR> instructionStream : functionInstructions){
            out.add(new FlowGraph(instructionStream));
        }

        return out;
    }
}
