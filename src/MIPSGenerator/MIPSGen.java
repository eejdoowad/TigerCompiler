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

        System.out.println(".text");
        for (AssemblyHelper s : v.assemblyHelp){
            System.out.println(s);
        }
        System.out.println("\n.data");
        for (String s : v.dataSection.keySet()) {
            String entry = s + ": ";
            int size = v.dataSection.get(s);
            if (size > 1) {
                entry = entry + ".space " + size;
            } else {
                entry = entry + ".word 0";
            }
            System.out.println(entry);
        }

     //   return v.mips;
        return null;
    }

}
