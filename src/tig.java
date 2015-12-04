import Config.Config;
import IRGenerator.IRGen;
import MIPSGenerator.*;
import MIPSGenerator.MIPSGen;
import Parser.Parser;
import Parser.TigerScanner;
import AST.ASTRoot;
import IR.*;
import RegisterAllocator.RegAllocator;
import Util.Util;

import java.io.PrintWriter;
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
        if (args.length != 2){
            System.out.println("ERROR; USAGE: java tig [-n or -i or -g based on allocation algorithm] input.tiger");
            System.out.println("First arg should be -n or -i or -g based on allocation algorithm");
            System.out.println("-n(aive) -i(ntrablock) -g(lobal)");
            System.out.println("Example: java tig -i test.tiger");
        } else if (!Util.getFileExtension(args[1]).equals("tiger")){
            System.out.println("Input file must have .tiger extension");
        } else if (!Util.fileExists(args[1])) {
            System.out.println(args[1] + " does not exist");
        } else {

            boolean selected = false;

            if (args[0].equals("-n") || args[0].equals("n") || args[0].equals("-naive")){
                Config.REG_ALLOCATOR = Config.RegAllocator.NAIVE;
                selected = true;
            } else if (args[0].equals("-i") || args[0].equals("i") || args[0].equals("-intrablock")){
                Config.REG_ALLOCATOR = Config.RegAllocator.INTRABLOCK;
                selected = true;
            } else if (args[0].equals("-g") || args[0].equals("g") || args[0].equals("-global")){
                Config.REG_ALLOCATOR = Config.RegAllocator.GLOBAL;
                selected = true;
            } else {
                System.out.println("ERROR; USAGE: java tig [-n or -i or -g based on allocation algorithm] input.tiger");
                System.out.println("First arg should be -n or -i or -g based on allocation algorithm");
                System.out.println("-n(aive) -i(ntrablock) -g(lobal)");
                System.out.println("Example: java tig -i test.tiger");
            }
            if (selected)
                compile(args[1]);
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
        System.out.println(".text");
        for (IR i : instructions2) {
            if (i instanceof Label)
                System.out.println("" + i);
            else
                System.out.println(i);
        }

        String mipscode = MIPSGen.generate(instructions2);

        String outfile = file.replace(".tiger", ".s");
        try {
            PrintWriter writer = new PrintWriter(outfile, "UTF-8");
            writer.println(mipscode);
            writer.close();
        }
        catch (Exception e){
            System.out.println("ERROR writing to " + outfile);
            System.out.println(e);
        }




    }
}
