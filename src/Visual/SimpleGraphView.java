package Visual;
import java.util.List;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;

public class SimpleGraphView {

    public final Graph<Node, String> graph;

    public SimpleGraphView(List<Node> nodes) {
        graph = new SparseMultigraph<>();
        for(int i = 0; i < nodes.size(); i++){
        	graph.addVertex(nodes.get(i));
        }
//        graph.addEdge("Edge-A", nodes.get(0), nodes.get(1));
//        graph.addEdge("Edge-B", nodes.get(3), nodes.get(5));
//        graph.addEdge("Edge-B", 1, 3);
//        graph.addEdge("Edge-C", 1, 4);
//        graph.addEdge("Edge-D", 2, 6);
//        graph.addEdge("Edge-E", 2, 4);
//        graph.addEdge("Edge-F", 3, 5);
//        graph.addEdge("Edge-B", 2, 3);
    }
}
