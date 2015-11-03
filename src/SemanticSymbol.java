/**
 * Base class for a semantic symbol that can be entered into the
 * symbol table
 */
public class SemanticSymbol {
    // enum of symbol classes
    public enum SymbolClass {
        TypeDecleration,
        VarDeclaration,
        FunctionDeclatation,
    }

    // Type of symbol
    private SymbolClass symClass;

    // Name of symbol
    private String name;

    public SemanticSymbol(String name, SymbolClass symClass) {
        this.name = name;
        this.symClass = symClass;
    }

    // Typing specifics
    public enum SymbolType {
        SymbolInt,
        SymbolFloat,
        SymbolCustom,
    }

    // The type that this symbol is aliasing/storing/returning
    private SymbolType type;

    // Reference to semantic symbol if a custom alias
    private SemanticSymbol typeSymbol;

    public void setSymbolType(SymbolType type) {
        this.type = type;
    }

    public void setSymbolType(SemanticSymbol typeSymbol) {
        this.type = SymbolType.SymbolCustom;
        this.typeSymbol = typeSymbol;
    }

    public SymbolType getSymbolType() {
        return type;
    }

    public SemanticSymbol getSymbolTypeReference() {
        return typeSymbol;
    }
}
