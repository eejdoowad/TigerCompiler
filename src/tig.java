import Config.Config;
import IRGenerator.IRGen;
import MIPSGenerator.*;
import MIPSGenerator.MIPSGen;
import Parser.Parser;
import Parser.TigerScanner;
import AST.ASTRoot;
import IR.IR;
import RegisterAllocator.RegAllocator;
import Util.Util;
import java.util.ArrayList;


// The Compiler class should be the top-level Entity
// It will be composed of the various phases of the compiler

public class tig {

    public static void main(String[] args){

        // Fail immediately if grammar and parse table aren't located
        boolean noGrammar = !Util.fileExists(Config.GRAMMAR_PATH);
        boolean noParseTable = !Util.fileExists(Config.PARSE_TABLE_PATH);
        if (noGrammar || noParseTable){
            System.out.println("Working Directory: " + System.getProperty("user.dir"));
            if (noGrammar)
                System.out.println("Failure, Grammar not at: " + Config.GRAMMAR_PATH);
            if (noParseTable)
                System.out.println("Failure, Parse Table not at: " + Config.PARSE_TABLE_PATH);
            System.exit(1);
        }

        //
        if (args.length != 1){
            System.out.println("Provide exactly one input tiger file for compilation");
            System.out.println("USAGE: java tig input.tiger");
        } else if (!Util.getFileExtension(args[0]).equals("tiger")){
            System.out.println("Input file must have .tiger extension");
        } else if (!Util.fileExists(args[0])) {
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
        ASTRoot ast = parser.parse();

        IRGen irgen = new IRGen(ast);
        ArrayList<IR> instructions1 = irgen.generate();

        ArrayList<IR> instructions2 = RegAllocator.allocate(instructions1);
        for (IR i : instructions2) {
            System.out.println(i);
        }

        ArrayList<String> output = MIPSGen.generate(instructions2);


    }
}
