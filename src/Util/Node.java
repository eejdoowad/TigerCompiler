package Util;

import java.util.ArrayList;

public abstract class Node {

    private ArrayList<Node> succ = new ArrayList<>();
    private ArrayList<Node> pred = new ArrayList<>();
    private ArrayList<Node> adj = new ArrayList<>();


    public Node(){

    }

    public void addSucc(Node n){
        succ.add(n);
        n.pred.add(this);
    }

    public void removeSucc(Node n){
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
    public boolean goesTo(Node n){
        return succ.contains(n);
    }
    public boolean comesFrom(Node n){
        return pred.contains(n);
    }
    public boolean adj(Node n){
        return goesTo(n) || comesFrom(n);
    }
    public String toString(){
        return "(NODE)";
    }
}