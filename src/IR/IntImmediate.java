package IR;

import SemanticAnalyzer.SemanticSymbol;

public class IntImmediate extends Immediate {

    public int val;

    public IntImmediate(int val){
        isInteger = true;
        this.val = val;
    }
    public String toString(){
        return Integer.toString(val);
    }
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "int";
	}
}
