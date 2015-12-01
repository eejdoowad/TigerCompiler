package MIPSGenerator;

import IR.IR;

import java.util.ArrayList;

public class MIPSGen {

    private MIPSGen(){}

    public static ArrayList<String> generate(ArrayList<IR> instructions){

        System.out.println("\nGENERATING MIPS CODE:\n");

        MIPSGenVisitor v = new MIPSGenVisitor();

        if (instructions != null){
            for (IR inst : instructions){
                inst.accept(v);
            }
        }


       for (AssemblyHelper s : v.assemblyHelp){
            System.out.println(s);
       }

     //   return v.mips;
        return null;
    }

}
