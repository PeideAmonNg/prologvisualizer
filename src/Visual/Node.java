package Visual;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {
	
	protected String node;
	protected List<Node> nodesTo = new ArrayList<>();
	protected List<Node> nodesFrom = new ArrayList<>();
	public enum TYPE {Functor, ListOperator, MainArgument, Operator, Variable};
	
	public Node(String node){
		this.node = node;
	}
	
	public abstract void render();
	
	public abstract TYPE getNodeType();
	
	public String getNodeName() {
		return node;
	}
	
	public void addToNode(Node node){
		this.nodesTo.add(node);
	}
	
	public void addFromNode(Node node){
		this.nodesFrom.add(node);
	}
	
	public static Node getExistingNode(List<Node> nodes, String name){
		for(Node n: nodes){
			if(n.getNodeName().equals(name)){
				return n;
			}
		}
		
		return null;
	}

}