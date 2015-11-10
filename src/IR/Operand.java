package IR;

// Class Hierarchy:
// Operand
//    Var
//        NamedVar
//        TempVar
//    Immediate
//        IntImmediate
//        FloatImmediate
//    LabelOp

public abstract class Operand {
    public abstract String toString();
}
