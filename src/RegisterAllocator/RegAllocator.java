package RegisterAllocator;

import IR.*;

import java.util.*;

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

            for (BasicBlock block : flow.getNodes()){

                // all dead by default
                block.initLiveness();

                // calculate liveness by iteration
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

                Set<Var> vars = new HashSet<Var>();
                for (IR instruction : block.instructions()){
                    if (instruction.def() != null)
                        vars.add(instruction.def());
                    vars.addAll(instruction.use());
                }

                // now calculate live ranges
                // maps a Var to a list of uses
                // where each list entry corresponds to a different definition
                // the first entry will correspond to the uninitialized live range
                // when a var is used without being defined (as in function parameters)
                Map<Var, LinkedList<HashSet<Integer>>> liveRanges = new HashMap<>();
                for (Var var : vars){
                   // liveRanges.put(var, new LinkedList<>()); // uninitialied definition
//                    liveRanges.get(var).add(new HashSet<>()); /// empty hashset
                }


                for (int i = 0; i < block.size(); i++){

                    // If a variable is alive in this instruction
                    // add a range to its most recent definition
                    for (Var var : block.in(i)){
                        if (liveRanges.get(var).isEmpty()) // in the case of function parameters, no init before use, so add scope
                      //      liveRanges.get(var).add( new HashSet<>());
                        liveRanges.get(var).getLast().add(i);
                    }

                    // If a variable is defined, add a new range
                    Var def = block.getInstruction(i).def();
                    if (def != null){
                      //  liveRanges.get(def).add(new HashSet<>());
                        liveRanges.get(def).getLast().add(i);
                    }
                }

                System.out.println("Calculated in and out sets");



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
