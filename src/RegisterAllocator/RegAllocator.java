package RegisterAllocator;

import IR.*;

import java.util.*;

import Config.*;

public class RegAllocator {

    private RegAllocator(){}

    // Given an stream of IR instructions with symbol names
    // returns a stream with MIPS register names
    // and loads and stores inserted
    public static ArrayList<IR> allocate(ArrayList<IR> instructions){
        if (Config.REG_ALLOCATOR == Config.RegAllocator.NAIVE){
            System.out.println("DOING NAIVE ALLOCATION\n");
            return naiveAllocator(instructions);
        }
        else if (Config.REG_ALLOCATOR == Config.RegAllocator.INTRABLOCK){
            System.out.println("DOING INTRABLOCK ALLOCATION\n");
            return intraBlockAllocator(instructions);
        }
        else if (Config.REG_ALLOCATOR == Config.RegAllocator.GLOBAL){
            System.out.println("DOING GLOBAL ALLOCATION\n");
            return globalAllocator(instructions);
        }
        else{
            System.out.println("WTH SORT OF ALLOCATION YOU DOING\n");
            System.exit(1);
            return null;
        }
    }

    // No liveness analysis
    private static ArrayList<IR> naiveAllocator(ArrayList<IR> instructions){
        NaiveAllocatorVisitor allocator = new NaiveAllocatorVisitor();
        for (IR i : instructions) {
            i.accept(allocator);
        }
        return allocator.instructions;
    }

    // Does liveness analysis at the BasicBlock level
    private static ArrayList<IR> intraBlockAllocator(ArrayList<IR> instructions){
        ArrayList<IR> out = new ArrayList<>();

        ArrayList<FlowGraph> flows = FlowGraphGen.generate(instructions);
        for (FlowGraph flow : flows){
            // replace symbolic registers with fixed register set
            // by calculating liveness ranges

            for (BasicBlock block : flow.getNodes()){
                // don't do anything for dummy entry/exit blocks
                if (block.size() > 0){

                    block.calcLiveness();
                    LiveRanges ranges = new LiveRanges(block);
                    InterferenceGraph IG = new InterferenceGraph(ranges);
                    Colorer colorer = new Colorer(block, IG);
                    ArrayList<IR> newIR = colorer.color();

                    if (block.startLabel != null)
                        out.add(block.startLabel);
                    out.addAll(newIR);
                }
            }
        }
        return out;
    }

    // Does liveness analysis at the procedure level


    // 1. Generate Control Flow Graph For each Procedure, where the nodes of
    // the control flow graph are basic blocks
    // 2. Generate global live ranges, where live ranges correspond to the absolute line numbering
    //    and not the relative line numbering
    //    also generate list of definitions per live range
    //    Do a store after every definition (resolve the case where one web merges into next
    // 3. Generate Global Interference Graph from global live range
    // 4. Color at the global level
    private static ArrayList<IR> globalAllocator(ArrayList<IR> instructions){
        ArrayList<IR> out = new ArrayList<>();

        // Generate control flow graph
        ArrayList<FlowGraph> flows = FlowGraphGen.generate(instructions);

        for (FlowGraph flow : flows){

            flow.calcGlobalLiveness();
            GlobalLiveRanges ranges = new GlobalLiveRanges(flow);
            GlobalInterferenceGraph IG = new GlobalInterferenceGraph(ranges);
            GlobalColorer colorer = new GlobalColorer(instructions, IG);
            ArrayList<IR> newIR = colorer.color();
            for (IR instruction : newIR){
                out.add(instruction);
            }
        }
        return out;
    }
}
