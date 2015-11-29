package Parser;

import Config.Config;
import Parser.NonTerminal;

import java.util.ArrayList;
import java.util.HashMap;

// NonTerminals is a collection designed to keep all of a grammar's NonTerminals such that:
//
//      1. Each NonTerminal is addressable by a unique integer ID (e.g. 0)
//         This is supported by an ArrayList
//      2. Each NonTerminal is addressable by a unique string symbol (e.g. tiger-program)
//         This is supported by a HashMap
//
// For example, in our grammar, tiger-program corresponds to a NonTerminal that can be accessed
// either by the integr id 0 (since it's the first nonterminal in the grammar)
// or string symbol "tiger-program"


public class NonTerminals {

    // Both keep track of the same nonterminals
    // But allow for quick indexing by either symbol or id
    private ArrayList<NonTerminal> nonTermByID;
    private HashMap<String, NonTerminal> nonTermByString = new HashMap<String, NonTerminal>();

    public NonTerminals(){
        nonTermByID = new ArrayList<NonTerminal>();
        nonTermByString = new HashMap<String, NonTerminal>();

        if (Config.DEBUG_INIT){
            System.out.println("NonTerminals initialized");
        }
    }

    public void add(String symbol){

        if (!this.contains(symbol)){
            NonTerminal nt = new NonTerminal(symbol, nonTermByID.size());
            nonTermByID.add(nt);
            nonTermByString.put(symbol, nt);
        }
    }


    public Boolean contains(String symbol) {
         return nonTermByString.containsKey(symbol);
    }

    public int size(){
        return nonTermByID.size();
    }

    public NonTerminal getByID(int id){
        return nonTermByID.get(id);
    }

    public NonTerminal getBySymbol(String symbol){
        return nonTermByString.get(symbol);
    }
}
