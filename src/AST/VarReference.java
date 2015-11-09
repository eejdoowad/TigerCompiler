package AST;

// ID should not appear in the final AST at all. It is
// just a temporary pushed by the parser before the
// semantic analyzer does a lookup

//  So a + b would have a plus node
// with two VarReference children

public class VarReference extends Expr {
    // Reference to variable symbol that this variable represents
    public SemanticSymbol reference;

    // Expression for index into if one exists
    public Expr index;

    public void accept(Visitor v) { v.visit(this); }
}
