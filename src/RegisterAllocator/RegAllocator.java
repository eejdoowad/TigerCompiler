package RegisterAllocator;

import IR.IR;
import java.util.ArrayList;

public class RegAllocator {


    public RegAllocator(ArrayList<IR> instructions){

        ArrayList<FlowGraph> fg = FlowGraphGen.generate(instructions);


        System.out.println("REGISTERS ALLOCATED");

    }

    public ArrayList<IR> allocate(){
        return null;
    }
}
