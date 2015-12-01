package IR;

import SemanticAnalyzer.SemanticSymbol;

public class TempVar extends Var {
    public static int num = 0;
    public int id;

    // default to int
    private TempVar(){
        this.type = SemanticSymbol.SymbolType.SymbolInt;
        id = num++;
        name = ((this.type == SemanticSymbol.SymbolType.SymbolInt) ? "$t" : "$f") + id;
    }
    private TempVar(SemanticSymbol.SymbolType type){
        this.type = type;
        id = num++;
        name = ((this.type == SemanticSymbol.SymbolType.SymbolInt) ? "$t" : "$f") + id;
    }
    private TempVar(Operand l, Operand r){
        if (l.type == SemanticSymbol.SymbolType.SymbolFloat
                || r.type == SemanticSymbol.SymbolType.SymbolFloat){
            this.type = SemanticSymbol.SymbolType.SymbolFloat;
        }
        else{
            this.type = SemanticSymbol.SymbolType.SymbolInt;
        }
        id = num++;
        name = ((this.type == SemanticSymbol.SymbolType.SymbolInt) ? "$t" : "$f") + id;
    }


    public static TempVar generateTempVar(){
        TempVar var = new TempVar();
        getVars().add(var);
        getNames().put(var.name, var);
        return var;
    }
    public static TempVar generateTempVar(SemanticSymbol.SymbolType type){
        TempVar var = new TempVar(type);
        getVars().add(var);
        getNames().put(var.name, var);
        return var;
    }
    public static TempVar generateTempVar(Operand l, Operand r){
        TempVar var = new TempVar(l, r);
        getVars().add(var);
        getNames().put(var.name, var);
        return var;
    }

    public String toString(){
        return name;
    }
    @Override
	public String getType() {
		// TODO Auto-generated method stub
		return "temp";
    }
    
}
