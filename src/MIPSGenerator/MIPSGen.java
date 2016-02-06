package MIPSGenerator;

import IR.IR;

import java.util.ArrayList;

public class MIPSGen {

    private MIPSGen(){}

    public static String generate(ArrayList<IR> instructions){

        FunctionSetupVisitor setup = new FunctionSetupVisitor();
        MIPSGenVisitor v = new MIPSGenVisitor();

        StringBuilder assembly = new StringBuilder();

        if (instructions != null){
            // Perform analysis on functions before generating the code
            for (IR inst : instructions) {
                inst.accept(setup);
            }
            for (IR inst : instructions){
                inst.accept(v);
            }
        }

        assembly.append(".text\n");

        // Generate printi
        assembly.append("_printi:\n");
        assembly.append("li $v0, 1\n");
        assembly.append("syscall\n");
        assembly.append("jal $ra\n");

        // Generate printf
        assembly.append("_printf:\n");
        assembly.append("swc1 $f12, 0($sp)\n");
        assembly.append("mtc1 $a0, $f12\n");
        assembly.append("li $v0, 2\n");
        assembly.append("syscall\n");
        assembly.append("lwc1 $f12, 0($sp)\n");
        assembly.append("jal $ra\n");

        // Generate actual assembly
        for (AssemblyHelper s : v.assemblyHelp){
            if (s.getOpcode().equals("main:")) {
                assembly.append(".globl main\n");
            }
            assembly.append(s.toString() + "\n");
            if (s.getOpcode().equals("main:")) {
                assembly.append("sw $ra, 0($sp)\n");
                assembly.append("sub $sp, $sp, 4\n");
            }
        }
        assembly.append("add $sp, $sp, 4\n");
        assembly.append("lw $ra, 0($sp)\n");
        assembly.append("jal $ra\n");
        assembly.append("\n.data\n");
        for (String s : v.dataSection.keySet()) {
            if (s != null) {
                String entry = s + ": ";
                int size = v.dataSection.get(s);
                if (size > 1) {
                    entry = entry + ".space " + size*4 + "\n";
                } else {
                    entry = entry + ".word 0\n";
                }
                assembly.append(entry);
            }
        }
        return assembly.toString();
    }

}
