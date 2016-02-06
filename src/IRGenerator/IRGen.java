package IRGenerator;

import AST.ASTRoot;
import java.util.ArrayList;
import java.util.HashMap;
import IR.*;

public class IRGen {

    // AST representation output by parser
    public AST.ASTRoot ast;
    private ArrayList<IR> instructions;


    public IRGen(AST.ASTRoot ast){
        this.ast = ast;
    }

    // generates IR code using the AST
    public ArrayList<IR> generate(){
        IR2GenVisitor generator = new IR2GenVisitor(ast);
        instructions = generator.generateIR();

        removeRedundantLabels();

        return instructions;
    }

    // if there are multiple consecutive labels,
    // removes all but the last (main label always comes last)
    public void removeRedundantLabels(){
        // Two passes:
        // 1. Find equivalent labels, and remove consecutive labels
        // 2. Replace instruction label operands with found equivalent labels

        // a label is equivalent to itself.
        // labels are equivalent if they are consecutive

        // 1. Find equivalent labels, and remove consecutive labels
        HashMap<Label, Label> newLabel = new HashMap<>();
        for (int i = instructions.size() - 2; i >= 0; i--) {
            int first = i + 1;
            if (instructions.get(first) instanceof Label){
                Label firstLabel = (Label)instructions.get(first);
                newLabel.put(firstLabel, firstLabel);
                while (i >= 0 && instructions.get(i) instanceof Label){
                    newLabel.put((Label)instructions.get(i), firstLabel);
                    instructions.remove(i);
                    i--;
                }
            }
        }
        // this removes all but the first... but main could get replaced =(
//        for (int i = 1; i < instructions.size(); i++) {
//            int first = i - 1;
//            if (instructions.get(first) instanceof Label){
//                Label firstLabel = (Label)instructions.get(first);
//                newLabel.put(firstLabel, firstLabel);
//                while (i < instructions.size() && instructions.get(i) instanceof Label){
//                    newLabel.put((Label)instructions.get(i), firstLabel);
//                    instructions.remove(i);
//                }
//            }
//        }

        // 2. Replace instruction label operands with found equivalent labels
        for (IR instruction : instructions) {
            if (instruction instanceof controlFlowInstruction){
                if (instruction instanceof branch){
                    branch inst = ((branch)instruction);
                    inst.labelOp.label = newLabel.get(inst.labelOp.label);
                }
                else if (instruction instanceof goTo){
                    goTo inst = ((goTo)instruction);
                    inst.labelOp.label = newLabel.get(inst.labelOp.label);
                }
                // FUNCTION CALL LABELS ARE NEVER CONSECUTIVE
                // AND ret never has a label as its operand
//                else if (instruction instanceof call){
//                    call inst = ((call)instruction);
//                    inst.labelOp.label = newLabel.get(inst.labelOp.label);
//                }
//                else if (instruction instanceof callr){
//                    callr inst = ((callr)instruction);
//                    inst.labelOp.label = newLabel.get(inst.labelOp.label);
//                }
            }
        }

    }



    public String toString(){
        String out = "";
        for (IR i : instructions){
            out += ((i instanceof Label) ? "" : "\t") + i.toString() + "\n";
        }
        return out;
    }
}
