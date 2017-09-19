package Visual;

import java.awt.Color;

public class ListOperatorNode extends Node {

	public ListOperatorNode(String node) {
		super(node);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub

	}

	public TYPE getType(){
		return TYPE.ListOperator;
	}

	@Override
	public Color getNodeColor() {
		// TODO Auto-generated method stub
		if(super.isMainArg){
			return Color.LIGHT_GRAY;
		}
		
		return Color.orange;
	}
}
