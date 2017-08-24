package Visual;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public abstract class Node {
	
	
	protected String node;
//	public List<Node> nodesTo = new ArrayList<>();
//	public List<Node> nodesFrom = new ArrayList<>();
	public List<Edge> nodesTo = new ArrayList<>();
	public List<Edge> nodesFrom = new ArrayList<>();
	
	public enum TYPE {Functor, ListOperator, MainArgument, Operator, Variable, Atom};
	public boolean isMainArg;
	public int mainArgNo;
	
	public Node(String node){
		this.node = node;
	}
	
	public void setMainArg(int no){
		this.isMainArg = true;
		this.mainArgNo = no;
	}
	
	public abstract void render();
	public abstract TYPE getNodeType();
	public abstract Color getNodeColor();
	
	public String getNodeName() {
		return node;
	}
	
	public void setNodeName(String node) {
		this.node = node;
	}
	
	public void addToNode(String edgeLabel, Node node){
		System.err.println(edgeLabel);
		System.err.println(this.getNodeName());
		System.err.println(this.getNodeType());
		if(this.getNodeType() == Node.TYPE.ListOperator){
			Edge e = new Edge("", this, node, true);
			e.fromLabel = edgeLabel;
			this.nodesTo.add(e);
			
		}else if(edgeLabel.startsWith(this.getNodeName())){
			this.nodesTo.add(new Edge("", this, node, true));
		}else{
			this.nodesTo.add(new Edge(edgeLabel, this, node, true));
		}
	}
	
	public void addFromNode(String edgeLabel, Node node){
		System.err.println(edgeLabel);
		System.err.println(node.getNodeName());
		if(node.getNodeType() == Node.TYPE.ListOperator){
			Edge e = new Edge("", node, this, true);
			e.fromLabel = edgeLabel;
			this.nodesFrom.add(e);
			
		}else if(edgeLabel.startsWith(node.getNodeName())){
			this.nodesFrom.add(new Edge("", node, this, true));
		}else{
			this.nodesFrom.add(new Edge(edgeLabel, node, this, true));
		}
	}
	
	public static Node getExistingNode(List<Node> nodes, String name){
		for(Node n: nodes){
			if(n.getNodeName().equals(name)){
				return n;
			}
		}
		
		return null;
	}
	
	public Edge getFromNodeEdge(Node node) throws Exception{
		for(Edge e : this.nodesFrom){
			if(e.fromNode == node){
				return e;
			}
		}
		throw new Exception("no fromNode of the name");
	
	}
	
	public Edge getToNodeEdge(Node node) throws Exception{
		for(Edge e : this.nodesTo){
			if(e.toNode == node){
				return e;
			}
		}
		throw new Exception("no toNode of the name");
	
	}
	
	@Override
	public String toString(){
		return this.getNodeName();
	}

}