package MIPSGenerator;

import IR.IR;

import java.util.ArrayList;

public class MIPSGen {

    private MIPSGen(){}

    public static ArrayList<String> generate(ArrayList<IR> instructions){

        MIPSGenVisitor v = new MIPSGenVisitor(instructions);
        for (IR inst : instructions){
            inst.accept(v);
        }

        return v.mips;
    }

}
