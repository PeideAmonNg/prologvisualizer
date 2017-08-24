package Visual;

import java.awt.Color;
import java.net.URL;

import main.Visualiser;

public class FunctorNode extends Node {

	//Icons for list predicates.
	public static final String CONCAT = "/resources/not_member.png";
	public static final String APPEND = "/resources/not_member.png";
	public static final String LENGTH = "/resources/member.png";
	public static final String MEMBER = "/resources/member.png";
	public static final String NOT_MEMBER = "/resources/not_member.png";
	
	
	public FunctorNode(String node) {
		super(node);
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub

	}
	
	public TYPE getNodeType(){
		return TYPE.Functor;
	}

	@Override
	public Color getNodeColor() {
		if(super.isMainArg){
			return Color.LIGHT_GRAY;
		}
		
		return Color.yellow;
	}
	
	public static boolean isListPredicate(String nodeName){
		if(nodeName.equals("member") || nodeName.equals("!member")){
			return true;
		}
		
		return false;
		
	}
	
	public static URL getIconPath(String nodeName){
		if(nodeName.equals("member")) return Visualiser.class.getResource(FunctorNode.MEMBER);
		if(nodeName.equals("!member")) return Visualiser.class.getResource(FunctorNode.NOT_MEMBER);
		
		return null;
	}

}
