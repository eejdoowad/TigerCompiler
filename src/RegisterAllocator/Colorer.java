package RegisterAllocator;

import IR.*;
import Util.Node;

import java.util.*;

// List of loads that will be inserted before an instruction
// store that will be inserted after an instruction
class LoadStore {
    public load iload1;
    public load iload2;
    public ArrayList<load> iloadargs = new ArrayList<>(); // any loads needed for array args
    public store istore;
}

public class Colorer {

    private BasicBlock block;
    private InterferenceGraph IG;
    private InterferenceGraph workGraph;

    private ArrayList<LoadStore> loadStores = new ArrayList<>();

    private LinkedHashSet<Register.Reg> intRegs = new LinkedHashSet<>();
    private LinkedHashSet<Register.Reg> floatRegs = new LinkedHashSet<>();
    private int numIntColors(){
        return 1; // intRegs.size();
    }
    private int numFloatColors(){
        return floatRegs.size();
    }

    Stack<Node<LiveRange>> stack = new Stack<>();


    public Colorer(BasicBlock block, InterferenceGraph IG){
        this.block = block;
        this.IG = IG;
        for (int i = 0; i < block.size(); i++) loadStores.add(new LoadStore());

        intRegs.add(Register.Reg.T0);
        intRegs.add(Register.Reg.T1);
        intRegs.add(Register.Reg.T2);
        intRegs.add(Register.Reg.T3);
        intRegs.add(Register.Reg.T4);
        intRegs.add(Register.Reg.T5);
        intRegs.add(Register.Reg.T6);
        intRegs.add(Register.Reg.T7);

        floatRegs.add(Register.Reg.F0);
        floatRegs.add(Register.Reg.F1);
        floatRegs.add(Register.Reg.F2);
        floatRegs.add(Register.Reg.F3);
        floatRegs.add(Register.Reg.F4);
        floatRegs.add(Register.Reg.F5);
        floatRegs.add(Register.Reg.F6);
        floatRegs.add(Register.Reg.F7);
    }

    public ArrayList<IR> color(){

        colorForType(true); // first color integers
        colorForType(false); // then color floats

        ArrayList<IR> out = new ArrayList<>();

        // generate new IR for every instruction, include loads and stores
        for (int i = 0; i < block.instructions().size(); i++){
            LoadStore ls = loadStores[i];

            //TODO HANDLE FUNCTION ARGS IN CALLR

            // insert loads before
            if (ls != null) {
                if (ls.iload1 != null) out.add(ls.iload1);
                if (ls.iload1 != null) out.add(ls.iload2);
            }
            // the actuall instruction
            out.add(block.instructions().get(i));

            // insert stores after
            if (ls != null) {if (ls.istore != null) out.add(ls.istore);}
        }

        return out;
    }

    // color either integers or floats based on passed bool
    private void colorForType(boolean colorInteger){
        // the working graph that we will modify, leave the original intact
        // exclude the float nodes
        workGraph = IG.copy();
        for (Node<LiveRange> node : colorInteger ? workGraph.getFloatNodes() : workGraph.getIntNodes()){
            workGraph.removeNode(node);
        }
        InterferenceGraph workGraphOriginal = workGraph.copy();

        // stack of Live ranges that have to be assigned colors
        Stack<LiveRange> colorStack = new Stack<>();

        // while nodes remaining
        while (!workGraph.isEmpty()){

            // Remove nodes that have degree < N
            // Push the removed nodes onto a stack
            for (int i = 0; i < workGraph.size(); i++){
                if (workGraph.get(i).degree() < (colorInteger ? numIntColors() : numFloatColors())){
                    colorStack.push(workGraph.get(i).val); // push Live Range onto stack
                    workGraph.remove(i); // remove node and all its edges from interference graph
                    i = 0; // restart search
                }
            }
            // When all the nodes have degree >= N
            // Find a node  with least spill cost and spill it
            if (!workGraph.isEmpty()) {
                int remove = 0;
                for (int i = 1; i < workGraph.size(); i++) {
                    if (workGraph.get(i).val.spillCost() < workGraph.get(remove).val.spillCost()) {
                        remove = i;
                    }
                }
                spill(workGraph.get(remove).val); // spill the var
                workGraph.remove(remove); // and remove it from the working graph
            }
        }

        // Now assign colors to registers
        while (!colorStack.isEmpty()){
            LiveRange lr = colorStack.pop();

            Node<LiveRange> node = workGraphOriginal.getNode(lr);

            for (Register.Reg color : colorInteger ? intRegs : floatRegs ){
                boolean colorUsed = false;
                for (Node<LiveRange> neighbor : node.getAdj()){
                    if (neighbor.val.getColor() == color){
                        colorUsed = true;
                    }
                }
                if (!colorUsed){
                    lr.setColor(color);
                }
            }
            if (lr.getColor() == null){
                System.out.println("NO COLOR AVAILABLE. ERROR. GRAPH SHOULD BE COLORABLE AT THIS STAGE")
            }
        }

    }

    // To spill, insert a load before every use and a store after every def
    private void spill(LiveRange liveRange){

        for (Integer i : liveRange.getLines()){
            // insert store for def
            if (block.def(i) == liveRange.var){
                boolean isInt = liveRange.var.isInt();
                loadStores.get(i).istore = new store(Register.res1(isInt), liveRange.var, isInt);
            }
            // insert load before use, for non-function-call instructions
            if (!(block.getInstruction(i) instanceof callInstruction)){
                boolean isInt = liveRange.var.isInt();
                // only use res2 if res1 is occupied
                if (loadStores.get(i).iload1 == null)
                    loadStores.get(i).iload1 = new load(Register.res1(isInt), liveRange.var, isInt);
                else if (loadStores.get(i).iload2 == null)
                    loadStores.get(i).iload2 = new load(Register.res2(isInt), liveRange.var, isInt);
                else
                    System.out.println("WTH? BOTH LOADS ARE USED.");
            }
            // TODO: need to do something about function arguments
            else{
                System.out.println("RegisterAllocator.Colorer.spill(): NO LOADING OF FUNCTION ARGS SUPPORTED");
            }

        }
    }
}
