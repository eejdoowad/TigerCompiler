package IRGenerator;

import AST.ASTRoot;
import java.util.ArrayList;
import IR.IR;
import IR.Label;

public class IRGen {

    // AST representation output by parser
    public AST.ASTRoot ast;
    private ArrayList<IR> instructions;


    public IRGen(AST.ASTRoot ast){
        this.ast = ast;
    }

    // generates IR code using the AST
    public ArrayList<IR> generate(){
        IRGenVisitor generator = new IRGenVisitor(ast);
        instructions = generator.generateIR();
        removeRedundantLabels();

        System.out.println(this.toString());

        return instructions;
    }

    // if there are multiple consecutive labels,
    // removes all but the first
    public void removeRedundantLabels(){
        // Two passes:
        // 1. Find consecutive labels
        // 2. Replace them, modifying instruction operands accordingly



    }



    public String toString(){
        String out = "";
        for (IR i : instructions){
            out += ((i instanceof Label) ? "" : "\t") + i.toString() + "\n";
        }
        return out;
    }
}
