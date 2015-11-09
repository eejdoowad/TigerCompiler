package IR;

// Class Hierarchy:
// Operand
//    Var
//        NamedVar
//        TempVar
//    Immediate
//        IntImmediate
//        FloatImmediate

public abstract class Operand {
    public abstract String toString();
}
