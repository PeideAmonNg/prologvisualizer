package Visual;

public class Edge {

	public String fromLabel = "", centerLabel = "", toLabel = ""; 
	public Node fromNode, toNode;
	public boolean isDirected = true;
	
	public Edge(String fromLabel, String centerLabel, String toLabel, Node fromNode, Node toNode, boolean isDirected){
		this.fromLabel = fromLabel;
		this.centerLabel = centerLabel;
		this.toLabel = toLabel;
		
		init(fromNode, toNode, isDirected);
	}
	
	public Edge(Node fromNode, Node toNode, boolean isDirected){		
		init(fromNode, toNode, isDirected);
	}
	
	private void init(Node fromNode, Node toNode, boolean isDirected){
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.isDirected = isDirected;
	}
}
