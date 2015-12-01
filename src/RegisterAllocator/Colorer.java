package RegisterAllocator;

import IR.*;
import Util.Node;

import java.util.*;

// LOAD LOAD STORE, and PARAM FOR FUNCTION CALL
class LLS{
    public load l1;
    public load l2;
    public store l3;
    public ArrayList<load> lParams = new ArrayList<>(); // any loads needed for array args
    public LLS(){

    }
}

public class Colorer {

    private BasicBlock block;
    private InterferenceGraph IG;
    private InterferenceGraph workGraph;

    private ArrayList<IR> instructions;
    private ArrayList<LLS> lls;

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

        colorInts();
        colorFloats();

        return null;
    }

    // note that if the interference graph is correctly generated
    // the
    private void colorInts(){
        // the working graph that we will modify
        // leave the original intact
        workGraph = IG.copy();

        // get a list of all int nodes in graph
        LinkedList<Node<LiveRange>> workGraphNodes = new LinkedList<>();
        for (Node<LiveRange> node : workGraph.getIntNodes()){
            workGraphNodes.add(node);
        }

        // Now add to colorStack in opposite order that colors should be assigned

        Stack<LiveRange> colorStack = new Stack<>();

        // while nodes remaining
        while (!workGraphNodes.isEmpty()){

            // Remove nodes that have degree < N
            // Push the removed nodes onto a stack
            for (int i = 0; i < workGraphNodes.size(); i++){
                if (workGraphNodes.get(i).degree() < numIntColors()){
                    colorStack.push(workGraphNodes.get(i).val); // push Live Range onto
                    workGraph.removeNode(workGraphNodes.get(i)); // remove node and all its edges from interference graph
                    workGraphNodes.remove(i); // remove from work graph
                    i = 0; // restart search
                }
            }
            // When all the nodes have degree >= N
            // Find a node  with least spill cost and spill it
            if (!workGraphNodes.isEmpty()) {
                int remove = 0;
                for (int i = 1; i < workGraphNodes.size(); i++) {
                    if (workGraphNodes.get(i).val.spillCost() < workGraphNodes.get(remove).val.spillCost()) {
                        remove = i;
                    }
                }
                spill(workGraphNodes.get(remove).val); // spill the var
                workGraphNodes.remove(remove); // and remove it from the working graph
            }
        }

    }

    // To spill, insert a load before every use and a store after every def
    private void spill(LiveRange liveRange){
        // TODO implement spill
    }

    private void colorFloats(){

    }
}
