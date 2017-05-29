package Visual;

import java.util.ArrayList;
import java.util.List;

import Visual.Node.TYPE;

public class OperatorNode extends Node {
	
	public OperatorNode(String node){
		super(node);
	}
	
	@Override
	public void render() {
		// TODO Auto-generated method stub

	}

	public TYPE getNodeType(){
		return TYPE.Operator;
	}
}
