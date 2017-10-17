package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.net.URL;
import java.util.List;
import java.util.Stack;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.commons.collections15.Transformer;

import Visual.Edge;
import Visual.FunctorNode;
import Visual.Node;
import Visual.SimpleGraphView;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.DirectionalEdgeArrowTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.BasicEdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.DefaultEdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;

public class GraphExtension {

	public static final int mainArgRadius = 30;
	public static final int listOpRadius = 22;
	public static final int arithmeticOpRadius = 20;
	public static final int functorOpHeight = 20;
	public static final int variableRadius = 5;
	public static final int listPredicateWidth = 30;
	public static final int listPredicateHeight = 25;
	
	public static JPanel visualise(List<Node> predicateClauses, Stack<Node> mainBranch, boolean directedEdgeEnabled){
		SimpleGraphView sgv = new SimpleGraphView(predicateClauses, directedEdgeEnabled); //We create our graph in here
		// The Layout<V, E> is parameterized by the vertex and edge types
//		Layout<Node, String> layout = new CircleLayout<>(sgv.graph);
//		FRLayout<Node, String> layout = new FRLayout<>(sgv.graph);
//		FRLayout2<Node, String> layout = new FRLayout2<>(sgv.graph);
		Layout<Node, Edge> layout = new ISOMLayout<>(sgv.graph);
		
//		ExtendedISOMLayout<Node, Edge> layout = new ExtendedISOMLayout(sgv.graph, mainBranch);
//		SpringLayout<Node, Edge> layout = new SpringLayout<>(sgv.graph);
//		layout.initialize();
//		
//		ISOMLayout<Node, Edge> layout = new CustomLayout(sgv.graph, mainBranch);
//		ISOMLayout<Node, Edge> layout = new ExtendedIsomLayout2(sgv.graph, mainBranch);
//		
//		Layout<Node, Edge> layout; //create a layout
//		layout = new TreeLayout<Node, Edge>((Forest<Node, Edge>) sgv.graph); 
		
//		Layout<Node, String> layout = new KKLayout<>(sgv.graph);
//		DAGLayout<Node, Edge> layout = new DAGLayout<>(sgv.graph);
//		layout.initialize();
		
		//-----FRLayout configuration------//
//		layout.setRepulsionMultiplier(0.7);
//		layout.setMaxIterations(10000000);
//		layout.setRepulsionMultiplier(1);
		//-----FRLayout configuration------//

		
		
//		layout.setSize(new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT)); // sets the initial size of the space
		layout.setSize(new Dimension(Visualiser.LAYOUT_WIDTH, Visualiser.LAYOUT_HEIGHT));
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
//		BasicVisualizationServer<Node,String> vv = new BasicVisualizationServer<Node,String>(layout);
		VisualizationViewer<Node,Edge> vv = new VisualizationViewer<Node,Edge>(layout, new Dimension(Visualiser.LAYOUT_WIDTH, Visualiser.LAYOUT_HEIGHT));
//		vv.setPreferredSize(new Dimension(LAYOUT_WIDTH - 500, LAYOUT_HEIGHT)); //Sets the viewing area size
		vv.setPreferredSize(new Dimension(Visualiser.LAYOUT_WIDTH, Visualiser.LAYOUT_HEIGHT));
//		vv.setSize(new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT - 100)); //Sets the viewing area size
	//	vv.setPreferredSize(new Dimension(600, 600)); //Sets the viewing area size
	//	vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		 // Set up a new stroke Transformer for the edges
		float dash[] = {10.0f};
		final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
		Transformer<String, Stroke> edgeStrokeTransformer = new Transformer<String, Stroke>() {
			public Stroke transform(String s) {
				return edgeStroke;
			}
		};
		
//		vv.getRenderer().setEdgeLabelRenderer(new BasicEdgeLabelRenderer<Node, Edge>() {
//			@Override
//			public void labelEdge(RenderContext<Node, Edge> rc, Layout<Node, Edge> layout, Edge e, String label) {
//		        call(rc, layout, e, label, "fromLabel", 50);
//		        call(rc, layout, e, label, "toLabel", 45);
//		        
//		    }
//			
//			private void call(RenderContext<Node, Edge> rc, Layout<Node, Edge> layout, Edge e, String label, String labelSide, int distDisplacement) {
//				if(label == null || label.length() == 0) return;
//		    	
//		    	Graph<Node, Edge> graph = layout.getGraph();
//		        // don't draw edge if either incident vertex is not drawn
//		        Pair<Node> endpoints = graph.getEndpoints(e);
//		        Node v1 = endpoints.getFirst();
//		        Node v2 = endpoints.getSecond();
//		        if (!rc.getEdgeIncludePredicate().evaluate(Context.<Graph<Node, Edge>,Edge>getInstance(graph,e)))
//		            return;
//
//		        if (!rc.getVertexIncludePredicate().evaluate(Context.<Graph<Node, Edge>,Node>getInstance(graph,v1)) || 
//		            !rc.getVertexIncludePredicate().evaluate(Context.<Graph<Node, Edge>,Node>getInstance(graph,v2)))
//		            return;
//
//		        Point2D p1 = layout.transform(v1);
//		        Point2D p2 = layout.transform(v2);
//		        p1 = rc.getMultiLayerTransformer().transform(Layer.LAYOUT, p1);
//		        p2 = rc.getMultiLayerTransformer().transform(Layer.LAYOUT, p2);
//		        float x1 = (float) p1.getX();
//		        float y1 = (float) p1.getY();
//		        float x2 = (float) p2.getX();
//		        float y2 = (float) p2.getY();
//
//		        GraphicsDecorator g = rc.getGraphicsContext();
//		        float distX = x2 - x1;
//		        float distY = y2 - y1;
//		        double totalLength = Math.sqrt(distX * distX + distY * distY);
//
//		        double closeness = rc.getEdgeLabelClosenessTransformer().transform(Context.<Graph<Node,Edge>,Edge>getInstance(graph, e)).doubleValue();
//		        
////		        int posX = (int) (x1 + (closeness) * distX);
////		        int posY = (int) (y1 + (closeness) * distY);
//		        
//		        Component component = prepareRenderer(rc, rc.getEdgeLabelRenderer(), labelSide, 
//		                rc.getPickedEdgeState().isPicked(e), e);
//
//		        Dimension d = component.getPreferredSize();
//		        
//		        FontMetrics fm = vv.getFontMetrics(vv.getFont());
////		        fm = rc.getGraphicsContext().getFontMetrics(rc.getGraphicsContext().getFont());
//		        
//		        float f = 0 ;
//		        if(labelSide.equals("fromLabel")) {
//		        	Node.TYPE type = v1.getType();
//		        	String fromLabel = e.fromLabel.contains("_") ? e.fromLabel.split("_")[0] : e.fromLabel;
//		        	
//		        	float tunedOffset = 1.3f;
//		        	if(type == Node.TYPE.Operator) {
//		        		distDisplacement = (int) (fm.stringWidth(fromLabel) / 2 + arithmeticOpRadius * tunedOffset);
//		        	}else if(type == Node.TYPE.Functor) {
//		        		int width = fm.stringWidth(v1.getName().trim());
//		        		int height = 0; 
//		        				
//		        		if(FunctorNode.isListPredicate(v1.getName().trim())) {
//		        			width = GraphExtension.listPredicateWidth;
//		        			height = GraphExtension.listPredicateHeight;
//			        		
//		        		}else {
//		        			width = (int)(width * 0.8);
//			        		if(width < 20) {
//			        			width = 20;
//			        		}else if(width > 25) {
//			        			width *= 0.8;
//			        		}
//			        		width = width > 35 ? 35 : width;
//			        		height = functorOpHeight;
//		        		}
////		        		
////		        		distDisplacement = (int) (fm.stringWidth(fromLabel) / 2 + width + 8);
////		        		
//		        		float angle = getAngle(p2, p1);
//		        		angle = angle > 180 ? angle - 180 : angle;
//		        		
//		        		if(angle >= 90) {
//		        			float angleDiff = angle - 90;
//		        			angle = 90 - angleDiff;		        			
//		        		}
//		        		
//		        		float angle1 = getAngle(new Point((int)(p1.getX() - width / 2), (int)(p1.getY() - height / 2)), p1);
//		        		
//		        		if(0 <= angle && angle < angle1) {
//		        			if(v1.getName().equals("roadroad") && v2.getName().equals("+")) {
//		        				System.out.println("width: " + width);
//		        				System.out.println("angle: " + getAngle(p2, p1));
//		        				System.out.println("angle1: " + angle1);
//		        				System.out.println("distDisplacement: " + (int) (width / 2 / Math.cos(Math.toRadians(angle))));
//		        			}
//		        			distDisplacement = (int) ((int) (width / Math.cos(Math.toRadians(angle))) * 1.3 + fm.stringWidth(fromLabel) / 2 );
//		        		}else if(angle1 <= angle && angle <= 90) {
//		        			angle = 90 - angle;
//		        			distDisplacement = (int) ((int) (height / Math.cos(Math.toRadians(angle))) * 1.3 + fm.stringWidth(fromLabel) / 2 );
////		        			distDisplacement = (int) (functorOpHeight / Math.cos(Math.toRadians(angle)));
//		        			if((v1.getName().equals("roadroad") || v1.getName().equals("roadroadroadroad")) && v2.getName().equals("+")) {		        				
//		        				System.out.println("between angle1 and 90");
//		        				System.out.println("angle: " + angle);
//		        				System.out.println("distDisplacement: " + distDisplacement);
//		        			}
//		        		}	        		
//		        	}if(type == Node.TYPE.ListOperator) { //Done
//		        		int width = listOpRadius;		        		        	
//		        		
//		        		float angle = getAngle(p2, p1);
//		        		angle = angle > 180 ? angle - 180 : angle;
//		        		
//		        		if(angle >= 90) {
//		        			float angleDiff = angle - 90;
//		        			angle = 90 - angleDiff;		        			
//		        		}
//		        		
//		        		float angle1 = getAngle(new Point((int)(p1.getX() - width / 2), (int)(p1.getY() - listOpRadius / 2)), p1);
//		        		
//		        		if(0 <= angle && angle < angle1) {
//		        			distDisplacement = (int) ((int) (width / Math.cos(Math.toRadians(angle))) * 1.3 + fm.stringWidth(fromLabel) / 2 );
//		        		}else if(angle1 <= angle && angle <= 90) {
//		        			angle = 90 - angle;
//		        			distDisplacement = (int) ((int) (listOpRadius / Math.cos(Math.toRadians(angle))) * 1.3 + fm.stringWidth(fromLabel) / 2 );
//		        		}	        
//		        	}
//		        	
//		        	f = (float) (distDisplacement / totalLength);
//		        	
//		        }else {
//		        	Node.TYPE type = v2.getType();
//		        	String toLabel = e.toLabel.contains("_") ? e.toLabel.split("_")[0] : e.toLabel;
//		        	float tunedOffset = 1.4f;
//		        	if(type == Node.TYPE.Operator) {
//		        		distDisplacement = (int) (fm.stringWidth(toLabel) / 2 + arithmeticOpRadius * tunedOffset);
//		        	}else if(type == Node.TYPE.Functor) {
////		        		System.out.println(v2.getName());
////		        		int width = fm.stringWidth(v2.getName().trim());		        		
////		        		width = (int)(width * 0.8);
////		        		if(width < 20) {
////		        			width = 20;
////		        		}else if(width > 25) {
////		        			width *= 0.8;
////		        		}
////		        		
////		        		distDisplacement = (int) (fm.stringWidth(e.toLabel) / 2 + width + 5);
//		        		
//		        		int width = fm.stringWidth(v2.getName().trim());	
//		        		int height = 0; 
//		        		
//		        		
//		        		
//		        		if(FunctorNode.isListPredicate(v2.getName().trim())) {
//		        			width = GraphExtension.listPredicateWidth;
//		        			height = GraphExtension.listPredicateHeight;
//		        			
//		        		}else {
//		        			width = (int)(width * 0.8);
//			        		if(width < 20) {
//			        			width = 20;
//			        		}else if(width > 25) {
//			        			width *= 0.8;
//			        		}
//			        		width = width > 35 ? 35 : width;
//			        		height = functorOpHeight;
//		        		}
//		        		
////		        		
////		        		distDisplacement = (int) (fm.stringWidth(toLabel) / 2 + width + 8);
//		        		
//		        		float angle = getAngle(p1, p2);
//		        		angle = angle > 180 ? angle - 180 : angle;
//		        		
//		        		if(angle >= 90) {
//		        			float angleDiff = angle - 90;
//		        			angle = 90 - angleDiff;		        			
//		        		}
//		        		
//		        		float angle1 = getAngle(new Point((int)(p2.getX() - width / 2), (int)(p2.getY() - height / 2)), p2);
//		        		
//		        		if(0 <= angle && angle < angle1) {
//		        			distDisplacement = (int) ((int) (width / Math.cos(Math.toRadians(angle))) * 1.3 + fm.stringWidth(toLabel) / 2 );
//		        		}else if(angle1 <= angle && angle <= 90) {
//		        			angle = 90 - angle;
//		        			distDisplacement = (int) ((int) (height / Math.cos(Math.toRadians(angle))) * 1.3 + fm.stringWidth(toLabel) / 2 );
//		        		}
//		        	}else if(type == Node.TYPE.ListOperator) {
//
//		        		int width = listOpRadius;		        	
//		        		
//		        		float angle = getAngle(p1, p2);
//		        		angle = angle >= 180 ? angle - 180 : angle;
//		        		
//		        		if(angle >= 90) {
//		        			float angleDiff = angle - 90;
//		        			angle = 90 - angleDiff;		        			
//		        		}
//		        		
//		        		float angle1 = getAngle(new Point((int)(p2.getX() - width / 2), (int)(p2.getY() - listOpRadius / 2)), p2);
//		        		
//		        		if(0 <= angle && angle < angle1) {
//		        			distDisplacement = (int) ((int) (width / Math.cos(Math.toRadians(angle))) * 1.3 + fm.stringWidth(toLabel) / 2 );
//		        		}else if(angle1 <= angle && angle <= 90) {
//		        			angle = 90 - angle;
//		        			distDisplacement = (int) ((int) (listOpRadius / Math.cos(Math.toRadians(angle))) * 1.3 + fm.stringWidth(toLabel) / 2 );
//		        		}
//		        	}
//		        	
//		        	f = (float) ((totalLength - distDisplacement) / totalLength);
//		        	
//		        }
//		        
//				
//		        int posX = (int) (x1 + distX * f);
//		        int posY = (int) (y1 + distY * f);
//		        
//		        int xDisplacement = (int) (rc.getLabelOffset() * (distY / totalLength));
//		        int yDisplacement = (int) (rc.getLabelOffset() * (-distX / totalLength));
//
//		        Shape edgeShape = rc.getEdgeShapeTransformer().transform(Context.<Graph<Node,Edge>,Edge>getInstance(graph, e));
//		        
//		        double parallelOffset = 1;
//
//		        parallelOffset += rc.getParallelEdgeIndexFunction().getIndex(graph, e);
//
//		        parallelOffset *= d.height;
//		        if(edgeShape instanceof Ellipse2D) {
//		            parallelOffset += edgeShape.getBounds().getHeight();
//		            parallelOffset = -parallelOffset;
//		        }
//		        
//		        AffineTransform old = g.getTransform();
//		        AffineTransform xform = new AffineTransform(old);
//		        xform.translate(posX + xDisplacement, posY + yDisplacement);
////		        xform.translate(x1, y1);
//		        double dx = x2 - x1;
//		        double dy = y2 - y1;
//		        if(rc.getEdgeLabelRenderer().isRotateEdgeLabels()) {
//		            double theta = Math.atan2(dy, dx);
//		            if(dx < 0) {
//		                theta += Math.PI;
//		            }
//		            xform.rotate(theta);
//		        }
//		        if(dx < 0) {
//		            parallelOffset = -parallelOffset;
//		        }
//		        
//		        xform.translate(-d.width/2, -(d.height/2-parallelOffset));
//		        
//		        g.setTransform(xform);
//		        g.draw(component, rc.getRendererPane(), 0, 0, d.width, d.height, true);
//
//		        g.setTransform(old);
//			}
//		});
//	    
//		DefaultEdgeLabelRenderer edgeLabelRenderer = new DefaultEdgeLabelRenderer(Color.black){
//			
//			@Override
//	        public <E> Component getEdgeLabelRendererComponent(
//	            JComponent vv, Object value, Font font, 
//	            boolean isSelected, E edge) 
//	        {
//	            super.getEdgeLabelRendererComponent(
//	                vv, value, font, isSelected, edge);
////	            this.setForeground(Color.BLUE);	            
//	            
////	            VisualizationViewer vvv = ((VisualizationViewer)vv);
////	            Layout<Node, Edge> layout = vvv.getGraphLayout();
////	            Graph g = vvv.getGraphLayout().getGraph();
////	            Object[] ee = g.getEdges().toArray();
////	            Edge e = (Edge) ee[0];
//	            
//	            Edge e = (Edge) edge;
//	            
//	            if(value.toString().equals("fromLabel")) {
//		        	String fromLabel = e.fromLabel.contains("_") ? e.fromLabel.split("_")[0] : e.fromLabel;
//	            	this.setText(((Edge) edge).fromLabel);
//	            }else if(value.toString().equals("toLabel")) {
//		        	String toLabel = e.toLabel.contains("_") ? e.toLabel.split("_")[0] : e.toLabel;
//	            	this.setText(toLabel);
////	            	this.setText("toLabel");
//	            }else {
//	            	System.err.println("edge label not recognised");
//	            }
//	            
//	            return this;
//	        }
//	        
//	       
//	        
//	    };
			
	   
	    DefaultVertexLabelRenderer vertexLabelRenderer = new DefaultVertexLabelRenderer(Color.black){
	        @Override
	        public <V> Component getVertexLabelRendererComponent(
	            JComponent vv, Object value, Font font, 
	            boolean isSelected, V vertex) 
	        {
	            super.getVertexLabelRendererComponent(
	                vv, value, font, isSelected, vertex);
	            setForeground(Color.black);
	            
	            Node n = (Node) vertex;
//	            if(n.isMainArg){
//	            	this.setText(n.mainArgNo + ". " + this.getText());
//	            }
	            
//	            if(n.getNodeType() == Node.TYPE.Functor){
//	            	this.setText(this.getText() + "()");
//	            }
//	            
	            if(n.getType() == Node.TYPE.Functor && !FunctorNode.isListPredicate(n.getName())) {

	        		FontMetrics fm = vv.getFontMetrics(vv.getFont());
	        		int width = fm.stringWidth(n.getName().trim());
	        		
	        		width = (int)(width * 0.8);
	        		if(width < 20) {
	        			width = 20;
	        		}else if(width > 25) {
	        			width *= 0.8;
	        		}
	        		
	        		String text = this.getText();
	        		
	        		if(width > 35) {
	        			width = 35;
	        			int charWidth = fm.stringWidth(n.getName().trim()) / n.getName().length();
	        			int chars = width / charWidth;
	        			text = this.getText().substring(0, chars) + "...";
	        		}
	        		
//	        		this.setText("<html><br>" + n.getName().trim() + "</html>");
	        		this.setText(text);
	        		
	            }
	            else { 
	            	if(!n.isMainArg && n.getType() == Node.TYPE.Variable){
	            		this.setText("<html><br>" + this.getText() + "</html>");
	            	}
	            }
	            
//	            vv.getGraphics().setColor(Color.blue);
//	            vv.getGraphics().fillRect(this.getX(), this.getY(), 10, 10);	           
	            
	            return this;
	        }
	        
	    };
	    
	 // Transformer maps the vertex number to a vertex property
	    Transformer<Node,Paint> vertexColor = new Transformer<Node, Paint>() {
	        public Paint transform(Node node){
//	        	return node.getNodeColor();
	        	return Color.white;
	        }
	    };
	    
	    Transformer<Node,Shape> vertexShape = new Transformer<Node, Shape>(){
	        public Shape transform(Node i){
	        	Shape shape = null;
	        	Node.TYPE nodeType = i.getType();
	        	
	        	
	        	if(i.isMainArg){	        	
	        		shape = new Ellipse2D.Double(-mainArgRadius/2, -mainArgRadius/2, mainArgRadius, mainArgRadius);
	        	}else if(nodeType == Node.TYPE.ListOperator){
	        		shape = new Rectangle(-listOpRadius/2, -listOpRadius/2, listOpRadius, listOpRadius);
	        	}else if(nodeType == Node.TYPE.Functor){
	//        		shape = new Rectangle(-15, -15, 20, 20);
//	        		int width = (i.getName().length() < 5) ? 5 : i.getName().length(); // so the node doesn't look too small.        		
//	        		width = width > 10 ? 10 : width; //enforce max rectangle width.
	        		
	        		FontMetrics fm = vv.getFontMetrics(vv.getFont());
	        		int width = fm.stringWidth(i.getName().trim());
	        		
	        		width = (int)(width * 0.8);
	        		if(width < 20) {
	        			width = 20;
	        		}else if(width > 25) {
	        			width *= 0.8;
	        		}
	        		
	        		
	        		width = width > 35 ? 35 : width;
	        		
//	        		width = (int)(width * 4.5);
//	        		width += 3; //for the extra "()" 
//	        		width += i.isMainArg ? 3: 0; //for the extra arg number at the start.
	        		
	        		if(FunctorNode.isListPredicate(i.getName())){
//	        			shape = new Rectangle(-15, -15, 30, 25);
	        			shape = new Rectangle(-listPredicateWidth/2, -listPredicateHeight/2, listPredicateWidth, listPredicateHeight);
	        		}else{
//	        			shape = new Rectangle(-15, -15, width, functorNodeHeight);
	        			shape = new Rectangle(-width/2, -functorOpHeight/2, width, functorOpHeight);
	        		}
	        		
	        	}else if(nodeType == Node.TYPE.Operator){
	        		shape = new Rectangle(-arithmeticOpRadius/2, -arithmeticOpRadius/2, arithmeticOpRadius, arithmeticOpRadius);
	        	}else if(nodeType == Node.TYPE.Variable){
	        		if(i.getFromNodeCount() + i.getToNodeCount() >= 3){ // A variable in a Junction.
	        			shape = new Ellipse2D.Double(-variableRadius/2, -variableRadius/2, variableRadius, variableRadius);	   	        			
	        		}else {
	        			final GeneralPath p0 = new GeneralPath();
						int sizeFactor = 10;
						p0.moveTo(0.0f, -sizeFactor); //start at point 1
						p0.lineTo(sizeFactor*2, sizeFactor); //go to point 2
						p0.lineTo(-sizeFactor*2, sizeFactor); //go to point 3
						p0.closePath();
		        		shape = p0;
	        		}	   
	        	}else if(nodeType == Node.TYPE.Atom){ // A variable not in a Junction i.e. a triangle.
					final GeneralPath p0 = new GeneralPath();
					int sizeFactor = 10;
					p0.moveTo(0.0f, -sizeFactor); //start at point 1
					p0.lineTo(sizeFactor*2, sizeFactor); //go to point 2
					p0.lineTo(-sizeFactor*2, sizeFactor); //go to point 3
					p0.closePath();
	        		shape = p0;
        		}else{
	        		shape = new Ellipse2D.Double(-10, -10, 20, 20);
	        	}
	        	
	            // in this case, the vertex is twice as large
	            return AffineTransform.getScaleInstance(2, 2).createTransformedShape(shape);
	            
	        }
	    };
	    
	    Transformer<Node, Icon> vertexIcon = new Transformer<Node, Icon>(){

			@Override
			public Icon transform(Node node) {
				try{
					
					if(node.isMainArg){					
						URL url = Visualiser.class.getResource("/resources/mainArgArrows.svg");
						SvgImage svgimage = new SvgImage(url);
				        return new ImageIcon(svgimage.getImage(45, 45));
				        
					}else if(node.getType() == Node.TYPE.ListOperator){
						URL url = Visualiser.class.getResource("/resources/listProcessor.png");
						ImageIcon icon = new ImageIcon(url);
						
						return new Icon() {
							@Override
			                public int getIconHeight() {
			                    return 45;
			                }
							
							@Override
			                public int getIconWidth() {
			                    return 45;
			                }
			                
			                @Override
			                public void paintIcon(Component c, Graphics g, int x, int y) {
			                	int offset =0;
			                    g.drawImage(icon.getImage(), x - offset, y - offset - 2, getIconWidth(), getIconHeight(), null);
			                }
			            };
			            
					}else if(node.getType() == Node.TYPE.Functor){
						String functor = node.getName();
						
						if(FunctorNode.isListPredicate(functor)){
//							ImageIcon icon = new ImageIcon("./resources/not_member.png");
							ImageIcon icon = new ImageIcon(FunctorNode.getIconPath(functor));
							// System.err.println(icon.getIconWidth() + " " + icon.getIconHeight());
//							return icon;
							return new Icon() {
				                public int getIconHeight() {
//				                    return 204;
				                	return 51;
				                }

				                public int getIconWidth() {
//				                    return 244;
				                	return 61;
				                }
				                
				                int offset = 0;
				                public void paintIcon(Component c, Graphics g, int x, int y) {
				                    g.drawImage(icon.getImage(), x , y - offset, getIconWidth(), getIconHeight(), null);
				                }
				            };
						}
						
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
				
				return null;
			}
	    	
	    };

	    Transformer<Node, String> vertexLabelTransformer = new Transformer<Node, String>(){

			@Override
			public String transform(Node node) {
				// TODO Auto-generated method stub
				Node.TYPE type = node.getType();
				if(type == Node.TYPE.ListOperator || (FunctorNode.isListPredicate(node.getName()))){
					return null;
				}else{
					return node.getName();
				}
			}
	    	
	    };
	    
	    vv.getRenderContext().setEdgeLabelClosenessTransformer(new Transformer<Context<Graph<Node, Edge>, Edge>, Number>() {
			@Override
			public Number transform(Context<Graph<Node, Edge>, Edge> arg0) {          
                return 0.5; //halfway on an edge.
			}
        });
	    
	    
	    Transformer<Context<Graph<Node,Edge>,Edge>, Shape> edgeShapeTransformer = new Transformer<Context<Graph<Node,Edge>,Edge>, Shape>(){

			@Override
			public Shape transform(Context<Graph<Node, Edge>, Edge> context) {
				// TODO Auto-generated method stub
//				Line<Node, Edge> e = new EdgeShape.Line<Node, Edge>();
				
//				if(!vertical(layout, context.element) && !horizontal(layout, context.element)) {
//					
//					AbstractEdgeShapeTransformer<Node, Edge> e = new AbstractEdgeShapeTransformer<Node, Edge>(){
//						@Override
//						public Shape transform(Context<Graph<Node, Edge>, Edge> arg0) {
//							// draw orthogonal edges
//							return new Rectangle();
//						}
//						
//					};
//					return (e).transform(context);
//				}
					
				return (new EdgeShape.Line<Node, Edge>()).transform(context);
			}
	    };
	    
//		vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Node, Edge>());
//		vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.CubicCurve<Node, Edge>());
//		vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.BentLine<Node, Edge>());
	    vv.getRenderContext().setEdgeShapeTransformer(edgeShapeTransformer);
//	    vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Orthogonal<Node, Edge>());
//		vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
	    
//		vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
//		vv.getRenderContext().setEdgeLabelRenderer(edgeLabelRenderer);
//		vv.getRenderContext().setLabelOffset(-5);
		 
		vv.getRenderContext().setEdgeArrowTransformer(new DirectionalEdgeArrowTransformer<Node, Edge>(5, 5, 0));
	    vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);
	    vv.getRenderContext().setVertexShapeTransformer(vertexShape);    
	    vv.getRenderContext().setVertexLabelRenderer(vertexLabelRenderer);
	    vv.getRenderContext().setVertexLabelTransformer(vertexLabelTransformer);
	    vv.getRenderContext().setVertexIconTransformer(vertexIcon);
        vv.getRenderer().setVertexRenderer(new MultiVertexRenderer<>());
	    vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
	   
		return vv;
	}
	
	
		
	
	public static float getAngle(Point2D source, Point2D target) {
		float angle = (float) Math.toDegrees(Math.atan2(target.getY() - source.getY(), target.getX() - source.getX()));
		
		if(angle < 0){
			angle += 360;
		}
		
		return angle;
	}
	

	
	static class MyRenderer implements Renderer.EdgeLabel<Node, Edge>{

		@Override
		public void labelEdge(RenderContext<Node, Edge> context, Layout<Node, Edge> layout, Edge edge, String label) {
			// TODO Auto-generated method stub
		}
		
	}
	
	private static String getNbsp(int spaces){
		String space = "";
		for(int i = 0; i < spaces; i++){
			space += "&nbsp;";
		}
		return space;
	}
	
	public static boolean vertical(Layout<Node, Edge> layout, Edge e) {
		if(Math.abs(layout.transform(e.fromNode).getX() -layout.transform(e.toNode).getX()) < 10) {
			return true;
		}else {
			return false;
		}		
	}
	
	public static boolean horizontal(Layout<Node, Edge> layout, Edge e) {
		if(Math.abs(layout.transform(e.fromNode).getY() -layout.transform(e.toNode).getY()) < 10) {
			return true;
		}else {
			return false;
		}
	}

}
