package IR;

import SemanticAnalyzer.SemanticSymbol;

public class LabelOp extends Operand {

    public Label label;

    public LabelOp(Label label){
        this.type = SemanticSymbol.SymbolType.SymbolInt;
        this.label = label;
    }

    public String toString(){
        return label.name;
    }
}
