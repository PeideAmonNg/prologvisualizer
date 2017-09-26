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

	@Override
	public TYPE getType(){
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
	
	
	public String getLeftLabel() {
		String name = super.name.trim();
		if(name.equals("+")) return "sum";
		else if(name.equals("-")) return "difference";
		else if(name.equals("*")) return "product";
		else if(name.equals("/")) return "quotient";
		else return "";
	}
	
}
