package Visual;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public abstract class Node {
	
	
	protected String name;
	private List<Edge> inEdges = new ArrayList<>(); // Edges from other nodes to this node.
	private List<Edge> outEdges = new ArrayList<>(); // Edges from this node to other nodes.
	
	public enum TYPE {Functor, ListOperator, MainArgument, Operator, Variable, Atom};
	public boolean isMainArg;
	public int mainArgNo;
	
	public Node(String name){
		this.name = name;
	}
	
	public void setMainArg(int number){
		this.isMainArg = true;
		this.mainArgNo = number;
	}
	
	public abstract void render();
	public abstract TYPE getType();
	public abstract Color getNodeColor();
	
	public String getName() {
		return name;
	}
	
	public void setName(String node) {
		this.name = node;
	}
	
	public void addToNode(String edgeLabel, Node node){
		if(this.getType() == Node.TYPE.ListOperator){
			this.inEdges.add(new Edge(edgeLabel, "", "", this, node, true));
			
		}else if(edgeLabel.startsWith(this.getName())){
			this.inEdges.add(new Edge(this, node, true));
		}else if(node.getType() == Node.TYPE.Variable) {
			this.inEdges.add(new Edge(this, node, true));
		}else{
			this.inEdges.add(new Edge("", "", edgeLabel, this, node, true));
		}
	}
	
	public void addFromNode(String edgeLabel, Node node){
		if(node.getType() == Node.TYPE.ListOperator){
			this.outEdges.add(new Edge(edgeLabel, "", "", node, this, true));
			
		}else if(edgeLabel.startsWith(node.getName())){
			this.outEdges.add(new Edge(node, this, true));
		}else if(this.getType() == Node.TYPE.Variable) {
			this.outEdges.add(new Edge(node, this, true));
		}else{
			this.outEdges.add(new Edge("", "", edgeLabel, node, this, true));
		}
	}
	
	public static Node getExistingNode(List<Node> nodes, String name){
		for(Node n: nodes){
			if(n.getName().equals(name)){
				return n;
			}
		}
		
		return null;
	}
	
	public Edge getFromNodeEdge(Node node) throws Exception{
		for(Edge e : this.outEdges){
			if(e.fromNode == node){
				return e;
			}
		}
		throw new Exception("no fromNode of the name");
	
	}
	
	public Edge getToNodeEdge(Node node) throws Exception{
		for(Edge e : this.inEdges){
			if(e.toNode == node){
				return e;
			}
		}
		throw new Exception("no toNode of the name");
	
	}
	
	public List<Edge> getInEdges(){
		return this.outEdges;
	}
	
	public List<Edge> getOutEdges(){
		return this.inEdges;
	}
	
	public int getFromNodeCount(){
		return this.outEdges.size();
	}
	
	public int getToNodeCount(){
		return this.inEdges.size();
	}
	
	public List<Node> getFromNodes(){
		List<Node> fromNodes = new ArrayList<>();
		
		for(Edge edge : this.outEdges){
			fromNodes.add(edge.fromNode);
		}
		
		return fromNodes;
	}
	
	public List<Node> getToNodes(){
		List<Node> toNodes = new ArrayList<>();
		
		for(Edge edge : this.inEdges){
			toNodes.add(edge.toNode);
		}
		
		return toNodes;
	}
	
	public void setEdge(Edge oldEdge, Edge newEdge){
		
	}
	
	@Override
	public String toString(){
		return this.getName();
	}
	
	
	public Node getParentNode(Stack<Node> mainBranch){
		
		for(Edge edge : this.inEdges){
			Node node = edge.toNode;
			if(mainBranch.contains(node)){
				return node;
			}
		}
		
		for(Edge edge : this.outEdges){
			Node node = edge.fromNode;
			if(mainBranch.contains(node)){
				return node;
			}
		}
		
		return null;
	}

	

}