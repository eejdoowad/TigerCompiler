package MIPSGenerator;

import IR.IR;

import java.util.ArrayList;
import java.util.HashMap;

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
        HashMap<String, String> codeData = new HashMap<String, String>();
       ArrayList<String> varBlock = new ArrayList<String>();
       ArrayList<String> mipsInst = new ArrayList<String>();
       mipsInst.add(".text");
        for (int i = 0; i < v.assemblyHelp.size(); i ++) {
            AssemblyHelper instruction = v.assemblyHelp.get(i);
            
            String[] parameters = instruction.getParameters();
            // parameters[0] is destination register, parameters[1] is source register, parameters[2] is source register or immediate value
            switch (instruction.getOpcode()) {
            	case "add":
            		mipsInst.add("add " + parameters[0] + ", " + parameters[1] + ", " + parameters[2]);
            		break;
                case "and":
                    mipsInst.add("and " + parameters[0] + ", " + parameters[1] + ", " + parameters[2]);
                    break;            		
                case "array_assign":
                    int size = Integer.parseInt(parameters[1]);
                    codeData.put(parameters[0], ".space " + size * 4);
                    break;            	
                case "array_load":
                    mipsInst.add("la " + parameters[0] + ", " + parameters[1]);
                    mipsInst.add("add " + parameters[0] + ", " + parameters[0] + ", " + parameters[2]);
                    mipsInst.add("lw " + parameters[0] + " 0(" + parameters[0] + ")");
                    break;
                case "array_store":
                    String[] paramSplitArray = parameters[0].split("\\|");
                    mipsInst.add("la " + paramSplitArray[0] + ", " + paramSplitArray[1]);
                    mipsInst.add("add " + paramSplitArray[0] + ", " + paramSplitArray[0] + ", " + parameters[1]);
                    mipsInst.add("sw " + parameters[2] + " 0(" + paramSplitArray[0] + ")");
                    break;
                case "assign":
                    mipsInst.add("add " + parameters[0] + ", " + parameters[1] + " 0");
                    break;
                case "breq":
                    mipsInst.add("beq " + parameters[0] + ", " + parameters[1] + ", " + parameters[2]);
                    break;
                case "bregeq":
                    mipsInst.add("bne " + parameters[0] + ", " + parameters[1] + ", " + parameters[2]);
                    break;     
                case "brgt":
                    mipsInst.add("bgt " + parameters[0] + ", " + parameters[1] + ", " + parameters[2]);
                    break;
                case "brleq":
                    mipsInst.add("ble " + parameters[0] + ", " + parameters[1] + ", " + parameters[2]);
                    break;
                case "brlt":
                    mipsInst.add("blt " + parameters[0] + ", " + parameters[1] + ", " + parameters[2]);
                    break;
                case "brgeq":
                    mipsInst.add("bge " + parameters[0] + ", " + parameters[1] + ", " + parameters[2]);
                    break;
                case "call":
                	// TODO 
                    break;
                case "callr":
                	// TODO 
                    break;   
                case "div":
                    mipsInst.add("div " + parameters[0] + ", " + parameters[1] + ", " + parameters[2]);
                    break;
                case "goTo":
                    // parameters[0] is goto label
                    mipsInst.add("b " + parameters[0]);
                    break;                    
                case "mult":
                    mipsInst.add("mul " + parameters[0] + ", " + parameters[1] + ", " + parameters[2]);
                    break;
                case "or":
                    mipsInst.add("or " + parameters[0] + ", " + parameters[1] + ", " + parameters[2]);
                    break;
                case "ret":
                	// TODO 
                    break;
                case "sub":
                    mipsInst.add("sub " + parameters[0] + ", " + parameters[1] + ", " + parameters[2]);
                    break;
                case "lw":
                    mipsInst.add("lw " + parameters[0] + ", " + parameters[1]);
                    break;
                case "sw":
                    codeData.put(parameters[1], ".space 4");
                    mipsInst.add("sw " + parameters[0] + ", " + parameters[1]);
                    break;
                case "label":
                    mipsInst.add(parameters[0] + ":" );
                    break;
                default:
                	break;
            }
        }
        mipsInst.add("jr $ra");
        varBlock.add(".data");
        for (String var : codeData.keySet()) {
            varBlock.add(var + ": " + codeData.get(var));
        }
        ArrayList<String> finalMips = new ArrayList<String>();
        finalMips.addAll(varBlock);
        finalMips.addAll(mipsInst);
        
       // return  finalMips;
      

        for (String s : finalMips){
           System.out.println(s);
        }

        return finalMips;
    }

}
