

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

    // eh.. review
    P_TYPEDEC ("#p-typeDec"),
    B_NEWTYPE ("#b-newType"),
    P_ADDTYPE ("#p-addType"),
    P_NEWTYPEID ("#p-newTypeID"),
    P_NEWTYPEDIM ("#p-newTypeDim"),


    P_VARDEC("#p-varDec"),
    A_VARDEC("#a-varDec"),
    B_VARID("#b-varid"),
    B_CONSTINIT ("#b-constinit"),
    P_ASSIGNSTAT ("#p-assignstat"),


    P_IF ("#p-if"),
    A_IF ("#a-if"),
    P_ID ("#p-ID"),
    P_ASSIGN ("#p-assign"),
    B_CONST ("#b-const");


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
