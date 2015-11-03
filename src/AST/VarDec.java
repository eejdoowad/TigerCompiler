package AST;

import java.util.ArrayList;

public class VarDec extends Node {

    public ArrayList<ID> IDs = new ArrayList<ID>();
    public ID type; // should it be ID?
    public Const init;
}
