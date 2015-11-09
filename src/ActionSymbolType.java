

public enum ActionSymbolType {
    START ("#start"),
    END ("#end"),


    SEMA_VAR_DEC ("#semaVarDec"),
    SEMA_TYPE_DEC("#semaTypeDec"),

    // New semantic action identifiers
    SEMA_IDENTIFIER ("#semaIdentifier"),
    SEMA_INT_LIT ("#semaIntLit"),
    SEMA_FLOAT_LIT ("#semaFloatLit"),
    SEMA_ARRAY_TYPE ("#semaArrayType"),

    // Sufyan SEMA additions... might as well attach them to root
    SEMA_ATTACH_TYPEDEC("#semaAttachTypeDec"),
    SEMA_ATTACH_VARDEC("#semaAttachVarDec"),
    SEMA_ATTACH_FUNDEC("#semaAttachFunDec"),

    SEMA_VAR_REF ("#semaVarRef"),
    SEMA_VAR_REF_INDEX ("#semaVarRefIndex"),
    SEMA_VAR_REF_ARRAY_CHECK ("#semaVarRefArrayCheck"),
    SEMA_ASSIGN ("#semaAssign"),

    // Logical binops
    SEMA_OR ("#semaOr"),
    SEMA_AND ("#semaAnd"),

    // Conditional binops
    SEMA_GREATER ("#semaGreater"),
    SEMA_LESSER ("#semaLesser"),
    SEMA_GREATEREQ ("#semaGreaterEq"),
    SEMA_LESSEREQ ("#semaLesserEq"),
    SEMA_EQ ("#semaEq"),
    SEMA_NEQ ("#semaNeq"),

    // Arithmetic binops
    SEMA_PLUS ("#semaPlus"),
    SEMA_MINUS ("#semaMinus"),
    SEMA_MULT ("#semaMult"),
    SEMA_DIV ("#semaDiv"),

    // If statement actions
    SEMA_IF_START ("#semaIfStart"),
    SEMA_IF_BLOCK ("#semaIfBlock"),
    SEMA_ELSE_START ("#semaElseStart"),
    SEMA_ELSE_BLOCK ("#semaElseBlock"),

    // While statement actions
    SEMA_WHILE_START ("#semaWhileStart"),
    SEMA_WHILE_BLOCK ("#semaWhileBlock"),

    // For statement actions
    SEMA_FOR_START ("#semaForStart"),
    SEMA_FOR_BLOCK ("#semaForBlock"),

    // Break
    SEMA_BREAK ("#semaBreak"),

    // Functions
    SEMA_FUNC_START ("#semaFuncStart"),
    SEMA_FUNC_ARGS ("#semaFuncArgs"),
    SEMA_FUNC_RET ("#semaFuncRet"),
    SEMA_FUNC_BLOCK ("#semaFuncBlock"),
    SEMA_RETURN ("#semaReturn"),
    SEMA_PROC_CALL ("#semaProcCall"),
    SEMA_FUNC_CALL ("#semaFuncCall");

    private final String name;

    private ActionSymbolType(String s) {
        name = s;
    }

    @Override
    public String toString() {
        return this.name;
    }
}



//Thoughts... you don't attach a subtree until the subtree is fully constructed
//        And you attach it as soon as it is fully constructed
