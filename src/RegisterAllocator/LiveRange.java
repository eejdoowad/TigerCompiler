package RegisterAllocator;

import IR.Register;
import IR.Var;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

// A Live Range is a variable and the lines on which it is used
public class LiveRange {

    public int definitionLine;
    public Var var;
    private Set<Integer> lines = new LinkedHashSet<>();
    public static int rangeNum = 0;
    public int rangeID;
    public BasicBlock block;

    private Register.Reg color;
    public Register.Reg getColor(){
        return color;
    }
    public void setColor(Register.Reg color){
        this.color = color;
    }

    private int numUses = 0;
    public void incrementUses(){
        numUses++;
    }
    public int spillCost(){
        return numUses;
    }

    public boolean spilled = false;


    public LiveRange(Var var, int definitionLine){
        this.var = var;
        this.rangeID = rangeNum++;
        this.definitionLine = definitionLine;
    }

    public Set<Integer> getLines(){
        return lines;
    }
    public void add(Integer line){
        lines.add(line);
    }

    boolean interferesWith(LiveRange other){
        if (this.var == other.var) return false;
        if (this.var.isInt() != other.var.isInt()) return false;
        for (Integer i : lines){
            if (other.lines.contains(i)) return true;
        }
        return false;
    }

    public String toString(){
        String l = "";
        boolean first = true;
        for (Integer line : lines){
            if (!first)
                l += ", ";
            l += line.toString();
            first = false;
        }
        return var.toString() + "#" + rangeID + ": "
                + l + " [" + numUses + " uses][" + (var.isInt() ? "int" : "float") + "][Reg=" + color + "]";
    }
}
