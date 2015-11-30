package RegisterAllocator;

import IR.*;
import java.util.ArrayList;
import java.util.HashSet;

import Config.*;

public class RegAllocator {

    private RegAllocator(){}

    // Given an stream of IR instructions with symbol names
    // returns a stream with MIPS register names

    public static ArrayList<IR> allocate(ArrayList<IR> instructions){
        if (Config.REG_ALLOCATOR == Config.RegAllocator.NAIVE){
            System.out.println("DOING NAIVE ALLOCATION");
            return naiveAllocator(instructions);
        }
        else if (Config.REG_ALLOCATOR == Config.RegAllocator.INTRABLOCK){
            System.out.println("DOING INTRABLOCK ALLOCATION");
            return intraBlockAllocator(instructions);
        }
        else if (Config.REG_ALLOCATOR == Config.RegAllocator.EBB){
            System.out.println("DOING EBB ALLOCATION");
            return EBBAllocator(instructions);
        }
        else{
            System.out.println("WTH");
            System.exit(1);
            return null;
        }
    }

    private static ArrayList<IR> naiveAllocator(ArrayList<IR> instructions){
        return instructions;
    }

    private static ArrayList<IR> intraBlockAllocator(ArrayList<IR> instructions){
        ArrayList<FlowGraph> flows = FlowGraphGen.generate(instructions);
        for (FlowGraph flow : flows){
            // replace symbolic registers with fixed register set
            // by calculating liveness ranges

            // for IntraBlock, calculate liveness on a per block basis
            for (BasicBlock block : flow.getNodes()){
                block.initLiveness(); // all to dead
            }
            for (BasicBlock block : flow.getNodes()){
                boolean changes;
                do{
                    changes = false;

                    for (int i = 0; i < block.size(); i++){
                        HashSet<Var> additions = new HashSet<>();
                        additions.addAll(block.out(i));
                        additions.remove(block.getInstruction(i).def());
                        additions.addAll(block.getInstruction(i).use());
                        if (!block.in(i).containsAll(additions)){
                            changes = true;
                            block.in(i).addAll(additions);
                        }
                        System.out.print("");
                    }

                } while (changes);
            }
            System.out.println("Calculated in and out sets");

            // then generating interference graphs
            // then coloring registers
        }
        return null;
    }

    private static ArrayList<IR> EBBAllocator(ArrayList<IR> instructions){
        return null;
    }
}
