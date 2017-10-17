package main;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import Visual.Edge;
import Visual.Node;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.graph.Graph;

public class CustomLayout extends ISOMLayout<Node, Edge>{
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
		
		return done;
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
	
				
				// set of remaining nodes (excluding nodes on mainBranch)
				Set<Node> remNodes = new HashSet<>();								
				for(Node node : super.getGraph().getVertices()){
					if(!mainBranch.contains(node)){
						remNodes.add(node);
					}
				}

				Map<Node, Integer> nodeLevels = new HashMap<>();
				Queue<Node> nodes = new ArrayDeque<>();
				
				for(Node node : mainBranch) {
					nodeLevels.put(node, 0);
					
					Collection<Node> neighbours = super.getGraph().getNeighbors(node);
					neighbours.removeAll(mainBranch); // neighbours now only contains child nodes
					
					for(Node childNode : neighbours) {
						nodes.offer(childNode);
					}

				}
				
				// Find level of each node in the graph.
				try {
					while(!nodes.isEmpty()) {
						Node node = nodes.poll();
						
						boolean levelFound = false;
						
						int lowestLevel = Integer.MAX_VALUE;
						Node parentNode = null;
						
						for(Node n : super.getGraph().getNeighbors(node)) {
							if(nodeLevels.containsKey(n)) {
								if(nodeLevels.get(n) < lowestLevel) {
									lowestLevel = nodeLevels.get(n);
									parentNode = n;
									levelFound = true;
								}
								
							}else {
								nodes.offer(n);
							}
						}
												
						if(!levelFound || parentNode == null) {
							throw new Exception("all nodes must have a parent");
						}
						
						nodeLevels.put(node, nodeLevels.get(parentNode) + 1);
						
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
				
				
				System.err.println(nodeLevels);
				
				
				Set<Node> drawnNodes = new HashSet<>();
				
				// How many lone child nodes a node has.
				Map<Node, Integer> loneChildNodeCount = new HashMap<>();
				
				// Get the lone child nodes of each mainBranch node.
				for(Node node : this.mainBranch){
					for(Node childNode : node.getFromNodes()){
						if(isLoneNode(childNode)){
							Integer count = loneChildNodeCount.get(node) == null ? 1 : loneChildNodeCount.get(node) + 1; 
							loneChildNodeCount.put(node, count);
						}
					}
					
					for(Node childNode : node.getToNodes()){   
						if(isLoneNode(childNode)){
							Integer count = loneChildNodeCount.get(node) == null ? 1 : loneChildNodeCount.get(node) + 1; 
							loneChildNodeCount.put(node, count);
						}
					}
				} 
				
				Map<Node, Integer> currentLoneChildNode = new HashMap<>();
				
				for(Node node : loneChildNodeCount.keySet()){
					currentLoneChildNode.put(node, 1);
				}
				
				
				Set<Node> nodesToRemoveFromRemNodes = new HashSet<>();
				
				// Draw lone nodes off mainBranch.
				for(Node node : remNodes){
					if(isLoneNode(node) && isFirstLevel(node)){
						// Position near that node on the main branch 
						Node parentNode = node.getFromNodeCount() > 0 ? node.getFromNodes().get(0) : (node.getToNodeCount() > 0 ? node.getToNodes().get(0) : null);
						if(parentNode != null){
							Integer cur = currentLoneChildNode.get(parentNode);
							double offset = cur * 5;
							setLocation(node, super.getX(parentNode) - (cur * 60) - offset, 100);
							currentLoneChildNode.put(parentNode, currentLoneChildNode.get(parentNode) + 1);
							drawnNodes.add(node);
						}else{
							setLocation(node, Math.random() * Visualiser.LAYOUT_WIDTH, Visualiser.LAYOUT_HEIGHT);
							drawnNodes.add(node);
						}
												
						nodesToRemoveFromRemNodes.add(node);
					}
				}
				
				for(Node node : nodesToRemoveFromRemNodes){
					remNodes.remove(node);
				}
				
				Set<Node> firstLevelNodes = new HashSet<>();				
				
				Map<Node, Integer> childNodeCount = new HashMap<>();
				Map<Node, Integer> currentChildNodeCount = new HashMap<>(); 
				
				// First time positioning the nodes.
				for(Node node : remNodes){
					Node parentNode = node.getParentNode(mainBranch);
					if(parentNode != null){	
						
						int siblingCount = 0;
						
						if(!childNodeCount.containsKey(parentNode)){
							siblingCount = getSiblingCount(node, remNodes, parentNode);
							childNodeCount.put(parentNode, siblingCount);								
						}else{
							siblingCount = childNodeCount.get(parentNode);
						}
//							
						int currentChildNode = 0;
						
						if(currentChildNodeCount.containsKey(parentNode)){
							currentChildNode = currentChildNodeCount.get(parentNode);
							currentChildNodeCount.put(parentNode, currentChildNodeCount.get(parentNode) + 1);
						}else{
							currentChildNodeCount.put(parentNode, 1);
						}
													
						double x = super.getX(parentNode) - (currentChildNode * 60.0) + (siblingCount * 60.0 / 2.0);
						double y = 350;
						
//						if(isConnectedToDrawnNodes(node, remNodes, drawnNodes)){
//							y = super.getY(getConnectedDrawnNode(node, remNodes, drawnNodes)) + 150 + 50;
//							
//						}
						
						setLocation(node, x, y);							
						firstLevelNodes.add(node);
						drawnNodes.add(node);
					}
				
				}
				
				
				Set<Node> fixedSiblings = new HashSet<>();
				
				// Reposition sibling nodes.
				for(Node node : remNodes){					
					Node parentNode = node.getParentNode(mainBranch);
					if(parentNode != null){
						if(getSiblingCount(node, remNodes, parentNode) > 0){ // Only reposition if the node has siblings it can switch with.
							Set<Node> siblings = getSiblings(node, remNodes, parentNode);
							Node neighbour = getNeighbour(node, siblings, remNodes);							
							
							if(neighbour != null){
								Node siblingClosestToMyNode = null;
								double closestSiblingDistance = Double.MAX_VALUE;
								
								for(Node sibling : siblings){
									if(!fixedSiblings.contains(sibling)){ // This sibling is already in the correct position, so skip it.
										double siblingToNeighbour = Math.abs((getX(neighbour) - getX(sibling)));
										if(siblingToNeighbour < Math.abs((getX(neighbour) - getX(node)))){ //sibling is closer to neighbour
											if(siblingToNeighbour < closestSiblingDistance){
												siblingClosestToMyNode = sibling;
												closestSiblingDistance = siblingToNeighbour;
											}
										}
									}
								}
								
								if(siblingClosestToMyNode != null){
									double nodeX = getX(node);
									double siblingNodeX = getX(siblingClosestToMyNode);
									fixedSiblings.add(node);
									setLocation(node, siblingNodeX, 350);
									setLocation(siblingClosestToMyNode, nodeX, 350);
								}
							}
						}
					}
				}							
				
				// Move node further up levels (higher Y coordinate), if node a:
				//    1) connects to other nodes in the same level, and 
				//    2) does not have siblings, and 
				//    3) the connected node (to be repositioned) has siblings (so it can't be moved).
				for(Node node : remNodes){
					Node parentNode = node.getParentNode(mainBranch);
					
					if(parentNode != null) {
						if(getSiblingCount(node, remNodes, parentNode) == 0) {
							
							Set<Node> sameLevelNodes = getSameLevelNodes(node, nodeLevels);
							if(!sameLevelNodes.isEmpty()) {
								
								
								boolean shouldMove = false;
								Node neighbourNode = null;
								
								for(Node n : sameLevelNodes) {
									Node sameLevelNodeParent = getParent(n, remNodes, nodeLevels);
									if(sameLevelNodeParent != null && getSiblingCount(n, remNodes, sameLevelNodeParent) > 0) {
										shouldMove = true;
										neighbourNode = n;
										break;
									}
								}
								
								
								if(shouldMove) {
									setLocation(node, getX(node), getY(neighbourNode) + 150);
								}
							}
						}
					}
				}
			
				
				// Draw second level nodes
				for(Node node : firstLevelNodes){
					
					for(Node neighbor : super.getGraph().getNeighbors(node)){
						if(!mainBranch.contains(neighbor) && !firstLevelNodes.contains(neighbor)){
							setLocation(neighbor, super.getX(node), 500);
							drawnNodes.add(node);
						}
					}
					
				}
				
				done = true;

			}
			
		}
		
	}
	
	private int getSiblingCount(Node node, Set<Node> nodes, Node parentNode){
		int siblingCount = 0;
		
		for(Node n : nodes){
			if(n != node && super.getGraph().isNeighbor(n,  parentNode)){
				siblingCount++;
			}
		}
		return siblingCount;
	}
	
	private Node getParent(Node node, Set<Node> nodes, Map<Node, Integer> nodeLevels) {
		
		for(Node n : this.mainBranch){
			if(n != null && n != node && super.getGraph().isNeighbor(n,  node)){
				if(nodeLevels.get(n) == nodeLevels.get(node) - 1) {
					return n;
				}
			}
		}
		
		for(Node n : nodes){
			if(n != null && n != node && super.getGraph().isNeighbor(n,  node)){
				if(nodeLevels.get(n) == nodeLevels.get(node) - 1) {
					return n;
				}
			}
		}
		
		
		return null;
	}
	
	private Set<Node> getSiblings(Node node, Set<Node> nodes, Node parentNode){
		Set<Node> siblings = new HashSet<>();
		
		for(Node n : nodes){
			if(n != node && super.getGraph().isNeighbor(n,  parentNode)){
				siblings.add(n);
			}
		}
		return siblings;
	}
	
	private Node getNeighbour(Node node, Set<Node> siblings, Set<Node> remNodes){
		
		for(Node n : remNodes){
			if(n != node && super.getGraph().isNeighbor(node, n) && !isLoneNode(n) && !isSiblingConnectedToNode(n, siblings)){
				return n;
			}
		}
		
		return null;
	}
	
	private boolean isSiblingConnectedToNode(Node node, Set<Node> siblings){
		for(Node sibling : siblings){
			if(super.getGraph().isNeighbor(node, sibling)){
				return true;
			}
		}
		
		return false;
	}

	private boolean isLoneNode(Node node){
		if(node.getFromNodeCount() + node.getToNodeCount() == 1){
			return true;
		}else{
			return false;
		}
	}
	
