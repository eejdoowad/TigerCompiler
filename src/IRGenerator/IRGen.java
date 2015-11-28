package IRGenerator;

import AST.Program;

import java.util.ArrayList;

public class IRGen {

    // AST representation output by parser
    public Program program;

    // IROC representation output by IRGenerator
    public ArrayList<IR.IR> instructions = new ArrayList<>();


    public IRGen(Program program){
        this.program = program;
    }

    // generates IR code using the AST
    public void generate(){
        IRGenVisitor g = new IRGenVisitor();
        g.program = program;
        g.instructions = instructions;
        program.accept(g);

        System.out.println("IR CODE GENERATED:");
        System.out.println(this.toString());
    }



    public String toString(){
        String out = "";
        for (IR.IR i : instructions){
            out += ((i instanceof IR.Label) ? "" : "\t") + i.toString() + "\n";
        }
        return out;
    }
}
