package Util;

import java.util.ArrayList;

public abstract class Graph {

    // private ArrayList<Node> nodes = new ArrayList<>();

    public Graph() {

    }

    public abstract void addNode(Node n);

    public void addEdge(Node from, Node to){
        from.addSucc(to);
    }

    public void removeEdge(Node from, Node to){
        from.removeSucc(to);
    }

    public void print(){

    }
}