package Util;

import java.util.ArrayList;

public abstract class DiGraph<T extends DiNode> {

    private ArrayList<T> nodes = new ArrayList<>();

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
            System.out.println("ERROR: DIGRAPH ADD EDGE");
            System.exit(1);
        }
    }

    public void removeEdge(T from, T to){
        if (nodes.contains(from) && nodes.contains(to)){
            from.removeSucc(to);
        }
        else{
            System.out.println("ERROR: DIGRAPH REMOVE EDGE");
            System.exit(1);
        }
    }
}