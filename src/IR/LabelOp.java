package IR;

import SemanticAnalyzer.SemanticSymbol;

public class LabelOp extends Operand {

    public Label label;

    public LabelOp(Label label){
        isInteger = true;
        this.label = label;
    }

    public String toString(){
        return label.name;
    }

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "LabelOp";
	}
}
