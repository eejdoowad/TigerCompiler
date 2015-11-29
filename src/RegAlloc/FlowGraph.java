package RegAlloc;

import IR.*;
import Util.*;
import java.util.ArrayList;

// A Control Flow Graph Used for liveness analysis
// Implemented as a Graph where nodes are of subtype BasicBlock

public class FlowGraph extends Graph{

    public FlowGraph(ArrayList<IR> instructions){

        // Start by adding empty Entry Basic Block (seeing main label will add it)
        BasicBlock block = new BasicBlock();

        // Now add Basic Blocks based on instruction stream
        for (IR instruction : instructions) {
            if (instruction instanceof Label){
                // end of Basic Block, save and start new

                // Save Block
                addNode(block);

                // Start New Block
                block = new BasicBlock();
            }
            else if (instruction instanceof controlFlowInstruction){

            }
            else if (instruction instanceof regularInstruction){

            }
            else{
                System.out.println("WTH SORT OF IR ARE YOU");
                System.exit(1);
            }
        }

        addNode(new BasicBlock());
    }

    public Operand def(){
        return null;
    }

    public ArrayList<Operand> use(){
        return null;
    }
}
