package Visual;

import java.awt.Color;

import Visual.Node.TYPE;

public class VariableNode extends Node {
		
	public VariableNode(final String node){
		super(node);
	}
	
	@Override
	public void render() {
		// TODO Auto-generated method stub

	}

	public TYPE getType(){
		return TYPE.Variable;
	}

	@Override
	public Color getNodeColor() {
		// TODO Auto-generated method stub
		if(super.isMainArg){
			return Color.LIGHT_GRAY;
		}
		
		return Color.white;
	}
	
	public boolean isJunctionVariable() {
		return getInEdges().size() + getOutEdges().size() >= 3;
	}
}
