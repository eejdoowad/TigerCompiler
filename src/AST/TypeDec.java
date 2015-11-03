package AST;

public class TypeDec extends Node {

    ID newType;
    ID existingType;
    IntLit dim; // will be Null on NonArray Types
}
