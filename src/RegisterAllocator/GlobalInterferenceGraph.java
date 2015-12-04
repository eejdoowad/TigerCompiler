package RegisterAllocator;


import IR.Var;
import Util.Graph;
import Util.Node;

import java.util.*;

public class GlobalInterferenceGraph extends Graph<GlobalLiveRange> {

    public GlobalLiveRanges ranges;


    // a graph where each node represents a live range for a particular variable
    public GlobalInterferenceGraph(GlobalLiveRanges ranges) {
        this.ranges = ranges;

        // Add a node in the graph for every live range
        LinkedList<GlobalLiveRange> allRanges = ranges.allRanges();
        for (GlobalLiveRange lr : allRanges) {
            addNode(new Node<GlobalLiveRange>(lr));
        }

        // add interference edges
        for (Node<GlobalLiveRange> lr1 : this.getNodes()) {
            for (Node<GlobalLiveRange> lr2 : this.getNodes()) {
                if (lr1.val.interferesWith(lr2.val)) {
                    connect(lr1, lr2);
                }
            }
        }
    }

    public GlobalInterferenceGraph(GlobalInterferenceGraph other){
        this.ranges = other.ranges; // ranges are shallow copied

        // Add a node in the graph for every node in other graph
        for (Node<GlobalLiveRange> lr : other.getNodes()) {
            addNode(new Node<GlobalLiveRange>(lr.val));
        }

        // add interference edges
        for (Node<GlobalLiveRange> lr1 : this.getNodes()) {
            for (Node<GlobalLiveRange> lr2 : this.getNodes()) {
                if (lr1.val.interferesWith(lr2.val)) {
                    connect(lr1, lr2);
                }
            }
        }
    }

    // creates an interference graph in which each node
    // gets deep copied (but LiveRanges within node are shallow copied)
    public GlobalInterferenceGraph copy(){
        return new GlobalInterferenceGraph(this);
    }
}
