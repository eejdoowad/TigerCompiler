package IR;

// represents assigning a single value to every element of an array
// var X : ArrayInt := 10;


import AST.SemanticSymbol;

public class array_assign extends IR {

    NamedVar array;
    IntImmediate size;
    Operand val; // the value to assign to the array

    public array_assign(SemanticSymbol array, Operand val){
        // this.array = new NamedVar(array.getName());
        // this.size = new Immediate
        // this.val = val;
    }
    public String toString(){
        return "assign, " + array + ", " +  "IAN_MAKE_ARRAYSIZE_PUBLIC"// array.arraySize()
            + ", " + val;
    }
}
