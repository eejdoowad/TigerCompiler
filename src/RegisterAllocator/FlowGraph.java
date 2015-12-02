package RegisterAllocator;

import IR.*;
import Util.*;

import java.util.ArrayList;

// A Control Flow Graph Used for liveness analysis
// Implemented as a Graph where nodes are of subtype BasicBlock

//1. First statement in the Program is a leader
//2. Any statement that is the target of a branch is a leader
//3. Any statement immediate following a branch or return statement is a leader

public class FlowGraph extends DiGraph<BasicBlock> {

    public ArrayList<IR> instructions;
    public BasicBlock entryBlock;
    public BasicBlock exitBlock;

    public void initGlobalLiveness(){

    }

    public FlowGraph(ArrayList<IR> instructions){

        // Associate FlowGraph instructions with passed instruction stream
        this.instructions = instructions;

        // First generate leaders
        ArrayList<Integer> leaders = generateLeaders(instructions);

        // Then create a basic block for each leader
        String startLabel = ((Label)instructions.get(0)).name;
        entryBlock = new BasicBlock(FunctionLabel.generate("ENTRY_" + startLabel), -1);
        exitBlock = new BasicBlock(FunctionLabel.generate("EXIT_" + startLabel), -1);
        addNode(entryBlock);
        addNode(exitBlock);

        BasicBlock block = entryBlock;

        // Start at 1. First instruction will always be a label
        for (int i = 1; i < instructions.size(); i++){

            // If a leader is found, create a new Basic Block
            // and connect current block with the next, if the last  instruction
            // of the current block was NOT an unconditional jump
            if (leaders.contains(i)){
                BasicBlock next;
                if (instructions.get(i-1) instanceof Label){
                    next = new BasicBlock((Label)instructions.get(i-1), i);
                }
                else{
                    next = new BasicBlock(i);
                }
                addNode(next);

                // If the last instruction wasn't an unconditional jump
                // add edge to next block
                if (block.size() == 0
                        || !(block.lastInstruction() instanceof goTo)
                        || !(block.lastInstruction() instanceof ret)){
                    addEdge(block, next);
                }
                block = next;
            }
            // Only add to basic block if not a label
            if (instructions.get(i) instanceof instruction){
                block.addInstruction(instructions.get(i));
            }
        }


        // Now add edges for goTo and branches
        // calll and callr are treated like regular binops and immediately
        // proceed to next instruction
        // ret goes to exit block
        for (BasicBlock bb : getNodes()){
            IR lastInst = bb.lastInstruction();
            if (lastInst != null){
                if (lastInst instanceof branch){
                    branch lastInstB = (branch)lastInst;
                    addEdge(bb, getBlockByLabel(lastInstB.labelOp.label));
                }
                else if (lastInst instanceof goTo){
                    goTo lastInstG = (goTo)lastInst;
                    addEdge(bb, getBlockByLabel(lastInstG.labelOp.label));
                }
                else if (lastInst instanceof ret){
                    ret lastInstR = (ret)lastInst;
                    addEdge(bb, exitBlock);
                }
            }
        }
    }


    private ArrayList<Integer> generateLeaders(ArrayList<IR> instructions){
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
            if (    (instructions.get(i) instanceof controlFlowInstruction)
                    && !(instructions.get(i) instanceof call)
                    && !(instructions.get(i) instanceof callr)  ){
                int nextInstruction = i + 1;
                if (nextInstruction < instructions.size()){
                    if (!(instructions.get(nextInstruction) instanceof Label)){
                        leaders.add(nextInstruction);
                        i = nextInstruction;
                    }
                }
            }
        }

        return leaders;
    }


    private BasicBlock getBlockByLabel(Label l){
        for (BasicBlock block: getNodes()) {
            if (block.startLabel == l)
                return block;
        }
        // none found, return exit block
        // helps the case when a branch targeted a deleted label
        return exitBlock;
    }

    public Operand def(){
        return null;
    }

    public ArrayList<Operand> use(){
        return null;
    }

}
