package AST;

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

    // Array size (0 means not an array)
    private int arraySize = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SymbolClass getSymbolClass() {
        return symClass;
    }

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

    // Returns the primitive that this symbol is derived from
    public SymbolType getInferredPrimitive() {
        SemanticSymbol iter = this;
        while (iter.type == SymbolType.SymbolCustom) {
            iter = iter.typeSymbol;
        }
        return iter.type;
    }

    public SemanticSymbol getSymbolTypeReference() {
        return typeSymbol;
    }

    public void setArraySize(int size) {
        arraySize = size;
    }

    public int getArraySize() {
        // Get inferred array size
        SemanticSymbol iter = this;
        int size = arraySize;
        while (iter.type == SymbolType.SymbolCustom && size <= 0) {
            iter = iter.typeSymbol;
            size = iter.arraySize;
        }
        return size;
    }

    public boolean isArray(){
        return arraySize > 0;
    }
}
