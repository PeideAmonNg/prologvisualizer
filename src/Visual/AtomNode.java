package Visual;

import java.awt.Color;

public class AtomNode extends Node{
	
	public AtomNode(String node){
		super(node);
	}
	
	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TYPE getNodeType() {
		// TODO Auto-generated method stub
		return Node.TYPE.Atom;
	}

	@Override
	public Color getNodeColor() {
		// TODO Auto-generated method stub
		return null;
	}

}
