package IR;

import AST.IRGenVisitor;
import AST.Program;

import java.util.ArrayList;

public class IRGenerator {

    // AST representation output by parser
    public Program program;

    // IROC representation output by IRGenerator
    public ArrayList<IR> instructions = new ArrayList<>();


    public IRGenerator(Program program){
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
        for (IR i : instructions){
            out += i.toString() + "\n";
        }
        return out;
    }
}
