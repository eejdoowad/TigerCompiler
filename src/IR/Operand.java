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

import SemanticAnalyzer.SemanticSymbol;

public abstract class Operand {
    SemanticSymbol.SymbolType type;
    public abstract String toString();
}
