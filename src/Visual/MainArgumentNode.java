package Visual;

import Visual.Node.TYPE;

public class MainArgumentNode extends Node {
	
	private Node node;
	
	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public MainArgumentNode(String node) {
		super(node);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub

	}

	public TYPE getNodeType(){
		return TYPE.MainArgument;
	}
	
}
