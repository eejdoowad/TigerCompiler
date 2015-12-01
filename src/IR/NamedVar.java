package IR;

import SemanticAnalyzer.SemanticSymbol;

public class NamedVar extends Var {

    private NamedVar(SemanticSymbol symbol){
        isInteger = (symbol.getInferredPrimitive() == SemanticSymbol.SymbolType.SymbolInt);
        this.name = symbol.getName();
    }

    public static NamedVar generateNamedVar(SemanticSymbol symbol){
        NamedVar var;
        if (getNames().containsKey(symbol.getName())){
            var = (NamedVar)getNames().get(symbol.getName());
        }
        else{
            var = new NamedVar(symbol);
            getVars().add(var);
            getNames().put(var.name, var);
        }
        return var;
    }

    public String toString(){
        return name;
    }
    
    @Override
	public String getType() {
		// TODO Auto-generated method stub
		return "var";
    }
}
