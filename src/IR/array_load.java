package IR;

import AST.SemanticSymbol;

public class array_load extends IR {

    // a := arr[0]
    SemanticSymbol left;
    SemanticSymbol array;
    SemanticSymbol index;

    public array_load(SemanticSymbol left, SemanticSymbol array, SemanticSymbol index){
        this.left = left;
        this.array = array;
        this.index = index;
    }

    public String toString(){
        return "array_load, " + left.getName() + ", " + array.getName() + ", " + index.getName();
    }
}
