package RegisterAllocator;


import IR.IR;
import IR.Var;


import java.util.*;

public class LiveRanges {

    private Map<Var, LinkedList<Set<Integer>>> liveRanges = new HashMap<>();
    private Set<Var> vars = new HashSet<Var>();

    // returns variables used by Basic Block
    public Set<Var> getVars(){
        return vars;
    }
    // returns a list of live ranges of a variable,
    // every definition of a variable gets its own entry in the list
    public LinkedList<Set<Integer>> getRanges(Var var){
        return liveRanges.get(var);
    }


    private void addVar(Var v){
        vars.add(v);
    }
    private void addVars(ArrayList<Var> vars){
        this.vars.addAll(vars);
    }
    private void startNewLiveRange(Var var){
        //liveRanges.get(var).add( new HashSet<>());
    }
    private void addLiveEntry(Var var, int line){
        liveRanges.get(var).getLast().add(line);
    }

    public LiveRanges(BasicBlock block){

        // initialize vars
        for (IR instruction : block.instructions()){
            if (instruction.def() != null)
                addVar(instruction.def());
            addVars(instruction.use());
        }

        // now calculate live ranges
        // maps a Var to a list of uses
        // where each list entry corresponds to a different definition
        // the first entry will correspond to the uninitialized live range
        // when a var is used without being defined (as in function parameters)
        for (Var var : vars){
           // liveRanges.put(var, new LinkedList<>());
        }

        for (int i = 0; i < block.size(); i++){

            // If a variable is alive in this instruction
            // add a range to its most recent definition
            for (Var var : block.in(i)){
                if (liveRanges.get(var).isEmpty()) // in the case of function parameters, no init before use, so add scope
                    startNewLiveRange(var);
                addLiveEntry(var, i);
            }

            // If a variable is defined, add a new range
            Var def = block.getInstruction(i).def();
            if (def != null){
                startNewLiveRange(def);
                addLiveEntry(def, i);
            }
        }


    }


}
