package RegisterAllocator;

import IR.Register;
import IR.Var;
import IR.load;
import IR.store;

import java.util.ArrayList;

// List of loads that will be inserted before an instruction
// store that will be inserted after an instruction
public class LoadStore {
    public ArrayList<load> iloads = new ArrayList<>(); // any loads before instruction
    public ArrayList<store> istores = new ArrayList<>(); // any stores after instruction

    public String toString(){
        String out = "";
        if (!iloads.isEmpty()){
            out += "[";
            for (load l : iloads){
                out += l + ", ";
            }
            out += "]";
        }
        if (!istores.isEmpty()){
            out += "[";
            for (store s : istores){
                out += s + ", ";
            }
            out += "]";
        }
        return out;
    }

    // returns the  the size of loads after addition
    public int addLoad(Var src, Register dst){
        iloads.add(new load(dst, src, src.isInt()));
        return iloads.size();
    }
    // returns the size of stores after addition
    public int addStore(Register src, Var dst){
        istores.add(new store(src, dst, dst.isInt()));
        return istores.size();
    }
}
