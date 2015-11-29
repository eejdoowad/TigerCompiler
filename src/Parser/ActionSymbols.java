package Parser;

import Config.Config;
import Parser.ActionSymbol;
import Parser.ActionSymbolType;

import java.util.*;

public class ActionSymbols {


    // Both keep track of the same action symbols
    // But allow for quick indexing by either TokenType or Symbol
    private HashMap<ActionSymbolType, ActionSymbol> ASByType;
    private HashMap<String, ActionSymbol> ASByString;

    public ActionSymbols(){
        ASByType = new HashMap<ActionSymbolType, ActionSymbol>();
        ASByString = new HashMap<String, ActionSymbol>();

        for (ActionSymbolType type : ActionSymbolType.values()) {
            add(type);

        }

        if (Config.DEBUG_INIT){
            System.out.println("ActionSymbols initialized");
        }
    }



    public void add(ActionSymbolType type){

        if (!this.containsByType(type)){
            ActionSymbol t = new ActionSymbol(type);
            ASByType.put(type, t);
            ASByString.put(type.toString(), t);
        }
    }


    public Boolean containsByType(ActionSymbolType type) {
        return ASByType.containsKey(type);
    }

    public Boolean containsBySymbol(String symbol) {
        return ASByString.containsKey(symbol);
    }

    public int size(){
        return ASByType.size();
    }

    public ActionSymbol getBySymbol(String symbol){
        return ASByString.get(symbol);
    }

    public ActionSymbol getByType(ActionSymbolType type){
        return ASByType.get(type);
    }
}



