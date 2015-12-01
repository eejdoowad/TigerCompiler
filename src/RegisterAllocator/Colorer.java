package RegisterAllocator;

import IR.*;

import java.util.ArrayList;

// LOAD LOAD STORE
class LLS{
    public load l1;
    public load l2;
    public store l3;
    public ArrayList<load> lParams = new ArrayList<>(); // any loads needed for array args
    public LLS(){

    }
}

public class Colorer {

    BasicBlock block;
    InterferenceGraph IG;
    ArrayList<IR> instructions;
    ArrayList<LLS> lls;


    public Colorer(BasicBlock block, InterferenceGraph IG){
        this.block = block;
        this.IG = IG;
    }

    public ArrayList<IR> color(){




        return null;
    }
}
