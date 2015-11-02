import java.util.*;

// Yeah... the parser
// Does what you expect it to


// Two things to note:
// The parser requires BOTH the grammar file and the parse table to execute successfully
// The grammar.txt file is used to construct a list of nonterminals and the rules
// The ParseTable.csv file is needed to... construct the parse table


public class TigerParser {

    private TigerScanner scanner;
    private Grammar grammar;
    private ParseTable parseTable;
    private Stack<Symbol> parseStack;
    private Token nextToken; // the next token returned by

    // Starts true, set to false if the parser encounters an error
    private boolean parseSuccess;

    // Used to restore parser state to last sequence point upon
    private Stack<Symbol> errorStack;


    public TigerParser(TigerScanner scanner){
        this.scanner = scanner;

        // Set up Grammar
        grammar = new Grammar();

        // Set up ParseTable
        parseTable = new ParseTable(grammar);

        // Set up Parse Stack
        parseStack = new Stack<Symbol>();

        if (Config.DEBUG && Config.DEBUG_INIT){
            System.out.println("Parser initialized");
        }
    }


    public void parse(){

        parseStack.push(grammar.nonTerminals.getByID(0)); // Push Start symbol (always at index 0)
        saveState();
        nextToken = scanner.nextToken();
        parseSuccess = true; // assume parse parseSuccess until failure

        while (nextToken.type != TokenType.ENDOFFILE){

            if (parseStack.empty()) {
                System.out.println("ERROR: Empty Parse Stack. Prefix is a valid program.. But this isn't");
                parseSuccess = false;
                break;
            }
            // Determine whether the symbol at the top of the stack is a Terminal, Nonterminal
            // or Epsilon, and dispatch appropriately
            // OFF-TOPIC RANT: OH HOW I WISH JAVA HAD PROPER DYNAMIC DISPATCH
            Symbol curSymbol = parseStack.peek();
            if      (curSymbol instanceof Terminal) processTerminal((Terminal) curSymbol);
            else if (curSymbol instanceof NonTerminal) processNonTerminal((NonTerminal)curSymbol);
            else if (curSymbol instanceof Epsilon) processEpsilon((Epsilon)curSymbol);
            else if (curSymbol instanceof ActionSymbol) processActionSymbol((ActionSymbol) curSymbol);
            else { System.out.println("WTH is the symbol?"); System.exit(1); }
        }
        System.out.println("\n" + ((parseSuccess && scanner.success) ? "successful" : "unsuccessful") + " parse");
    }

    // Processes a Terminal at the top of the parse stack
    // On parseSuccess:
    //      1. Pop parse stack
    //      2. Request next Token
    private void processTerminal(Terminal curTerminal){
        if (curTerminal.type == nextToken.type){
            if (Config.DEBUG && Config.DEBUG_PARSER2){
                System.out.println("PARSED: Matched terminal " + curTerminal);
            }
            if (Config.DEBUG && Config.DEBUG_PARSER1){
                System.out.print(curTerminal + " ");
            }
            parseStack.pop();
            if (nextToken.type.isSequencePoint()) saveState();
            nextToken = scanner.nextToken();


        } else { // ERROR

            if (Config.DEBUG && Config.DEBUG_PARSER2){
                System.out.println("ERROR: Parse stack top terminal mismatch. Discarding token.");
            }
            error();
        }
    }

    // Processes a Nonterminal at the top of the parse sack
    // On Success:
    //      1. Pop the NonTerminal from the parse stack and push its derivation onto the parse stack
    private void processNonTerminal(NonTerminal curNonTerminal){

        if (parseTable.containsRuleID(curNonTerminal.id, nextToken.type)){
            if (Config.DEBUG && Config.DEBUG_PARSER2){
                System.out.println("PARSED: Expanded NonTerminal " + curNonTerminal + " against " + nextToken);
            }
            // pop NonTerminal from stack then push symbols of its derivation onto stack in reverse order
            int nextRuleID = parseTable.getRuleID(curNonTerminal.id, nextToken.type);
            ArrayList<Symbol> derivation = grammar.rules.get(nextRuleID).derivation;
            parseStack.pop();
            for (int i = derivation.size() - 1; i >= 0; i--) {
                parseStack.push(derivation.get(i));
            }
        } else { // ERROR
            System.out.println("ERROR: No Parse Table Entry for [" + curNonTerminal.symbol + "][" + nextToken.type + "]");
            error();
        }
    }

    // processes an Epsilon at the top of the parse stack
    // Always succeeds. Always:
    //      1. Pops epsilon from the parse stack
    private void processEpsilon(Epsilon epsilon){
        if (Config.DEBUG && Config.DEBUG_PARSER2){
            System.out.println("PARSED: Popped EPSILON off stack");
        }
        parseStack.pop();
    }

    // processes an Action Symbol at the top of the parse stack
    // 1. Pops Action Symbol from top of parse stack
    private void processActionSymbol(ActionSymbol curActionSymbol){
        if (Config.DEBUG && (Config.DEBUG_PARSER2 || Config.DEBUG_SEMACTS)){
            System.out.println("ACTION SYMBOL: " + curActionSymbol);
        }

        SemanticAction(curActionSymbol);

        parseStack.pop();
    }

    private void error(){
        if (Config.DEBUG && Config.DEBUG_PARSER1){
            System.out.println("Parser error (line " + scanner.getLineNumber() +
                    "): " + scanner.getLineString() + "<---");
            System.out.println("        " + scanner.getLexeme() + " is not a valid token.");
        }
        parseSuccess = false;
        if (nextToken.type.isSequencePoint()) recoverState();
        nextToken = scanner.nextToken();
    }


    // saves the current state of the stack
    @SuppressWarnings("unchecked")
	private void saveState(){
        errorStack = (Stack<Symbol>)parseStack.clone();
    }

    @SuppressWarnings("unchecked")
	private void recoverState(){
        parseStack = (Stack<Symbol>)errorStack.clone();
    }



    // this is going to take a while...
    void SemanticAction(ActionSymbol action){
        switch (action.type){
            case P_PROG_P_DECS:
                System.out.println("HOLA");
                break;
            case A_DECS_P_STATS:

                break;
            case A_STATS:

                break;
            case P_TYPES:

                break;
            case A_TYPES_P_VARS:

                break;
            case A_VARS_P_FUNS:

                break;
            case A_FUNS:

                break;
            case P_VAR:

                break;
            case B_VARID:

                break;
            case B_CONSTINIT:

                break;
            case P_ASSIGNSTAT :

                break;
            // eh.. review
            case P_TYPE:

                break;
            case P_NEWTYPE:

                break;
            case P_ADDTYPE:

                break;
            case P_NEWTYPEID:

                break;
            case P_NEWTYPEDIM:

                break;
            case P_IF:

                break;
            case A_IF:

                break;
            case P_ID:

                break;
            case P_ASSIGN:

                break;
            case B_CONST:

                break;
        }
    }
}
