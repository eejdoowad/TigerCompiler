package RegisterAllocator;


import IR.Var;
import Util.Graph;
import Util.Node;

import java.util.*;

public class InterferenceGraph extends Graph<LiveRange> {

    // a graph where each node represents a live range for a particular variable
    public InterferenceGraph(LiveRanges ranges) {

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

        System.out.println("interference edges generated");
    }
}
