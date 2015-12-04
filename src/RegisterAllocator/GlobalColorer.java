package RegisterAllocator;

import IR.*;
import Util.Node;

import java.util.*;

public class GlobalColorer {

    private ArrayList<IR> instructions;
    private GlobalInterferenceGraph IG;
    private GlobalInterferenceGraph workGraph;

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


    public GlobalColorer(ArrayList<IR> instructions, GlobalInterferenceGraph IG){
        this.instructions = instructions;
        this.IG = IG;
        for (int i = 0; i < instructions.size(); i++) loadStores.add(new LoadStore());

        intRegs.add(Register.Reg.T0);
        intRegs.add(Register.Reg.T1);
        intRegs.add(Register.Reg.T2);
        intRegs.add(Register.Reg.T3);
        intRegs.add(Register.Reg.T4);
        intRegs.add(Register.Reg.T5);
        intRegs.add(Register.Reg.T6);
        intRegs.add(Register.Reg.T7);
//        intRegs.add(Register.Reg.S2);
//        intRegs.add(Register.Reg.S3);
//        intRegs.add(Register.Reg.S4);
//        intRegs.add(Register.Reg.S5);
//        intRegs.add(Register.Reg.S6);
//        intRegs.add(Register.Reg.S7);

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
        for (int i = 0; i < instructions.size(); i++){
            LoadStore ls = loadStores.get(i);

            // insert loads before
            for (load iload : ls.iloads){
                out.add(iload);
            }
            // the actual instruction
            out.add(instructions.get(i));

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

        GlobalInterferenceGraph workGraphOriginal = workGraph.copy();

        // stack of Live ranges that have to be assigned colors
        Stack<GlobalLiveRange> colorStack = new Stack<>();

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
            GlobalLiveRange lr = colorStack.pop();

            Node<GlobalLiveRange> node = workGraphOriginal.getNode(lr);

            // Cycle through colors until an available one is found
            for (Register.Reg color : colorInteger ? intRegs : floatRegs ){
                boolean colorUsed = false;
                for (Node<GlobalLiveRange> neighbor : node.getAdj()){
                    if (neighbor.val.getColor() == color){
                        colorUsed = true;
                    }
                }
                if (!colorUsed){
                    if (lr.getColor() == null) {
                        lr.setColor(color);
                    }
                    break;
                }
            }
            if (lr.getColor() == null){
                System.out.println("NO COLOR AVAILABLE. ERROR. GRAPH SHOULD BE COLORABLE AT THIS STAGE");
            }
        }

    }


    private void allocate() {

        // For every live Range
        for (GlobalLiveRange liveRange : IG.ranges.allRanges()) {

            // don't reallocate spilled variables
            // that are already assigned colors.
            if (liveRange.spilled){
                continue;
            }


            Var var = liveRange.var;

            Register reg = new Register(liveRange.getColor());
            boolean isInt = var.isInt();
            Set<Integer> definitionLines = liveRange.getDefinitionLines();

            // it is not necessary for the definition line to actually contain the def

            // if the definition line has a definition
            for (Integer definitionLine : definitionLines){
                if (definitionLine < 0){
                    System.out.println("WTH DONT DO THIS TO ME GLOBALColor.allocate())");
                    System.out.println("" + var + "has definitionLine " + definitionLine + " but there is no definition on that line");
                }
                else if (instructions.get(definitionLine).def() == var) {

                    // FOR GLOBAL ALLOCATION, UNLIKE INTRABLOCK ALLOCATION,
                    // YOU DONT HAVE TO STORE TEMPORARIES AS THEY WILL NEVER
                    // BE USED OUTSIDE THEIR LIVE RANGE

                    // if that definition is used, assign its color and store it if it's not a temp
                    instructions.get(definitionLine).replaceDef(var, reg);
                    if (!(var instanceof TempVar)){
                        loadStores.get(definitionLine).addStore(reg, var);
                    }
                } else{
                    System.out.println("ALSO2: WTH DONT DO THIS TO ME GLOBALColor.allocate())");
                    System.out.println("" + var + "has definitionLine " + definitionLine + " but there is no definition on that line");
                }
            }
//            // if the definition line has no definition, this indicates a var is used without being defined
//            // so load it from mem in next line
//            else {
//                // a variable is used on line 0 without being defined
//                // so load it on definition line
////                if (definitionLine + 1 >= block.size())
////                    System.out.println("ERROR: allocate() tried to add load past end of block");
////                else loadStores.get(definitionLine + 1).addLoad(var, reg);
//                System.out.println("GLOBAL ALLOCATION SHOULD NEVER REACH HERE");
//            }


            // For every line in the live range
            for (Integer i : liveRange.getLiveLines()){
                instructions.get(i).replaceUses(var, reg);
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
    private void spill(GlobalLiveRange liveRange){
        liveRange.spilled = true;
        Var var = liveRange.var;
        boolean isInt = var.isInt();
        Register res1 = Register.res1(var.isInt());
        Register res2 = Register.res2(var.isInt());
        Set<Integer> definitionLines = liveRange.getDefinitionLines();
        boolean usingRes2 = false; // only use res2 if res1 is occupied

        // add load and store for every use line if actually used

        // for every line in live range
        for (Integer i : liveRange.getLiveLines()){

            // if the var is used on that line
            boolean used = instructions.get(i).use().contains(var);

            if (instructions.get(i).use().contains(var)){


                if (!(instructions.get(i) instanceof callInstruction)){

                    // insert load before use, for non-function-call instructions

                    if (loadStores.get(i).iloads.size() == 0){
                        loadStores.get(i).addLoad(var, res1);
                        instructions.get(i).replaceUses(liveRange.var, res1);
                        liveRange.setColor(res1.register);
                    }
                    else if (loadStores.get(i).iloads.size() == 1){
                        loadStores.get(i).addLoad(var, res2);
                        instructions.get(i).replaceUses(liveRange.var, res2);
                        liveRange.setColor(res2.register);
                        usingRes2 = true;
                    }
                    else{
                        System.out.println("WTH? BOTH LOADS ARE USED.");
                    }

                    // insert store after use if it is a destination, for non-function-call instructions
//                    if (liveRange.var == block.getInstruction(i).def()) {
//                        loadStores.get(i).addStore(usingRes2 ? res2 : res1, var);
//                    }
                }
                // TODO: need to do something about function arguments
                else{
                    System.out.println("RegisterAllocator.Colorer.spill(): NO LOADING OF FUNCTION ARGS SUPPORTED");

                }

            }

        }

        for (Integer definitionLine : definitionLines){
            if (definitionLine >= 0){
                if (instructions.get(definitionLine).def() == var){
                    instructions.get(definitionLine).replaceDef(var, res1);
                    loadStores.get(definitionLine).addStore(res1, var);
                }
            }
        }
    }






}
