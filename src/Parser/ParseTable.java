package Parser;

import Config.Config;
import Util.Util;
import Parser.Grammar;

import java.util.*;



// A parse table is used for determining the next grammar rule to apply
// Given: A nonterminal and a look-ahead symbol (token type),
// the parse table returns the index of the next grammar rule to apply



public class ParseTable {

    // A Parse table is an array of Hashmaps such that
    //     1. the array is indexed by the NonTerminal number (see Grammar class)
    //     2. Each Hashmap contains the TokenType to Production mappings for the indexed NonTerminal
    // The end result is a quick way to find the correct expansion rule given
    //     1. The NonTerminal ID at the top of the parse stack
    //     2. The TokenType of the token returned by the scanner

    public ArrayList<HashMap<TokenType, Integer>> table;
    Grammar grammar; // needed for constructing ParseTable

    public ParseTable(Grammar grammar){
        table = new ArrayList<HashMap<TokenType, Integer>>();
        this.grammar = grammar;
        init();
        if (Config.DEBUG && Config.DEBUG_INIT){
            System.out.println("Parse Table initialized");
        }
    }

    private void init(){
        // open ParseTable CSV file
        String [] lines = Util.readLines(Config.PARSE_TABLE_PATH);
        if (lines.length == 0) {System.out.println("ParseTable Invalid (it's empty). ABORT."); System.exit(1);}

        // Now store the index at which each TokenType appears in the ParseTable.csv file
        String [] tokenLineEntries = lines[0].split(",");
        // Note first entry will be junk and should not be written or read
        TokenType[] tokenTypeAtIndex = new TokenType[tokenLineEntries.length]; // first entry blank in csv file
        for (int i = 1; i < tokenLineEntries.length; i++){
            tokenTypeAtIndex[i] = grammar.terminals.getBySymbol(tokenLineEntries[i]).type;
        }

        // Construct ParseTable by adding TokenType-ProductionID Mappings for each NonTerminal
        for (int lineNum = 1; lineNum < lines.length; lineNum++){

            HashMap<TokenType, Integer> newMappings = new HashMap<TokenType, Integer>();
            String [] lineEntries = lines[lineNum].split(",");

            // Now add a new Mapping for every token if the parsetable line entry is not empty
            // first entry is the nonterminal string, so start loop index at 1
            for (int i = 1; i < lineEntries.length; i++){
                if (!lineEntries[i].trim().isEmpty()){
                    // Subtract 1 because parse table rules start at 1 while grammar rules indexed by 0
                    newMappings.put(tokenTypeAtIndex[i], Integer.parseInt(lineEntries[i].trim()) - 1);
                }
            }
            if (Config.DEBUG && Config.DEBUG_PARSETABLE){
                System.out.print("NonTerm " + (lineNum - 1) + " (" + grammar.nonTerminals.getByID(lineNum - 1).symbol + "): ");
                for (TokenType key : newMappings.keySet()){
                    System.out.print(key + "->" + newMappings.get(key) + ", ");
                }
                System.out.println();
            }
            table.add(newMappings);
        }
    }

    public TokenType getAnExpected(int nonTerminalID){ return table.get(nonTerminalID).keySet().iterator().next(); }

    public int getRuleID(int nonTerminalID, TokenType tokenType){
        return table.get(nonTerminalID).get(tokenType);
    }

    public boolean containsRuleID(int nonTerminalID, TokenType tokenType){
        return (nonTerminalID < table.size()) && table.get(nonTerminalID).containsKey(tokenType);
    }



}
