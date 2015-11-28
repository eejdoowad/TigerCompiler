package Parser;

public class NonTerminal extends Symbol {

    int id; // each nonTerminal should be uniquely identified by its id

    public NonTerminal(String symbol, int id){
        this.symbol = symbol;
        this.id = id;
    }

    public String toString(){
        return symbol;
    }
}
