package IR;

import AST.SemanticSymbol;

import java.util.ArrayList;

public class callr extends IR {

    public SemanticSymbol fun;
    public SemanticSymbol retVal;
    public ArrayList<SemanticSymbol> args;

    public callr(SemanticSymbol fun, SemanticSymbol retVal, ArrayList<SemanticSymbol> args){
        this.fun = fun;
        this.retVal = retVal;
        this.args = args;
    }
    public String toString(){
        String out = "call, " + fun.getName() + ", " + retVal.getName();
        for (SemanticSymbol arg : args){
            out += (", " + arg.getName());
        }
        return out;
    }
}
