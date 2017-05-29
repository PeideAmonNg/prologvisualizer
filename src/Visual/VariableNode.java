package Visual;

import Visual.Node.TYPE;

public class VariableNode extends Node {
		
	public VariableNode(final String node){
		super(node);
	}
	
	@Override
	public void render() {
		// TODO Auto-generated method stub

	}

	public TYPE getNodeType(){
		return TYPE.Variable;
	}
}
