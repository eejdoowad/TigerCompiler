

public enum ActionSymbolType {
    P_PROG_P_DECS ("#p-prog-p-decs"),
    A_DECS_P_STATS ("#a-decs-p-stats"),
    A_STATS ("#a-stats"),
    P_TYPES ("#p-types"),
    A_TYPES_P_VARS ("#a-types-p-vars"),
    A_VARS_P_FUNS ("#a-vars-p-funs"),
    A_FUNS ("#a-funs"),
    P_VAR("#p-var"),
    B_VARID("#b-varid"),
    B_CONSTINIT ("#b-constinit"),
    P_ASSIGNSTAT ("#p-assignstat"),

    // eh.. review
    P_TYPE ("#p-type"),
    P_NEWTYPE ("#p-newtype"),
    P_ADDTYPE ("#p-addtype"),
    P_NEWTYPEID ("#p-newtypeID"),
    P_NEWTYPEDIM ("#p-newtypedim"),

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
