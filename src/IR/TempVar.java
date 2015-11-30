package IR;

import SemanticAnalyzer.SemanticSymbol;

public class TempVar extends Var {
    public static int num = 0;
    public int id;

    public TempVar(Operand l, Operand r){
        if (l.type == SemanticSymbol.SymbolType.SymbolFloat
                || r.type == SemanticSymbol.SymbolType.SymbolFloat){
            this.type = SemanticSymbol.SymbolType.SymbolFloat;
        }
        else{
            this.type = SemanticSymbol.SymbolType.SymbolInt;
        }
        id = num++;
    }
    public TempVar(SemanticSymbol.SymbolType type){
        this.type = type;
        id = num++;
    }

    // default to int
    public TempVar(){
        this.type = SemanticSymbol.SymbolType.SymbolInt;
        id = num++;
    }

    public String toString(){
        return ((type == SemanticSymbol.SymbolType.SymbolInt) ? "$t" : "$f") + id;
    }
}
