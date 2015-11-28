import Config.Config;
import IRGenerator.IRGen;
import Parser.Parser;
import Parser.TigerScanner;
import Util.Util;

import java.io.File;


// The Compiler class should be the top-level Entity
// It will be composed of the various phases of the compiler

public class tig {

    public static void main(String[] args){
        if (Config.DEBUG){
            System.out.println("Working Directory = " + System.getProperty("user.dir"));
        }
        if (args.length != 1){
            System.out.println("Provide exactly one input tiger file for compilation");
            System.out.println("USAGE: java Main input.tiger");
        } else if (!Util.getFileExtension(args[0]).equals("tiger")){
            System.out.println("Input file must have .tiger extension");
        } else if (!(new File(args[0]).exists())) {
            System.out.println(args[0] + " does not exist");
        } else {
            compile(args[0]);
        }
    }

    // Compiles the specified tiger file
    // Doesn't do any error checking regarding the tiger file
    // So error check the file before calling compile
    public static void compile(String file){
        TigerScanner scanner = new TigerScanner(file);
        Parser parser = new Parser(scanner);
        parser.parse();
        if (parser.isParseSuccess() && scanner.success) {
            IRGen irgen = new IRGen(parser.program);
            irgen.generate();
        }
    }
}
