package main;

import java.awt.geom.Point2D;
import java.util.Stack;

import Visual.Node;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.graph.Graph;

public class ExtendedIsomLayout2<Node, Edge> extends ISOMLayout<Node, Edge> {

	private Stack<Node> mainBranch;
	private boolean isDrawn;
	
	public ExtendedIsomLayout2(Graph<Node, Edge> g, Stack<Node> mainBranch) {
		super(g);
		this.mainBranch = mainBranch;
	}
	
	@Override
	public void step() {
//		
//		if(this.mainBranch != null && !isDrawn){			
//				
			double i = 0;
			for(Node node : this.mainBranch){
				i++;
				double x = i * 200;
				double y = 200;				
				setLocation(node, x, y);
				lock(node, true);
			}
					
			this.isDrawn = true;
//		}
		
		super.step();
	}
	
}
