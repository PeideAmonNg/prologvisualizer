package Visual;

import java.awt.Color;

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

	public TYPE getType(){
		return TYPE.MainArgument;
	}

	@Override
	public Color getNodeColor() {
		// TODO Auto-generated method stub
		return Color.lightGray;
	}
	
}
