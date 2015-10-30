import java.util.*;

// A production is a single grammar rule
// Example: expr -> expr + expr

// Productions are composed of:
// 1. The left hand side: a NonTerminal
// 2. The derivation: an ArrayList of symbols where index 0 is the left-most symbol

public class Production {


    public NonTerminal nonterminal;
    public ArrayList<Symbol> derivation;

    public Production(NonTerminal nonterminal, ArrayList<Symbol> derivation){
        this.nonterminal = nonterminal;
        this.derivation = derivation;
    }

    @Override
    public String toString(){
        String derivationStr = "";
        for (int i = 0; i < derivation.size(); i++){
            derivationStr += (derivation.get(i).symbol + " ");
        }
        return nonterminal.symbol + " -> " + derivationStr;
    }

    public String toStringID(){
        String derivationStr = "";
        for (int i = 0; i < derivation.size(); i++){
            if (derivation.get(i) instanceof NonTerminal){
                derivationStr += (((NonTerminal)derivation.get(i)).id + " ");
            }
            else{
                derivationStr += (derivation.get(i).symbol + " ");
            }

        }
        return nonterminal.id + " -> " + derivationStr;


    }


}
