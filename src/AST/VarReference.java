package AST;

public class VarReference extends Expr {
    // Reference to variable symbol that this variable represents
    public SemanticSymbol reference;

    // Expression for index into if one exists
    public Expr index;
}
