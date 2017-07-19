package Visual;

public class Edge {

	public String label;
	public Node fromNode, toNode;
	
	public Edge(String label, Node fromNode, Node toNode){
		this.label = label;
		this.fromNode = fromNode;
		this.toNode = toNode;
	}
}
