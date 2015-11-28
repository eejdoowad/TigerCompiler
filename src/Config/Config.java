package Config;


public class Config {
    public static boolean DEBUG = true;                         // Enable or disable all debugging, HIGHEST PRIORITY
    public static boolean DEBUG_INIT = false;                   // Prompt when modules successfully initialize
    public static boolean DEBUG_GRAMMAR = false;
    public static boolean DEBUG_PARSETABLE = false;
    public static boolean DEBUG_PARSER1 = false;                 // Enable for the debugging messages specified in the project report
    public static boolean DEBUG_PARSER2 = false;
    public static boolean DEBUG_SEMACTS = false;                 // Enable to debug semantic actions for generating parse tree
    public static boolean DEBUG_IRCODEGEN = false;               // Enable to debug intermediate code generation
    public static String PARSE_TABLE_PATH = "ParseTable.csv";   // Specify the parse table file path
    public static String GRAMMAR_PATH = "Agrammar.txt";         // Specify the grammar file path
    public static String EPSILON = "EPSILON";                   // Specify the string used to denote epsilon in the grammar
}

