package Visual;

import Visual.Node.TYPE;

public class ListOperatorNode extends Node {

	public ListOperatorNode(String node) {
		super(node);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub

	}

	public TYPE getNodeType(){
		return TYPE.ListOperator;
	}
}
