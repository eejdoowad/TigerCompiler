package IR;

import SemanticAnalyzer.SemanticSymbol;

public class IntImmediate extends Immediate {

    public int val;

    public IntImmediate(int val){
        this.type = SemanticSymbol.SymbolType.SymbolInt;
        this.val = val;
    }
    public String toString(){
        return Integer.toString(val);
    }
}
