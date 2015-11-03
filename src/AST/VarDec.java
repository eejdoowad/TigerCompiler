package AST;

import java.util.ArrayList;

public class VarDec extends Node {

    public ArrayList<SemanticSymbol> vars = new ArrayList<>();
    public SemanticSymbol type;
    public Const init;
}
