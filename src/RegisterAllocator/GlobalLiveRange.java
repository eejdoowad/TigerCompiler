package RegisterAllocator;

import IR.Register;
import IR.Var;
import IR.IR;
import java.util.*;

// A Live Range is a variable and the lines on which it is used
public class GlobalLiveRange {

    // The variable of the line range
    public Var var;

    // A variable is live after its first definition until before its last use
    // Live ranges interfere if
    // If it is never used after definition, the live range is empty

    // The live lines, as index into global instruction stream
    private Set<Integer> liveLines = new LinkedHashSet<>();
    public Set<Integer> getLiveLines(){
        return liveLines;
    }
    public void addUseLine(Integer line){
        liveLines.add(line);
    }

    // The definition lines, as index into global instruction stream
    private Set<Integer> definitionLines = new LinkedHashSet<>();
    public Set<Integer> getDefinitionLines(){
        return definitionLines;
    }
    public void addDefinitionLines(Integer line){
        definitionLines.add(line);
    }

    // Color assigned to the live range
    private Register.Reg color;
    public Register.Reg getColor(){
        return color;
    }
    public void setColor(Register.Reg color){
        this.color = color;
    }

    // Spilling data/methods
    public int spillCost(){
        return numUses;
    }
    public boolean spilled = false;
    private int numUses = 0;
    public void incrementUses(){
        numUses++;
    }

    // Constructor
    public GlobalLiveRange(Var var){
        this.var = var;
    }

    // Returns true if live ranges interfere (overlap)
    boolean interferesWith(GlobalLiveRange other){

        // floats and ints have separate live ranges and can't interfere
        if (this.var.isInt() != other.var.isInt()) return false;

         // If live ranges overlap, they interfere
        for (Integer i : getLiveLines()){
            if (other.getLiveLines().contains(i)) return true;
        }

        return false;
    }

    // merges a range with another range
    void union(GlobalLiveRange other) {
        for (Integer line : other.definitionLines) {
            definitionLines.add(line);
        }
        for (Integer line : other.liveLines) {
            liveLines.add(line);
        }
        if (color == null) {
            color = other.color;
        }
    }

    public String toString(){

        // Get Definition Lines
        String defLines = "";
        boolean first = true;
        for (Integer line : getDefinitionLines()){
            if (!first)
                defLines += ", ";
            defLines += line.toString();
            first = false;
        }

        // Get Live Lines
        String liveLines = "";
        first = true;
        for (Integer line : getLiveLines()){
            if (!first)
                liveLines += ", ";
            liveLines += line.toString();
            first = false;
        }

        String out = "";
        out += var.toString();
        out += (var.isInt() ? "int" : "float") + ": ";
        out += "[defs=" + defLines + "]";
        out += "[lives=" + liveLines + "]";
        out += "[reg=" + color + "]";

        return out;
    }
}
