package Config;


public class Config {

    public static String PARSE_TABLE_PATH = "ParseTable.csv";   // Specify the parse table file path
    // Note that the current implementation expects
    // a grammar augmented with semantic actions
    public static String GRAMMAR_PATH = "Agrammar.txt";         // Specify the grammar file path
    public static String EPSILON = "EPSILON";                   // Specify the string used to denote epsilon in the grammar

    public enum RegAllocator { NAIVE, INTRABLOCK, EBB};
    public static RegAllocator REG_ALLOCATOR = RegAllocator.INTRABLOCK;

    // If false, disables ALL debugging, HIGHEST PRIORITY
    private static boolean DEBUG_MASTER = true;

    // Modify these to enable/disable debugging
    // If you add a new DEBUG option, also add a corresponding
    // publicly usable setting down below
    private final static boolean DEBUG_INIT_S = false;             // Prompt when modules successfully initialize
    private final static boolean DEBUG_GRAMMAR_S = false;
    private final static boolean DEBUG_PARSETABLE_S = false;
    private final static boolean DEBUG_PARSER1_S = false;            // Enable for the debugging messages specified in the project report
    private final static boolean DEBUG_PARSER2_S = false;
    private final static boolean DEBUG_SEMACTS_S = false;            // Enable to debug semantic actions for generating parse tree
    private final static boolean DEBUG_SYMTABLE_S = false;
    private final static boolean DEBUG_IRCODEGEN_S = false;          // Enable to debug intermediate code generation
    private final static boolean DEBUG_REGALLOC_S = true;            // Enable to debug register allocation

    // Public exposed
    public final static boolean DEBUG = DEBUG_MASTER;
    public final static boolean DEBUG_INIT = DEBUG_INIT_S && DEBUG_MASTER;
    public final static boolean DEBUG_GRAMMAR = DEBUG_GRAMMAR_S && DEBUG_MASTER;
    public final static boolean DEBUG_PARSETABLE = DEBUG_PARSETABLE_S && DEBUG_MASTER;
    public final static boolean DEBUG_PARSER1 = DEBUG_PARSER1_S && DEBUG_MASTER;
    public final static boolean DEBUG_PARSER2 = DEBUG_PARSER2_S && DEBUG_MASTER;
    public final static boolean DEBUG_SEMACTS = DEBUG_SEMACTS_S && DEBUG_MASTER;
    public final static boolean DEBUG_SYMTABLE = DEBUG_SYMTABLE_S && DEBUG_MASTER;
    public final static boolean DEBUG_IRCODEGEN = DEBUG_IRCODEGEN_S && DEBUG_MASTER;
    public final static boolean DEBUG_REGALLOC = DEBUG_REGALLOC_S && DEBUG_MASTER;
}

