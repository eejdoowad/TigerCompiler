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

    private void allocate(){
        System.out.println("Starting color allocation");

        // For every live Range
        for (LiveRange liveRange : IG.ranges.allRanges()){
            Var var = liveRange.var;
            Register reg = new Register(liveRange.getColor());
            boolean isInt = var.isInt();
            int definitionLine = liveRange.definitionLine;

            // it is not necessary for the definition line to actually contain the def

            // if the definition line has a definition
            if (block.def(definitionLine) == var){

                // if that definition is never used, assign a temp and store it
                if (liveRange.getLines().size() == 0){
                    Register res1 = Register.res1(var.isInt());
                    block.getInstruction(definitionLine).replaceDef(liveRange.var, res1);
                    loadStores.get(definitionLine).addStore(res1, var);
                }
                // if that definition is used, assign its color and store it
                else{
                    block.getInstruction(definitionLine).replaceDef(liveRange.var, reg);
                    loadStores.get(definitionLine).addStore(reg, var);
                }
            }
            // if the definition line has no definition, this indicates a var is used without being defined
            // so load it from mem in next line
            else{
                if (definitionLine + 1 >= block.size()) System.out.println("ERROR: allocate() tried to add load past end of block");
                else loadStores.get(definitionLine).addLoad(var, reg);
            }


            // For every line in the live range
            for (Integer i : liveRange.getLines()){
                block.getInstruction(i).replaceUses(var, reg);
            }

        }
    }

    // To spill, insert a load before every use and a store after every def
    // and replace every use and every def with the appropriate instruction
    private void spill(LiveRange liveRange){

        Var var = liveRange.var;
        Register res1 = Register.res1(var.isInt());
        Register res2 = Register.res2(var.isInt());

        for (Integer i : liveRange.getLines()){
            // insert store for def
            if ((i - 1 >= 0) && block.def(i - 1) == liveRange.var){
                boolean isInt = liveRange.var.isInt();
                loadStores.get(i - 1).addStore(res1, var);
                block.getInstruction(i - 1).replaceDef(liveRange.var, Register.res1(isInt));
            }
            // insert load before use, for non-function-call instructions
            if (!(block.getInstruction(i) instanceof callInstruction)){
                boolean isInt = liveRange.var.isInt();
                // only use res2 if res1 is occupied
                if (loadStores.get(i).iloads.size() == 0){
                    loadStores.get(i).addLoad(var, res1);
                    block.getInstruction(i).replaceUses(liveRange.var, Register.res1(isInt));
                }
                else if (loadStores.get(i).iloads.size() == 1){
                    loadStores.get(i).addLoad(var, res2);
                    block.getInstruction(i).replaceUses(liveRange.var, Register.res2(isInt));
                }
                else{
                    System.out.println("WTH? BOTH LOADS ARE USED.");
                }
            }
            // TODO: need to do something about function arguments
            else{
                System.out.println("RegisterAllocator.Colorer.spill(): NO LOADING OF FUNCTION ARGS SUPPORTED");
            }

        }
    }






}
