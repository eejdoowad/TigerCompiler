package RegisterAllocator;


import IR.Var;
import Util.Graph;
import Util.Node;

import java.util.*;

public class InterferenceGraph extends Graph<LiveRange> {

    private LiveRanges ranges;
    private ArrayList<Node<LiveRange>> intNodes = new ArrayList<>();
    private ArrayList<Node<LiveRange>> floatNodes = new ArrayList<>();
    public ArrayList<Node<LiveRange>> getIntNodes(){
        return intNodes;
    }
    public ArrayList<Node<LiveRange>> getFloatNodes(){
        return floatNodes;
    }

    // a graph where each node represents a live range for a particular variable
    public InterferenceGraph(LiveRanges ranges) {
        this.ranges = ranges;

        // Add a node in the graph for every live range
        LinkedList<LiveRange> allRanges = ranges.allRanges();
        for (LiveRange lr : allRanges) {
            addNode(new Node<LiveRange>(lr));
        }

        // add interference edges
        for (Node<LiveRange> lr1 : this.getNodes()) {
            for (Node<LiveRange> lr2 : this.getNodes()) {
                if (lr1.val.interferesWith(lr2.val)) {
                    connect(lr1, lr2);
                }
            }
        }

        // separate into int and float nodes
        for (Node<LiveRange> node : getNodes()){
            if (node.val.var.isInt()){
                intNodes.add(node);
            }
            else{
                floatNodes.add(node);
            }
        }
    }

    // creates an interference graph in which each node
    // gets deep copied (but LiveRanges within node are shallow copied)
    public InterferenceGraph copy(){
        return new InterferenceGraph(ranges);
    }
}
