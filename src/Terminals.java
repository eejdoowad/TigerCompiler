
// Contains a list of terminals
// All Terminal symbols are hardcoded in this implementation
// This should be modified in future implementations to allow for a more general compiler generator


// Similar to the NonTerminals Class, Terminals are addressable by either a TokenType or a symbol string
// and provide two separate HashMaps for rapid access via either type of key

import java.util.*;


public class Terminals {

    // Both keep track of the same nonterminals
    // But allow for quick indexing by either TokenType or Symbol
    private HashMap<TokenType, Terminal> TermByType;
    private HashMap<String, Terminal> TermByString;

    public Terminals(){
        TermByType = new HashMap<TokenType, Terminal>();
        TermByString = new HashMap<String, Terminal>();

        for (TokenType type : TokenType.values()) {
            add(type);

        }

        if (Config.DEBUG && Config.DEBUG_INIT){
            System.out.println("Terminals initialized");
        }
    }



    public void add(TokenType type){

        if (!this.containsByType(type)){
            Terminal t = new Terminal(type);
            TermByType.put(type, t);
            TermByString.put(type.toString(), t);
        }
    }


    public Boolean containsByType(TokenType type) {
        return TermByType.containsKey(type);
    }

    public Boolean containsBySymbol(String symbol) {
        return TermByString.containsKey(symbol);
    }

    public int size(){
        return TermByType.size();
    }

    public Terminal getBySymbol(String symbol){
        return TermByString.get(symbol);
    }

    public Terminal getByType(TokenType type){
        return TermByType.get(type);
    }
}

