package IR;

import SemanticAnalyzer.SemanticSymbol;

public class NamedVar extends Var {

    private NamedVar(SemanticSymbol symbol){
        isInteger = (symbol.getInferredPrimitive() == SemanticSymbol.SymbolType.SymbolInt);
        this.name = symbol.getName();
        this.isLocal = symbol.isLocal();
    }

    public static NamedVar generateNamedVar(SemanticSymbol symbol) {
        NamedVar var;
        if (getNames().containsKey(symbol.getName())){
            var = (NamedVar)getNames().get(symbol.uniqueString());
        }
        else{
            var = new NamedVar(symbol);
            getVars().add(var);
            getNames().put(symbol.uniqueString(), var);
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
