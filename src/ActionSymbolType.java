

public enum ActionSymbolType {
    START ("#start"),
    END ("#end"),

    // New semantic action identifiers
    SEMA_VAR_DEC ("#semaVarDec"),
    SEMA_IDENTIFIER ("#semaIdentifier"),
    SEMA_INT_LIT ("#semaIntLit"),
    SEMA_FLOAT_LIT ("#semaFloatLit"),
    SEMA_ARRAY_TYPE ("#semaArrayType"),

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
