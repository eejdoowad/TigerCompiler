package IR;

// represents assigning a single value to every element of an array
// var X : ArrayInt := 10;


import AST.SemanticSymbol;

public class array_assign extends IR {

    SemanticSymbol array;
    SemanticSymbol val; // the value to assign to the array

    public array_assign(SemanticSymbol array, SemanticSymbol val){
        this.array = array;
        this.val = val;
    }
    public String toString(){
        return "assign, " + array.getName() + ", " +  "IAN_MAKE_ARRAYSIZE_PUBLIC"// array.arraySize()
            + ", " + val.getName();
    }
}
