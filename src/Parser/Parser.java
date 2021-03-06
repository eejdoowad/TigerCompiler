package Parser;

import Config.Config;
import java.util.*;
import AST.*;
import SemanticAnalyzer.SemanticAnalyzer;

// Yeah... the parser
// Does what you expect it to


// Two things to note:
// The parser requires BOTH the grammar file and the parse table to execute successfully
// The grammar.txt file is used to construct a list of nonterminals and the rules
// The ParseTable.csv file is needed to... construct the parse table


public class Parser {

    private TigerScanner scanner;
    private Grammar grammar;
    private ParseTable parseTable;
    private Stack<Symbol> parseStack;
    private Token nextToken; // the next token returned by
    private SemanticAnalyzer analyzer;
    private boolean doSemanticAnalysis; // used to disable analysis in case of parser error
    private ASTRoot ast;

    // Stack that ID or LIT tokens popped off the parse stack are pushed to
    // to be processed by the semantic analyzer
    private Deque<Token> tokenStack;

    // Starts true, set to false if the parser encounters an error
    private boolean parseSuccess;

    // Used to restore parser state to last sequence point upon
    private Stack<Symbol> errorStack;


    public Parser(TigerScanner scanner){
        this.scanner = scanner;

        // Set up Grammar
        grammar = new Grammar();

        // Set up ParseTable
        parseTable = new ParseTable(grammar);

        // Set up Parse Stack
        parseStack = new Stack<Symbol>();

        // Set up semantic analyzer
        analyzer = new SemanticAnalyzer();

        // Set up token stack
        tokenStack = new ArrayDeque<>();

        // Default to doing semantic analysis
        doSemanticAnalysis = true;

        if (Config.DEBUG_INIT){
            System.out.println("Parser initialized");
        }
    }


    public ASTRoot parse(){

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

        boolean success = parseSuccess && scanner.success;
        System.out.println("\n" + (success ? "successful" : "unsuccessful") + " parse");
        if (!success) System.exit(1);

        return ast;
    }

