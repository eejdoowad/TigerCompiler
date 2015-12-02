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
            System.out.println("WTH SORT OF ALLOCATION YOU DOING");
            System.exit(1);
            return null;
        }
    }

    private static ArrayList<IR> naiveAllocator(ArrayList<IR> instructions){
        NaiveAllocatorVisitor allocator = new NaiveAllocatorVisitor();
        for (IR i : instructions) {
            i.accept(allocator);
        }
        return allocator.instructions;
    }

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

                    out.add(block.startLabel);
                    out.addAll(newIR);


//                    System.out.println("For Block " + block.startLabel + " IR OUTPUT:");
//                    for (IR i : newIR){
//                        System.out.println(i);
//                    }

//                    System.out.println("END OF BLOCK ANALYSIS");
                }
            }
        }
        return out;
    }

    private static ArrayList<IR> EBBAllocator(ArrayList<IR> instructions){
        return null;
    }
}
