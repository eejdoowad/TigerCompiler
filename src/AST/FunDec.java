package AST;

import SemanticAnalyzer.SemanticSymbol;

import java.util.ArrayList;

public class FunDec extends Node {
    public SemanticSymbol function;
    public ArrayList<Stat> stats = new ArrayList<>();

    public String type(){return "FunDec";}
    public void accept(Visitor v) { v.visit(this); }
    public ArrayList<Node> children(){
        ArrayList<Node> children = new ArrayList<>();
        children.add(function);
        if (function.getFunctionParameters() != null)
            for (SemanticSymbol arg : function.getFunctionParameters()){
                StupidNode Param = new StupidNode("Parameter");
                StupidNode argType = new StupidNode("Type", arg.getSymbolTypeReference().getName());
                Param.children.add(arg);
                Param.children.add(argType);
                children.add(Param);
            }
        if (function.getFunctionReturnType() != null)
            children.add(function.getFunctionReturnType());
        children.addAll(stats);
        return children;
    }
    public ArrayList<String> attr(){
        ArrayList<String> attr = new ArrayList<>();
        return attr;
    }
}
