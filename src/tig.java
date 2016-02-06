import AST.ASTToString;
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

    static boolean genMIPS = true;
    static boolean genIR = false;
    static boolean genAST = false;
    static boolean printSRC = false;
    static boolean printMIPS = false;
    static boolean printIR = false;
    static boolean printAST = false;
    static boolean ASTAsSEXP = true;
    static String source = "";

    public static void main(String[] args){
        checkForDependencies();
        parseArgs(args);
        compile();
    }

    public static void checkForDependencies(){
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
    }

    public static void printHelp(){
        System.out.println("Tiger Compiler by Sufyan Dawoodjee, Ian Ewell and Anastasia Cotton.");
        System.out.println("HELP");
        System.out.println("    -h :    print help message");
        System.out.println("GENERATE FILES (defaults to mips only)");
        System.out.println("    -g=ast  :   generate AST file (.ast extension)");
        System.out.println("    -g=ir   :   generate IR file (.ir extension)");
        System.out.println("    -g=mips :   generate MIPS file (.s extension)");
        System.out.println("PRINT TO STDOUT (all off by default)");
        System.out.println("    -p=src  :   print source code");
        System.out.println("    -p=ast  :   print AST");
        System.out.println("    -p=ir   :   print IR");
        System.out.println("    -p=mips :   print MIPS");
        System.out.println("    -p=mips :   print MIPS");
        System.out.println("AST PRINT OPTIONS (for both gen-file and stdout, S-Expression default)");
        System.out.println("    -ast=sexp :   prints AST as S-Expression");
        System.out.println("    -ast=easy :   print AST in a more readable format");
        System.out.println("REGISTER ALLOCATION ALGORITHMS (defaults to intrablock, exclusive)");
        System.out.println("    -a=n    :   naive");
        System.out.println("    -a=i    :   intrablock");
        System.out.println("    -a=g    :   global");
    }

    public static void parseArgs(String[] args){
        if (args.length == 0){
            printHelp();
            System.exit(0);
        }
        for (int i = 0; i < args.length; i++){

            // switches
            if (args[i].charAt(0) == '-'){
                if (args[i].equals("-h")){
                    printHelp();
                    System.exit(0);
                }

                // generate file options
                if (args[i].equals("-g=ast")){
                    genAST = true;
                }
                if (args[i].equals("-g=ir")){
                    genIR = true;
                }
                if (args[i].equals("-g=mips")){
                    genMIPS = true;
                }
                // print options
                if (args[i].equals("-p=src")){
                    printSRC = true;
                }
                if (args[i].equals("-p=ast")){
                    printAST = true;
                }
                if (args[i].equals("-p=ir")){
                    printIR = true;
                }
                if (args[i].equals("-p=mips")){
                    printMIPS = true;
                }
                // AST Print Options
                if (args[i].equals("-ast=sexp")){
                    ASTAsSEXP = true;
                }
                if (args[i].equals("-ast=easy")) {
                    ASTAsSEXP = false;
                }
                // Register allocation options
                if (args[i].equals("-a=n")){
                    Config.REG_ALLOCATOR = Config.RegAllocator.NAIVE;
                }
                if (args[i].equals("-a=i")){
                    Config.REG_ALLOCATOR = Config.RegAllocator.INTRABLOCK;
                }
                if (args[i].equals("-a=g")){
                    Config.REG_ALLOCATOR = Config.RegAllocator.GLOBAL;
                }
            }
            else {
                // check for a tiger file
                if (!Util.getFileExtension(args[i]).equals("tiger")){
                    System.out.println("Input file \"" + args[i] +  "\" must have .tiger extension. Aborting");
                    System.exit(0);
                } else if (!Util.fileExists(args[i])) {
                    System.out.println("Input file \"" + args[i] +  "\" does not exist. Aborting");
                    System.exit(0);
                }
                else{
                    source = args[i];
                }
            }
        }
    }

    public static void compile(){

        // Parse source file and generate an AST
        TigerScanner scanner = new TigerScanner(source);
        Parser parser = new Parser(scanner);
        ASTRoot ast = parser.parse();
        if (printSRC){
            System.out.println("\n-----SOURCE START-----");
            System.out.println(Util.readFile(source));
            System.out.println("-----SOURCE END--------");
        }
        if (printAST){
            System.out.println("\n-----AST START-----");
            System.out.println(ASTToString.getTreeString(ast, ASTAsSEXP));
            System.out.println("-----AST END--------");
        }
        if (genAST){
            Util.writeFile(ASTToString.getTreeString(ast, ASTAsSEXP), source.replace(".tiger", ".ast"));
        }

        // Walk AST to generate IR code
        IRGen irgen = new IRGen(ast);
        ArrayList<IR> ir1 = irgen.generate();
        if (printIR){
            System.out.println("\n-----IR START-----");
            System.out.print(IRStreamToString(ir1));
            System.out.println("-----IR END--------");
        }
        if (genIR){
            Util.writeFile(IRStreamToString(ir1), source.replace(".tiger", ".ir"));
        }

        // Iterate through IR code to assign registers and insert loads/stores
        ArrayList<IR> ir2 = RegAllocator.allocate(ir1);

        // Iterate through augmented IR to generate MIPS code
        String mipscode = MIPSGen.generate(ir2);
        if (printMIPS){
            System.out.println("\n-----MIPS START-----");
            System.out.print(mipscode);
            System.out.println("-----MIPS END--------");
        }
        if (genMIPS){
            Util.writeFile(mipscode, source.replace(".tiger", ".s"));
        }
    }

    public static String IRStreamToString(ArrayList<IR> stream){
        String str = "";
        for (IR i : stream) {
            if (i instanceof Label)
                str += (i + "\n");
            else
                str += ("    " + i + "\n");
        }
        return str;
    }
}
