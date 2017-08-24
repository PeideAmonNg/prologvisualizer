package Visual;
import java.util.List;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

public class SimpleGraphView {

    public final Graph<Node, Edge> graph;

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
    	
//    	graph = new DirectedSparseMultigraph<>();
    	graph = new SparseMultigraph<>();
//    	graph = new UndirectedSparseMultigraph<>();
    	
    	int id = 0;
    	
    	for(int i = 0; i < nodes.size(); i++){
    		Node currentNode = nodes.get(i);
    		graph.addVertex(currentNode);    		
    		List<Edge> fromNodes = currentNode.nodesFrom;
    		
    		for(Edge edge : fromNodes){
//    			if(edge.toNode.getNodeType() == Node.TYPE.Operator){
//    				
//    			}
//    			if(edge.toNode.isMainArg){
    			if(edge.isDirected){
//    				graph.addEdge(edge.label + "_" + id++, edge.fromNode, currentNode, EdgeType.DIRECTED);
    				graph.addEdge(edge, edge.fromNode, currentNode, EdgeType.DIRECTED);
    			}else{
//    				graph.addEdge(edge.label + "_" + id++, edge.fromNode, currentNode, EdgeType.UNDIRECTED);
    				graph.addEdge(edge, edge.fromNode, currentNode, EdgeType.UNDIRECTED);
    			}
//    			}else{
//    				graph.addEdge(edge.label + "_" + id++, edge.fromNode, currentNode, EdgeType.UNDIRECTED);
//    			}
    			
    		}
    	}
    	
    	
//    	if(nodes != null){
//    		int id = 1;
//            for(int i = 0; i < nodes.size(); i++){ // For every node in the graph.
//            	graph.addVertex(nodes.get(i));
//            	if(!nodes.get(i).nodesFrom.isEmpty()){
//            		Node n = nodes.get(i);
//            		
//            		for(int j = 0; j < n.nodesFrom.size(); j++){ // For every node the current node is connected to.
//            			Node fromNode = n.nodesFrom.get(j);
//            			
//            			if(n.getNodeType() == Node.TYPE.ListOperator){
//            				if(fromNode.getNodeType() == Node.TYPE.Variable && fromNode.isMainArg && n.nodesFrom.size() == 1){
//            					graph.addEdge("clauseHeadListArg" + Integer.toString(id++), fromNode, n);
//            				}else{
//	            				if(j == 0){
//	            					System.out.println("tail");
//	            					graph.addEdge("head" + Integer.toString(id++), fromNode, n);
//	            				}else{
//	            					graph.addEdge("tail" + Integer.toString(id++), fromNode, n);
//	            				}
//            				}
//            				
//            			}else if(n.getNodeType() == Node.TYPE.Functor){
//            				if(n.getNodeName().substring(1, n.getNodeName().length()).equals("member")){
//            					if(j == 0){
//            						graph.addEdge("element " + "_" + Integer.toString(id++), fromNode, n);
//            					}else{
//            						graph.addEdge("set " + "_" + Integer.toString(id++), fromNode, n);
//            					}
//            				}else{
//            					graph.addEdge("arg " + (j+1) + "_" + Integer.toString(id++), fromNode, n);
//            				}
//            			}else if(n.getNodeType() == Node.TYPE.Operator){
//            				if(j == 0){
//            					graph.addEdge("op1" + "_" + Integer.toString(id++), fromNode, n);            					
//            				}else{
//            					graph.addEdge("op2" + "_" + Integer.toString(id++), fromNode, n);            					
//            				}
//            			}else if(n.getNodeType() == Node.TYPE.Variable){
//            				if(fromNode.getNodeType() == Node.TYPE.Functor){
//            					graph.addEdge("is _fromFunctor" + Integer.toString(id++), fromNode, n);
//            				}else if(fromNode.getNodeType() == Node.TYPE.ListOperator){
//            					if(n == fromNode.nodesTo.get(0)){
//	            					System.out.println("head");
//	            					graph.addEdge("head" + Integer.toString(id++), fromNode, n);
//	            				}else{
//	            					graph.addEdge("tail" + Integer.toString(id++), fromNode, n);
//	            				}
//            				}else{
//                				graph.addEdge("is " + "_" + Integer.toString(id++), fromNode, n);            					
//            				}
//
//            			}else{
//            				graph.addEdge(Integer.toString(id++), fromNode, n);
//            			}
//            		}        		
//            	}
//            }
//    	}
        
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
