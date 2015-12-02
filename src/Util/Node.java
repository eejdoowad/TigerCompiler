package Util;


import java.util.ArrayList;

public class Node<T> {

    public T val;

    public ArrayList<Node<T>> adj = new ArrayList<>();
    public ArrayList<Node<T>> getAdj(){
        return adj;
    }

    public Node(T val){
        this.val = val;
    }
    public void connect(Node n){
        if (!this.neighbors(n)){
            adj.add(n);
            n.adj.add(this);
        }
    }

    public void disconnet(Node n){
        if (adj.contains(n) && n.adj.contains(this)){
            n.adj.remove(this);
            adj.remove(n);
        }
        else{
            System.out.println("UTIL.NODE ERROR: ATTEMPTED TO Disconnect nonneighboring nodes");
        }
    }

    public int degree(){
        return adj.size();
    }
    public boolean neighbors(Node n){
        return adj.contains(n);
    }
    public String toString(){
        return val.toString() + "[Deg=" + getAdj().size() + "]";
    }
}
