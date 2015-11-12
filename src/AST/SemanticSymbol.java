package AST;

import java.util.ArrayList;

/**
 * Base class for a semantic symbol that can be entered into the
 * symbol table
 */
public class SemanticSymbol {
    // enum of symbol classes
    public enum SymbolClass {
        TypeDecleration ("Type"),
        VarDeclaration ("Variable"),
        FunctionDeclatation ("Function");

        private String name;
        private SymbolClass(String text) {
            name = text;
        }

        public String toString() {
            return name;
        }
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
        SymbolInt ("int"),
        SymbolFloat ("float"),
        SymbolCustom ("custom"),
        SymbolError ("error");

        private String name;
        private SymbolType(String text) {
            name = text;
        }

        public String toString() {
            return name;
        }
    }

    // The type that this symbol is aliasing/storing/returning
    private SymbolType type;

    // Reference to semantic symbol if a custom alias
    private SemanticSymbol typeSymbol;

    // Array size (0 means not an array)
    private int arraySize = 0;

    // Function parameters
    private ArrayList<SemanticSymbol> functionParameters;
    private SemanticSymbol functionReturnType;

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

    public boolean isIntPrimitive() { return getInferredPrimitive() == SemanticSymbol.SymbolType.SymbolInt; }
    public boolean isFloatPrimitive() { return getInferredPrimitive() == SymbolType.SymbolFloat; }

    public boolean isArray(){
        return getArraySize() > 0;
    }

    public void setFunctionParameters(ArrayList<SemanticSymbol> parameters) {
        functionParameters = parameters;
    }

    public ArrayList<SemanticSymbol> getFunctionParameters() {
        return functionParameters;
    }

    public void setFunctionReturnType(SemanticSymbol type) {
        functionReturnType = type;
    }

    public SemanticSymbol getFunctionReturnType() {
        return functionReturnType;
    }

    public String toString() {
        String ret = "Symbol: " + name + "\n" +
                "\tClass: " + symClass + "\n";
        if (symClass == SymbolClass.TypeDecleration) {
            ret += "\tBase type: ";
            if (type == SymbolType.SymbolCustom) {
                ret += typeSymbol.getName();
            } else {
                ret += type;
            }
            if (arraySize > 0) {
                ret += "\n\tArray Size: " + arraySize;
            }
        } else if (symClass == SymbolClass.VarDeclaration) {
            ret += "\tType: " + typeSymbol.getName();
        } else {
            if (functionReturnType != null) {
                ret += "\tReturn type: " + functionReturnType.getName();
            } else {
                ret += "\tReturn type: void";
            }
            if (functionParameters != null) {
                ret += "\n\tParameters:";
                for (SemanticSymbol param : functionParameters) {
                    ret += "\n\t\t" + param.getName() + " : " + param.getSymbolTypeReference().getName();
                }
            }
        }
        return ret;
    }
}
