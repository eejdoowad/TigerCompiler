package IR;

import AST.SemanticSymbol;

import java.util.ArrayList;

public class call extends IR {

    public SemanticSymbol fun;
    public ArrayList<SemanticSymbol> args;

    public call(SemanticSymbol fun, ArrayList<SemanticSymbol> args){
        this.fun = fun;
        this.args = args;
    }
    public String toString(){
        String out = "call, " + fun.getName();
        for (SemanticSymbol arg : args){
            out += (", " + arg.getName());
        }
        return out;
    }
}
