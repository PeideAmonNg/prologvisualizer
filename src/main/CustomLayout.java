package main;

import java.util.Stack;

import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.graph.Graph;

public class CustomLayout<Node, Edge> extends ISOMLayout<Node, Edge>{
	private boolean done = false;
	private Stack<Node> mainBranch;
	
	public CustomLayout(Graph g, Stack<Node> mainBranch) {
		super(g);
		this.mainBranch = mainBranch;
	}
	
	@Override
	public boolean done(){

		if(this.mainBranch == null){
			return super.done();
		}
		return this.done;
	}
	
	@Override
	public void step(){
		if(this.mainBranch == null){
			super.step();
		}else{
			if(!done){					
				double i = 0;
				for(Node node : this.mainBranch){
					i++;
					double x = i * 200;
					double y = 200;				
					setLocation(node, x, y);				
				}
	
				double bound_x = i * 200, bound_y = Visualiser.LAYOUT_HEIGHT - 400;
				
				for(Node node : getGraph().getVertices()) {			
					if(!mainBranch.contains(node)){
						System.err.println("not in mainBranch");
						setLocation(node, Math.random() * bound_x, Math.random() * bound_y + 300);
					}
				}
				
				done = true;
			}
		}
	}

}
