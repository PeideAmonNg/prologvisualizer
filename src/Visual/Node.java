package Visual;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public abstract class Node {
	
	protected String node;
	public List<Node> nodesTo = new ArrayList<>();
	public List<Node> nodesFrom = new ArrayList<>();
	public enum TYPE {Functor, ListOperator, MainArgument, Operator, Variable};
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
	
	
	@Override
	public String toString(){
		return this.getNodeName();
	}

}