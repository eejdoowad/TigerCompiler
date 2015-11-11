// The Compiler class should be the top-level Entity
// It will be composed of the various phases of the compiler

import AST.IRGenVisitor;
import IR.IRGenerator;

public class Compiler {

    public Compiler(){
        if (Config.DEBUG && Config.DEBUG_INIT){
            System.out.println("Compiler initialized");
        }
    }

    // Compiles the specified tiger file
    // Doesn't do any error checking regarding the tiger file
    // So error check the file before calling compile
    public void compile(String file){
        TigerScanner scanner = new TigerScanner(file);
        TigerParser parser = new TigerParser(scanner);
        parser.parse();
        if (parser.isParseSuccess()) {
            IRGenerator irgen = new IRGenerator(parser.program);
            irgen.generate();
        }
    }
}
