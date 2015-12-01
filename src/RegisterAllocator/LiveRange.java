package RegisterAllocator;

import IR.Var;

import java.util.HashSet;
import java.util.Set;

// A Live Range is a variable and the lines on which it is used
public class LiveRange {

    Var var;
    Set<Integer> lines = new HashSet<>();

    public LiveRange(Var var){
        this.var = var;
    }

    public void add(Integer line){
        lines.add(line);
    }

    boolean interferesWith(LiveRange other){
        if (this.var == other.var) return false;
        for (Integer i : lines){
            if (other.lines.contains(i)) return true;
        }
        return false;
    }
}
