package Util;

import java.util.ArrayList;

public abstract class DiNode {

    public ArrayList<DiNode> getSucc(){return succ;}
    private ArrayList<DiNode> succ = new ArrayList<>();
    private ArrayList<DiNode> pred = new ArrayList<>();

    public void addSucc(DiNode n){
        succ.add(n);
        n.pred.add(this);
    }

    public void removeSucc(DiNode n){
        if (succ.contains(n) && n.pred.contains(this)){
            n.pred.remove(this);
            succ.remove(n);
        }
        else{
            System.out.println("UTIL.NODE ERROR: ATTEMPTED TO REMOVE NONEXISTENT SUCCESSOR");
        }
    }

    public int inDegree(){
        return pred.size();
    }

    public int outDegree(){
        return succ.size();
    }

    public int degree(){
        return inDegree() + outDegree();
    }
    public boolean goesTo(DiNode n){
        return succ.contains(n);
    }
    public boolean comesFrom(DiNode n){
        return pred.contains(n);
    }
    public boolean adj(DiNode n){
        return goesTo(n) || comesFrom(n);
    }
    public String toString(){
        return "(DINODE)";
    }
}