package RegisterAllocator;

import IR.IR;
import java.util.ArrayList;

public class RegAllocator {


    public RegAllocator(ArrayList<IR> instructions){

        FlowGraph fg = new FlowGraph(instructions);

    }

    public ArrayList<IR> allocate(){
        return null;
    }
}
