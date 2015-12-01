package IR;

import SemanticAnalyzer.SemanticSymbol;

public abstract class TempVar extends Var {
    public static int num = 0;
    public int id;

    public static TempVar gen(SemanticSymbol.SymbolType type){
        if (type == SemanticSymbol.SymbolType.SymbolInt)
            return TempIntVar.gen();
        else
            return TempFloatVar.gen();
    }

    public static TempVar gen(Operand left, Operand right){
        if (left.isInt() && right.isInt())
            return TempIntVar.gen();
        else
            return TempFloatVar.gen();
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
