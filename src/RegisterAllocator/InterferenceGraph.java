package RegisterAllocator;


import IR.Var;
import Util.Graph;
import Util.Node;

import java.util.*;

public class InterferenceGraph extends Graph<LiveRange> {

    private LiveRanges ranges;


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
    }

    public InterferenceGraph(InterferenceGraph other){
        ranges = other.ranges; // ranges are shallow copied

        // Add a node in the graph for every node in other graph
        for (Node<LiveRange> lr : other.getNodes()) {
            addNode(new Node<LiveRange>(lr.val));
        }

        // add interference edges
        for (Node<LiveRange> lr1 : this.getNodes()) {
            for (Node<LiveRange> lr2 : this.getNodes()) {
                if (lr1.val.interferesWith(lr2.val)) {
                    connect(lr1, lr2);
                }
            }
        }
    }

    // creates an interference graph in which each node
    // gets deep copied (but LiveRanges within node are shallow copied)
    public InterferenceGraph copy(){
        return new InterferenceGraph(this);
    }
}
