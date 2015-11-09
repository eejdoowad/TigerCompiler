package AST;

// So this is the root node

import java.util.ArrayList;

public class Program extends Node {

    public ArrayList<TypeDec> typeDecs = new ArrayList<TypeDec>();
    public ArrayList<VarDec> varDecs = new ArrayList<VarDec>();
    public ArrayList<FunDec> funDecs = new ArrayList<FunDec>();
    public ArrayList<Stat> stats = new ArrayList<Stat>();

    public void accept(Visitor v) { v.visit(this); }
}