    // Processes a Terminal at the top of the parse stack
    // On parseSuccess:
    //      1. Pop parse stack
    //      2. Request next Token
    private void processTerminal(Terminal curTerminal){
        if (curTerminal.type == nextToken.type){
            if (Config.DEBUG_PARSER2){
                System.out.println("PARSED: Matched terminal " + curTerminal);
            }
            if (Config.DEBUG_PARSER1){
                System.out.print(curTerminal + " ");
            }
            parseStack.pop();
            if (nextToken.type.isSequencePoint()) saveState();
            if (nextToken.type == TokenType.ID ||
                    nextToken.type == TokenType.INTLIT ||
                    nextToken.type == TokenType.FLOATLIT ||
                    nextToken.type == TokenType.KINT ||
                    nextToken.type == TokenType.KFLOAT) {
                tokenStack.addFirst(nextToken);
                analyzer.setCurrentLine(scanner.getLineNumber());
            }
            nextToken = scanner.nextToken();


        } else { // ERROR

            if (Config.DEBUG_PARSER2){
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
            if (Config.DEBUG_PARSER2){
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
            if (Config.DEBUG_PARSER2) {
                System.out.println("ERROR: No Parse Table Entry for [" + curNonTerminal.symbol + "][" + nextToken.type + "]");
            }
            error();
        }
    }

    // processes an Epsilon at the top of the parse stack
    // Always succeeds. Always:
    //      1. Pops epsilon from the parse stack
    private void processEpsilon(Epsilon epsilon){
        if (Config.DEBUG_PARSER2){
            System.out.println("PARSED: Popped EPSILON off stack");
        }
        parseStack.pop();
    }

    // processes an Action Symbol at the top of the parse stack
    // 1. Pops Action Symbol from top of parse stack
    private void processActionSymbol(ActionSymbol curActionSymbol){
        if (Config.DEBUG_PARSER2 || Config.DEBUG_SEMACTS){
            System.out.println("ACTION SYMBOL: " + curActionSymbol);
        }

        SemanticAction(curActionSymbol);

        parseStack.pop();
    }

    private void error(){
        System.out.println("Parser error (line " + scanner.getLineNumber() +
                "): " + scanner.getLineString() + "<---");
        System.out.print("        " + scanner.getLexeme() + " is not a valid token. ");

        Symbol curSymbol = parseStack.peek();
        if (curSymbol instanceof NonTerminal){
            System.out.println("Expected \"" + parseTable.getAnExpected(((NonTerminal)curSymbol).id).toLexeme() + "\".");
        }
        else if (curSymbol instanceof Terminal){
            System.out.println("Expected \"" + ((Terminal)curSymbol).type.toLexeme() + "\".");
        }

        parseSuccess = false;
        doSemanticAnalysis = false;
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
        if (!doSemanticAnalysis) {
            return;
        }
        switch (action.type){
            case START:
                analyzer.semaProgramStart();
                break;
            case END:
                ast = analyzer.semaProgramEnd();
                break;

            case SEMA_INT_LIT:
                Token intlit = tokenStack.removeFirst();
                analyzer.semaIntLit(intlit.lexeme);
                break;
            case SEMA_FLOAT_LIT:
                Token floatlit = tokenStack.removeFirst();
                analyzer.semaFloatLit(floatlit.lexeme);
                break;
            case SEMA_IDENTIFIER:
                Token ident = tokenStack.removeFirst();
                analyzer.semaIdentifier(ident.lexeme);
                break;
            case SEMA_ARRAY_TYPE:
                analyzer.semaArrayType();
                break;

            case SEMA_VAR_DEC:
                analyzer.semaVarDeclaration();
                break;
            case SEMA_TYPE_DEC:
                analyzer.semaTypeDeclaration();
                break;
            case SEMA_VAR_REF:
                analyzer.semaVariableReference(tokenStack.removeFirst().lexeme);
                break;
            case SEMA_VAR_REF_INDEX:
                analyzer.semaVariableReferenceIndex();
                break;
            case SEMA_VAR_REF_ARRAY_CHECK:
                analyzer.semaVariableReferenceArrayCheck();
                break;
            case SEMA_ASSIGN:
                analyzer.semaAssign();
                break;
            case SEMA_PLUS:
                analyzer.semaArithmeticBinOp(new AST.Add());
                break;
            case SEMA_MINUS:
                analyzer.semaArithmeticBinOp(new AST.Sub());
                break;
            case SEMA_MULT:
                analyzer.semaArithmeticBinOp(new AST.Mult());
                break;
            case SEMA_DIV:
                analyzer.semaArithmeticBinOp(new AST.Div());
                break;
            case SEMA_GREATER:
                analyzer.semaComparisonBinOp(new AST.Greater());
                break;
            case SEMA_LESSER:
                analyzer.semaComparisonBinOp(new AST.Lesser());
                break;
            case SEMA_GREATEREQ:
                analyzer.semaComparisonBinOp(new AST.GreaterEq());
                break;
            case SEMA_LESSEREQ:
                analyzer.semaComparisonBinOp(new AST.LesserEq());
                break;
            case SEMA_EQ:
                analyzer.semaComparisonBinOp(new AST.Eq());
                break;
            case SEMA_NEQ:
                analyzer.semaComparisonBinOp(new AST.Neq());
                break;
            case SEMA_AND:
                analyzer.semaLogicBinOp(new AST.And());
                break;
            case SEMA_OR:
                analyzer.semaLogicBinOp(new AST.Or());
                break;
            case SEMA_IF_START:
                analyzer.semaIfStart();
                break;
            case SEMA_IF_BLOCK:
                analyzer.semaIfBlock();
                break;
            case SEMA_ELSE_START:
                analyzer.semaElseStart();
                break;
            case SEMA_ELSE_BLOCK:
                analyzer.semaElseBlock();
                break;
            case SEMA_WHILE_START:
                analyzer.semaWhileStart();
                break;
            case SEMA_WHILE_BLOCK:
                analyzer.semaWhileBlock();
                break;
            case SEMA_FOR_START:
                analyzer.semaForStart();
                break;
            case SEMA_FOR_BLOCK:
                analyzer.semaForBlock();
                break;
            case SEMA_BREAK:
                analyzer.semaBreak();
                break;
            case SEMA_FUNC_START:
                analyzer.semaFunctionStart();
                break;
            case SEMA_FUNC_ARGS:
                analyzer.semaFunctionArgs();
                break;
            case SEMA_FUNC_RET:
                analyzer.semaFunctionReturnType();
                break;
            case SEMA_FUNC_BLOCK:
                analyzer.semaFunctionBlock();
                break;
            case SEMA_RETURN:
                analyzer.semaReturn();
                break;
            case SEMA_PROC_CALL:
                analyzer.semaProcedureCall();
                break;
            case SEMA_FUNC_CALL:
                analyzer.semaFunctionCall();
                break;
        }

        // No success so code won't generate
        if (analyzer.isSemanticError()) {
            parseSuccess = false;
        }
    }

    public boolean isParseSuccess() {
        return parseSuccess;
    }
}