//	private boolean isConnectedToDrawnNodes(Node node, Set<Node> remNodes, Set<Node> drawnNodes){
//		
//		for(Node n : super.getGraph().getNeighbors(node)){
//			if(remNodes.contains(n) && drawnNodes.contains(n)){
//				return true;
//			}
//		}		
//		
//		return false;
//	}
//	
//	private Node getConnectedDrawnNode(Node node, Set<Node> remNodes, Set<Node> drawnNodes){
//		
//		for(Node n : super.getGraph().getNeighbors(node)){
//			if(remNodes.contains(n) && drawnNodes.contains(n)){
//				return n;
//			}
//		}		
//		
//		return null;
//	}
	
	private Set<Node> getSameLevelNodes(Node node, Map<Node, Integer> nodeLevels) {
		
		Set<Node> nodes = new HashSet<>();
		
		for(Node n : super.getGraph().getNeighbors(node)) {
			if(nodeLevels.get(n) == nodeLevels.get(node)) {
				nodes.add(n);
			}
		}
		return nodes;
	}
	
	private boolean isFirstLevel(Node node) {
		for(Node neighbour : getGraph().getNeighbors(node)) {
			if(this.mainBranch.contains(neighbour)) {
				return true;
			}
		}
		
		return false;
		
	}
	
	private int getMaxLevel(Map<Node, Integer> nodeLevels) {
		int maxLevel = 0;
		
		for(Integer level : nodeLevels.values()) {
			if(level > maxLevel) {
				maxLevel = level;
			}
		}
		
		return maxLevel;
	}
	
	
}
