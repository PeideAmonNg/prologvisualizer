package Visual;

import java.awt.Color;

public class FunctorNode extends Node {

	//Icons for list predicates.
	public static final String CONCAT = "./colours/not_member.png";
	public static final String APPEND = "./colours/not_member.png";
	public static final String LENGTH = "./colours/member.png";
	public static final String MEMBER = "./colours/member.png";
	public static final String NOT_MEMBER = "./colours/not_member.png";
	
	
	public FunctorNode(String node) {
		super(node);
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub
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
	
	public static String getIconPath(String nodeName){
		if(nodeName.equals("member")) return FunctorNode.MEMBER;
		if(nodeName.equals("!member")) return FunctorNode.NOT_MEMBER;
		
		return null;
	}

}
