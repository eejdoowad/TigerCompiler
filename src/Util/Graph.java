package Util;

import java.util.ArrayList;

public abstract class Graph<T extends Node> {

    private ArrayList<T> nodes = new ArrayList<>();

    public Graph() {

    }

    public ArrayList<T> getNodes(){
        return nodes;
    }

    public  void addNode(T n){
        nodes.add(n);
    }

    public void addEdge(T from, T to){
        if (nodes.contains(from) && nodes.contains(to))
            from.addSucc(to);
        else{
            System.out.println("ERROR: GRAPH ADD EDGE");
            System.exit(1);
        }
    }

    public void removeEdge(T from, T to){
        if (nodes.contains(from) && nodes.contains(to)){
            from.removeSucc(to);
        }
        else{
            System.out.println("ERROR: GRAPH REMOVE EDGE");
            System.exit(1);
        }
    }

    public void print(){

    }
}