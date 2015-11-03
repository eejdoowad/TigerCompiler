package AST;

import java.util.ArrayList;

public class FunDec extends Node {

    public ID name;
    public ArrayList<Param> params = new ArrayList<Param>();
    public ID retType; // Not sure about this

}
