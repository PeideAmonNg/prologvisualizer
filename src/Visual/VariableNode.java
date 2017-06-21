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

	public TYPE getNodeType(){
		return TYPE.Variable;
	}

	@Override
	public Color getNodeColor() {
		// TODO Auto-generated method stub
		if(super.isMainArg){
			return Color.LIGHT_GRAY;
		}
		
		return Color.PINK;
	}
}
