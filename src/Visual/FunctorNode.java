package Visual;

import Visual.Node.TYPE;

public class FunctorNode extends Node {

	public FunctorNode(String node) {
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
