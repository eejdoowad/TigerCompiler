package MIPSGenerator;

public class AssemblyHelper {

	private String opcode;
	private String parameter1, parameter2, parameter3;
	private final int NUMPARAMS = 3;

	public AssemblyHelper(String opcode, String param1, String param2, String param3) {
		this.opcode = opcode;
		this.parameter1 = param1;
		this.parameter2 = param2;
		this.parameter3 = param3;
	}
	public String getOpcode() {
		return opcode;
	}
    public String[] getParameters() {
        String[] s = new String[NUMPARAMS];
        s[0] = parameter1;
        s[1] = parameter2;
        s[2] = parameter3;
        return s;
    }
	public String toString() {
		String ret = opcode;
		if (!parameter1.equals("")) {
			ret = ret + " " + parameter1;
		}
		if (!parameter2.equals("")) {
			ret = ret + ", " + parameter2;
		}
		if (!parameter3.equals("")) {
			ret = ret + ", " + parameter3;
		}
		return ret;
	}
	
	
}
