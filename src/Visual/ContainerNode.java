package Visual;

import java.util.ArrayList;
import java.util.List;

public class ContainerNode extends Node{
	
	public ContainerNode(String node) {
		super(node);
		// TODO Auto-generated constructor stub
	}

	private List<Node> nodes = new ArrayList<>();
	
	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TYPE getNodeType() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void addNode(Node node){
		this.nodes.add(node);
	}

}
