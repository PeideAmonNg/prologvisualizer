package Visual;
import java.util.List;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;

public class SimpleGraphView {

    public final Graph<Node, String> graph;

    public SimpleGraphView(List<Node> nodes) {    	
//    	AbstractGraph<V,E>	Abstract implementation of the Graph interface.
//    	AbstractTypedGraph<V,E>	An abstract class for graphs whose edges all have the same EdgeType.
//    	DelegateForest<V,E>	An implementation of Forest that delegates to a specified DirectedGraph instance.
//    	DelegateTree<V,E>	An implementation of Tree that delegates to a specified instance of DirectedGraph.
//    	DirectedOrderedSparseMultigraph<V,E>	An implementation of DirectedGraph, suitable for sparse graphs, that orders its vertex and edge collections according to insertion time.
//    	DirectedSparseGraph<V,E>	An implementation of DirectedGraph suitable for sparse graphs.
//    	DirectedSparseMultigraph<V,E>	An implementation of DirectedGraph, suitable for sparse graphs, that permits parallel edges.
//    	GraphDecorator<V,E>	An implementation of Graph that delegates its method calls to a constructor-specified Graph instance.
//    	ObservableGraph<V,E>	A decorator class for graphs which generates events
//    	OrderedKAryTree<V,E>	An implementation of Tree in which each vertex has <= k children.
//    	OrderedSparseMultigraph<V,E>	An implementation of Graph that orders its vertex and edge collections according to insertion time, is suitable for sparse graphs, and permits directed, undirected, and parallel edges.
//    	SetHypergraph<V,H>	An implementation of Hypergraph that is suitable for sparse graphs and permits parallel edges.
//    	SortedSparseMultigraph<V,E>	An implementation of Graph that is suitable for sparse graphs, orders its vertex and edge collections according to either specified Comparator instances or the natural ordering of their elements, and permits directed, undirected, and parallel edges.
//    	SparseGraph<V,E>	An implementation of Graph that is suitable for sparse graphs and permits both directed and undirected edges.
//    	SparseMultigraph<V,E>	An implementation of Graph that is suitable for sparse graphs and permits directed, undirected, and parallel edges.
//    	UndirectedOrderedSparseMultigraph<V,E>	An implementation of UndirectedGraph that is suitable for sparse graphs, orders its vertex and edge collections according to insertion time, and permits parallel edges.
//    	UndirectedSparseGraph<V,E>	An implementation of UndirectedGraph that is suitable for sparse graphs.
//    	UndirectedSparseMultigraph<V,E>
    	
    	graph = new DirectedSparseMultigraph<>();
    	
    	if(nodes != null){
    		int id = 1;
            for(int i = 0; i < nodes.size(); i++){
            	graph.addVertex(nodes.get(i));
            	if(!nodes.get(i).nodesFrom.isEmpty()){
            		Node n = nodes.get(i);
            		for(Node x : n.nodesFrom){
            			graph.addEdge(Integer.toString(id++), x, n);
            		}        		
            	}
            }
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
