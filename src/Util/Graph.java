package Util;


import java.util.ArrayList;

public abstract class Graph<T> {

    private ArrayList<Node<T>> nodes = new ArrayList<>();

    public ArrayList<Node<T>> getNodes(){
        return nodes;
    }

    public  void addNode(Node<T> n){
        nodes.add(n);
    }

    public void connect(Node<T> node1, Node<T> node2){
        if (nodes.contains(node1) && nodes.contains(node2))
                node1.connect(node2);
        else{
            System.out.println("ERROR: GRAPH ADD EDGE");
            System.exit(1);
        }
    }

    public void disconnect(Node<T> node1, Node<T> node2){
        if (nodes.contains(node1) && nodes.contains(node2)){
            node1.disconnet(node2);
        }
        else{
            System.out.println("ERROR: GRAPH REMOVE EDGE");
            System.exit(1);
        }
    }

}
