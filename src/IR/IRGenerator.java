package IR;

import AST.IRGenVisitor;
import AST.Program;

import java.util.ArrayList;

public class IRGenerator {

    // AST representation output by parser
    Program program;

    // IROC representation output by IRGenerator
    public ArrayList<IR> instructions = new ArrayList<>();


    public void emit(IR instruction){
        instructions.add(instruction);
    }

    // generates IR code using the AST
    public void generate(){
        IRGenVisitor x;
    }

    public String toString(){
        String out = "";
        for (IR i : instructions){
            out += i.toString();
        }
        return out;
    }
}
