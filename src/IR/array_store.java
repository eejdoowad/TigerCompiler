package IR;

import AST.SemanticSymbol;

public class array_store extends IR {

    // arr[0] := a
    SemanticSymbol array;
    SemanticSymbol index;
    SemanticSymbol right;

    public array_store(SemanticSymbol array, SemanticSymbol index, SemanticSymbol right){
        this.array = array;
        this.index = index;
        this.right = right;
    }

    public String toString(){
        return "array_store, " + array.getName() + ", " + index.getName() + ", " + right.getName();
    }
}
