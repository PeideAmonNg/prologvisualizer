package main;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;

public class SimpleGraphView {

    public final Graph<Integer, String> graph;

    public SimpleGraphView() {
        graph = new SparseMultigraph<>();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge("Edge-A", 1, 2);
        graph.addEdge("Edge-B", 2, 3);
    }
}
