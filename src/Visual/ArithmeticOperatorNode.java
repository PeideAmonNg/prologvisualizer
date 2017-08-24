package Visual;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import Visual.Node.TYPE;

public class ArithmeticOperatorNode extends Node {
	
	public ArithmeticOperatorNode(String node){
		super(node);
	}
	
	@Override
	public void render() {
		// TODO Auto-generated method stub

	}

	public TYPE getNodeType(){
		return TYPE.Operator;
	}

	@Override
	public Color getNodeColor() {
		// TODO Auto-generated method stub
		if(super.isMainArg){
			return Color.LIGHT_GRAY;
		}
		
		return Color.green;
	}
}
