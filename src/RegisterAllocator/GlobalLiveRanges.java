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
        ArrayList<GlobalLiveRange> ranges = flow.getGlobalLiveRanges();
        for (GlobalLiveRange range : ranges) {
            Var var = range.var;
            if (!liveRanges.containsKey(var)) {
                addVar(var);
                liveRanges.put(var, new LinkedList<>());
            }
            liveRanges.get(var).add(range);
        }
    }


}
