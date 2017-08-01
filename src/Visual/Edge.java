package Visual;

public class Edge {

	public String label;
	public Node fromNode, toNode;
	public boolean isDirected = true;
	
	public Edge(String label, Node fromNode, Node toNode, boolean isDirected){
		this.label = label;
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.isDirected = isDirected;
	}
}
