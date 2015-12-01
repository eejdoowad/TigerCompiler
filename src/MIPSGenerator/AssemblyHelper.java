package MIPSGenerator;

public class AssemblyHelper {

	private String opcode;
	private String paremeter1, parameter2, parameter3;
	private final int NUMPARAMS = 3;

	public AssemblyHelper(String opcode, String param1, String param2, String param3) {
		this.opcode = opcode;
		this.paremeter1 = param1;
		this.parameter2 = param2;
		this.parameter3 = param3;
	}
	public String getOpcode() {
		return opcode;
	}
    public String[] getParameters() {
        String[] s = new String[NUMPARAMS];
        s[0] = paremeter1;
        s[1] = parameter2;
        s[2] = parameter3;
        return s;
    }
	public String toString() {
		return opcode + ", " + paremeter1 + ", " + parameter2 + ", " + parameter3;
	}
	
	
}
