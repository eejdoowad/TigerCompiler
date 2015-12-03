package RegisterAllocator;

import IR.*;
import Util.Node;

import javax.sound.sampled.Line;
import java.util.*;

// List of loads that will be inserted before an instruction
// store that will be inserted after an instruction
class LoadStore {
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

public class Colorer {

    private BasicBlock block;
    private InterferenceGraph IG;
    private InterferenceGraph workGraph;

    private ArrayList<LoadStore> loadStores = new ArrayList<>();

    private LinkedHashSet<Register.Reg> intRegs = new LinkedHashSet<>();
    private LinkedHashSet<Register.Reg> floatRegs = new LinkedHashSet<>();
    private int numIntColors(){
        return intRegs.size(); // intRegs.size();
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

        allocate(); // now use colors to rewrite IR with new regs and loads/stores

        ArrayList<IR> out = new ArrayList<>();

        // generate new IR for every instruction, include loads and stores
        for (int i = 0; i < block.instructions().size(); i++){
            LoadStore ls = loadStores.get(i);

            // insert loads before
            for (load iload : ls.iloads){
                out.add(iload);
            }
            // the actual instruction
            out.add(block.getInstruction(i));

            // insert stores after
            for (store istore : ls.istores){
                out.add(istore);
            }
        }
        return out;
    }

    // color either integers or floats based on passed bool
    private void colorForType(boolean colorInteger){
        // the working graph that we will modify, leave the original intact
        // exclude the float nodes
        workGraph = IG.copy();
        for (int i = workGraph.size() - 1; i >= 0; i--){
            // if the node is of the opposite type, remove it
            boolean temp = workGraph.get(i).val.var.isInt() != colorInteger;
            if (workGraph.get(i).val.var.isInt() != colorInteger){
                workGraph.remove(i);
            }
        }

        InterferenceGraph workGraphOriginal = workGraph.copy();

        // stack of Live ranges that have to be assigned colors
        Stack<LiveRange> colorStack = new Stack<>();

        // while nodes remaining
        while (!workGraph.isEmpty()){

            // Remove nodes that have degree < N
            // Push the removed nodes onto a stack
            for (int i = 0; i < workGraph.size();){
                Var curVar = workGraph.get(i).val.var;
                int degree = workGraph.get(i).degree();
                int numColors = (colorInteger ? numIntColors() : numFloatColors());
                if (degree < numColors){
                    colorStack.push(workGraph.get(i).val); // push Live Range onto stack
                    workGraph.remove(i); // remove node and all its edges from interference graph
                    i = 0; // restart search
                }
                else{
                    i++;
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

            // Cycle through colors until an available one is found
            for (Register.Reg color : colorInteger ? intRegs : floatRegs ){
                boolean colorUsed = false;
                for (Node<LiveRange> neighbor : node.getAdj()){
                    if (neighbor.val.getColor() == color){
                        colorUsed = true;
                    }
                }
                if (!colorUsed){
                    lr.setColor(color);
                    break;
                }
            }
            if (lr.getColor() == null){
                System.out.println("NO COLOR AVAILABLE. ERROR. GRAPH SHOULD BE COLORABLE AT THIS STAGE");
            }
        }

    }

    // After a color has been assigned to every live range
    // go through them and insert loads and stores form the var
    // to the colored register
    private void allocate() {

        // For every live Range
        for (LiveRange liveRange : IG.ranges.allRanges()) {

            // don't reallocate spilled variables
            // that are already assigned colors
            if (liveRange.spilled){
                continue;
            }


            Var var = liveRange.var;
            if (liveRange.getColor() == null){
                int j = 1;
            }
            Register reg = new Register(liveRange.getColor());
            boolean isInt = var.isInt();
            int definitionLine = liveRange.definitionLine;

            // it is not necessary for the definition line to actually contain the def

            // if the definition line has a definition
            if (block.def(definitionLine) == var) {

                // if that definition is never used, assign to reserved var then store it
                if (liveRange.getLines().size() == 0) {
                    Register res1 = Register.res1(var.isInt());
                    block.getInstruction(definitionLine).replaceDef(var, res1);
                    if (!(var instanceof TempVar))
                        loadStores.get(definitionLine).addStore(res1, var);
                }
                // if that definition is used, assign its color and store it
                else {
                    block.getInstruction(definitionLine).replaceDef(var, reg);
                    if (!(var instanceof TempVar))
                        loadStores.get(definitionLine).addStore(reg, var);
                }
            }
            // if the definition line has no definition, this indicates a var is used without being defined
            // so load it from mem in next line
            else {
                // a variable is used on line 0 without being defined
                // so load it on definition line
                if (definitionLine + 1 >= block.size())
                    System.out.println("ERROR: allocate() tried to add load past end of block");
                else loadStores.get(definitionLine + 1).addLoad(var, reg);
            }


            // For every line in the live range
            for (Integer i : liveRange.getLines()){
                block.getInstruction(i).replaceUses(var, reg);
            }

        }
    }

    // To spill:
    // For the given live range
    // For every line in the live range:
    //  if var is used in the instruction at that line
    //      1. insert a load to a reserved var before line
    //      2. insert a store from the same reserved var after line
    //  insert a store after the first definition, if there was one
    private void spill(LiveRange liveRange){
        liveRange.spilled = true;
        Var var = liveRange.var;
        boolean isInt = var.isInt();
        Register res1 = Register.res1(var.isInt());
        Register res2 = Register.res2(var.isInt());
        int definitionLine = liveRange.definitionLine;
        boolean usingRes2 = false; // only use res2 if res1 is occupied

        // add load and store for every use line if actually used

        // for every line in live range
        for (Integer i : liveRange.getLines()){

            // if the var is used on that line
            boolean used = block.getInstruction(i).use().contains(var);

            if (block.getInstruction(i).use().contains(var)){


                if (!(block.getInstruction(i) instanceof callInstruction)){

                    // insert load before use, for non-function-call instructions

                    if (loadStores.get(i).iloads.size() == 0){
                        loadStores.get(i).addLoad(var, res1);
                        block.getInstruction(i).replaceUses(liveRange.var, res1);
                        liveRange.setColor(res1.register);
                    }
                    else if (loadStores.get(i).iloads.size() == 1){
                        loadStores.get(i).addLoad(var, res2);
                        block.getInstruction(i).replaceUses(liveRange.var, res2);
                        liveRange.setColor(res2.register);
                        usingRes2 = true;
                    }
                    else{
                        System.out.println("WTH? BOTH LOADS ARE USED.");
                    }

                    // insert store after use, for non-function-call instructions
                    loadStores.get(i).addStore(usingRes2 ? res2 : res1, var);
                }
                // TODO: need to do something about function arguments
                else{
                    System.out.println("RegisterAllocator.Colorer.spill(): NO LOADING OF FUNCTION ARGS SUPPORTED");

                }

            }

        }

        // add store for definition line
        // if there is in fact a definition (recall there may be no definiton)
        if (block.def(definitionLine) == var){
            block.getInstruction(definitionLine).replaceDef(var, res1);
            loadStores.get(definitionLine).addStore(res1, var);
        }


    }






}
