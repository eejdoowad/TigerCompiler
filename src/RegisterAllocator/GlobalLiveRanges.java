package RegisterAllocator;


import IR.IR;
import IR.Var;


import java.util.*;

public class GlobalLiveRanges {

    // The set of Vars (including temporaries) used in the program
    private Set<Var> vars = new LinkedHashSet<Var>();
    public Set<Var> getVars(){
        return vars;
    }
    private void addVar(Var v){
        vars.add(v);
    }
    private void addVars(ArrayList<Var> vars){
        this.vars.addAll(vars);
    }

    // Maps a var to its  Global Live Ranges (spans across blocks)
    private Map<Var, LinkedList<GlobalLiveRange>> liveRanges = new HashMap<>();
    public Map<Var, LinkedList<GlobalLiveRange>> getRanges(){ return liveRanges;}

    // Returns a Linked List of all live ranges within the flow graph
    public LinkedList<GlobalLiveRange> allRanges(){
        LinkedList<GlobalLiveRange> out = new LinkedList<>();
        for (LinkedList<GlobalLiveRange> liveRanges : getRanges().values()){
            for (GlobalLiveRange range : liveRanges){
                out.add(range);
            }
        }
        return out;
    }

    //TODO
/*
    private void startNewLiveRange(Var var, int definitionLine){
        liveRanges.get(var).add( new GlobalLiveRange(var, definitionLine));
    }
    private void addLiveEntry(Var var, int line){
        liveRanges.get(var).getLast().add(line);
    }
*/
    public GlobalLiveRanges(FlowGraph flow){


        // initialize vars with all vars among all basic blocks in flow graph
        for (BasicBlock block : flow.getNodes()){
            for (IR instruction : block.instructions()){
                if (instruction.def() != null)
                    addVar(instruction.def());
                addVars(instruction.use());
            }
        }

        // now calculate live ranges


        // For every line:
        //  if live in current line,
        //      add to output this line
        //  if def in current line, recursively repeat search with new live range
        //      starting from entry line, follows every path into the next line
        //  else:
        //      addAll(repeat Search next line for all that are live for this var)


        // until:
        //  1. not live in any possible next instruction
        //  2. defined in current instruction

        // maps a Var to a list of uses
        // where each list entry corresponds to a different definition
        // the first entry will correspond to the uninitialized live range
        // when a var is used without being defined (as in function parameters)
        for (Var var : vars){
            liveRanges.put(var, new LinkedList<>());
        }

//        for (int i = 0; i < block.size(); i++){
//
//            // If a variable is alive in this instruction
//            // add a range to its most recent definition
//            for (Var var : block.in(i)){
//                if (liveRanges.get(var).isEmpty()) // in the case of function parameters, no init before use, so add scope
//                    startNewLiveRange(var, i);
//                addLiveEntry(var, i);
//            }
//
//            // If a variable is defined, add a new live range
//            Var def = block.getInstruction(i).def();
//            if (def != null){
//                startNewLiveRange(def, i);
//                // It is possible that the definition is never used
//                // in which case the live range is empty
//                // this line might be redundant, figure it out TODO
//                if (block.out(i).contains(def)) addLiveEntry(def, i+1);
//            }
//        }
//
//        // calculate number of uses for each live range
//        for (LiveRange lr : allRanges()){
//            for (Integer i : lr.getLines()){
//                for (Var v : block.getInstruction(i).use()){
//                    if (v == lr.var){
//                        lr.incrementUses();
//                        break;
//                    }
//                }
//
//            }
//        }


    }


}
