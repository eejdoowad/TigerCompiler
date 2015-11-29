package Util;

import java.util.ArrayList;

public class Graph {

    private ArrayList<Node> nodes = new ArrayList<>();

    public Graph() {

    }

    public void addNode(Node n){
        nodes.add(n);
    }

    public void addEdge(Node from, Node to){
        from.goesTo(to);
        to.comesFrom(from);
    }

    public void removeEdge(Node from, Node to){

    }

    public void print(){

    }
}