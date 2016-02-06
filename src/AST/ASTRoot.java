package AST;

// So this is the root node

import java.util.ArrayList;

public class ASTRoot extends Node {

    public ArrayList<TypeDec> typeDecs = new ArrayList<TypeDec>();
    public ArrayList<VarDec> varDecs = new ArrayList<VarDec>();
    public ArrayList<FunDec> funDecs = new ArrayList<FunDec>();
    public ArrayList<Stat> stats = new ArrayList<Stat>();

    public String type(){return "Tiger";}
    public void accept(Visitor v) { v.visit(this); }
    public ArrayList<Node> children(){
        ArrayList<Node> children = new ArrayList<>();
        for (TypeDec d : typeDecs){
            children.add(d);
        }
        for (VarDec d : varDecs){
            children.add(d);
        }
        for (FunDec d : funDecs){
            children.add(d);
        }
        for (Stat s : stats){
            children.add(s);
        }
        return children;
    }
    public ArrayList<String> attr(){
        ArrayList<String> attr = new ArrayList<>();
        return attr;
    }
}
