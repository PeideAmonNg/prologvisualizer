package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Paint;
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
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.DirectionalEdgeArrowTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.DefaultEdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;

public class GraphExtension {
	
	public static JPanel visualise(List<Node> predicateClauses, Stack<Node> mainBranch, boolean directedEdgeEnabled){
		SimpleGraphView sgv = new SimpleGraphView(predicateClauses, directedEdgeEnabled); //We create our graph in here
		// The Layout<V, E> is parameterized by the vertex and edge types
//		Layout<Node, String> layout = new CircleLayout<>(sgv.graph);
//		FRLayout<Node, String> layout = new FRLayout<>(sgv.graph);
//		FRLayout2<Node, String> layout = new FRLayout2<>(sgv.graph);
//		Layout<Node, Edge> layout = new ISOMLayout<>(sgv.graph);
		ISOMLayout<Node, Edge> layout = new CustomLayout(sgv.graph, mainBranch);
//		ExtendedISOMLayout<Node, Edge> layout = new ExtendedISOMLayout(sgv.graph, mainBranch);
//		SpringLayout<Node, Edge> layout = new SpringLayout<>(sgv.graph);
//		layout.initialize();
		
		

		
		
//		
//		Layout<Node, String> layout = new KKLayout<>(sgv.graph);
//		DAGLayout<Node, String> layout = new DAGLayout<>(sgv.graph);
		
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
		
		
		DefaultEdgeLabelRenderer edgeLabelRenderer = new DefaultEdgeLabelRenderer(Color.black){
	        @Override
	        public <E> Component getEdgeLabelRendererComponent(
	            JComponent vv, Object value, Font font, 
	            boolean isSelected, E edge) 
	        {
	            super.getEdgeLabelRendererComponent(
	                vv, value, font, isSelected, edge);
//	            this.setForeground(Color.BLUE);	            
	            
	            VisualizationViewer vvv = ((VisualizationViewer)vv);
	            Layout<Node, Edge> layout = vvv.getGraphLayout();
	            Graph g = vvv.getGraphLayout().getGraph();
	            Object[] ee = g.getEdges().toArray();
//	            Edge e = (Edge) ee[0];
	            
	            Edge e = (Edge) edge;
	            Node fromNode = e.fromNode;
	            Node toNode = e.toNode;
	            
	            Point2D from = layout.transform(fromNode);
	            Point2D to = layout.transform(toNode);
	            
	            // System.err.println("------------------------------>" + from);
	            
	            double x1 = from.getX(), y1 = from.getY(), x2 = to.getX(), y2 = to.getY();
	            double distance = Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	            distance = distance - 35; //radius of nodes.
	            
	            String fromLabel = e.fromLabel, toLabel = e.toLabel, centerLabel = e.centerLabel;
	            
	            if(fromLabel.contains("_")){
	            	fromLabel = fromLabel.split("_")[0];
	            }
	            
	            if(toLabel.contains("_")){
	            	toLabel = toLabel.split("_")[0];
	            }
	            
	            // System.err.println("---" + fromLabel + " --- " + toLabel);
	            
	            double fromLabelSize = e.fromLabel.length() * 5.0, toLabelSize = e.toLabel.length() * 5.0, centerLabelSize = e.centerLabel.length() * 5.0; 
	            
	            int space = (int) ((distance - (fromLabelSize + toLabelSize + centerLabelSize)) / 5.0);
	            
	            String spaceFiller = "";
	            
//	            if(distance <= 250){
//	            	space = (int)(space * 0.7);
//	            }else if(distance <= 350){
//	            	space = (int)(space * 0.8);
//	            }
	            
	            for(int i = 0; i < space; i++){
	            	spaceFiller += "&nbsp;";
	            }
	            
	            
	            double spaceWidth = 3.5;
	            double offset = 0.5;
	            
	            if(x1 <= x2){	            	
//	            	if(centerLabel.equals("")){
//	            		this.setText("<html><strong><font color='red'>" + fromLabel + "</font></strong>" + spaceFiller +
//		            			"<strong><font color='blue'>" + toLabel + "</font></strong></html>");
//	            	}else{
	            		
	            		double spaceLeft = distance - fromLabelSize - centerLabelSize - toLabelSize;
	            		String nbsp = getNbsp(((int)(spaceLeft / 3.0) / 2) - 7);
	            		
	            		String startSpace = getNbsp(1); 
	            		if(distance < 120){
	            			startSpace = "";
	            			nbsp = getNbsp(3);
	            		}
	            		
//	            		this.setText("<html>" + 
//	            				"<strong><font color='red'>" + fromLabel + "</font></strong>" +
//	            				nbsp +
//	            				"<strong><font color='purple'>" + centerLabel + "</font></strong>" +
//	            				nbsp + 
//		            			"<strong><font color='blue'>" + toLabel + "</font></strong>" +
//	            				"</html>");
	            		
	            		this.setText("<html>" + 
	            				fromLabel +
	            				nbsp +
	            				centerLabel + 
	            				nbsp + 
	            				toLabel +
		            			"</html>");
	            		

//	            		double quarter = distance * 0.25;
//	            		double totalOffset = (quarter * offset);
	            		
//	            		if(distance < 200) totalOffset = quarter * offset * 1.5;
//	            		
//	            		double first = ((quarter - (fromLabelSize / 2.0) - totalOffset) / spaceWidth);
//	            		double second = (quarter - (fromLabelSize / 2.0) + (centerLabelSize / 2.0) + totalOffset) / spaceWidth;
//	            		double third = (quarter - ((centerLabelSize / 2.0) + (toLabelSize / 2.0))) / spaceWidth;
//	            		double fourth = ((quarter - (toLabelSize / 2.0)) / spaceWidth);
//	            		
//	            		this.setText("<html>" + 
//	            				getNbsp((int)first) +  
//	            				"<strong><font color='red'>" + fromLabel + "</font></strong>" +
//	            				getNbsp((int)second) +
//	            				"<strong><font color='purple'>" + centerLabel + "</font></strong>" +
//	            				getNbsp((int)third) + 
//		            			getNbsp((int)fourth) + 
//		            			"<strong><font color='blue'>" + toLabel + "</font></strong>" +
//	            				"</html>");
//	            	}
	            	
	            }else{
//	            	if(centerLabel.equals("")){
//	            		this.setText("<html><strong><font color='blue'>" + toLabel + "</font></strong>" + spaceFiller + 
//		            			"<strong><font color='red'>" + fromLabel + "</font></strong></html>");
//	            	}else{
	            	
		            	double spaceLeft = distance - fromLabelSize - centerLabelSize - toLabelSize;
	            		String nbsp = getNbsp(((int)(spaceLeft / 3.0) / 2) - 7);
	            		
	            		String startSpace = getNbsp(6); 
	            		if(distance < 120){
	            			startSpace = "";
	            			nbsp = getNbsp(3);
	            		}
	            		
//	            		this.setText("<html>" +  
//	            				"<strong><font color='blue'>" + toLabel + "</font></strong>" +
//	            				nbsp +
//	            				"<strong><font color='purple'>" + centerLabel + "</font></strong>" +
//	            				nbsp + 
//		            			"<strong><font color='red'>" + fromLabel + "</font></strong>" +
//	            				"</html>");
//	            		
	            		this.setText("<html>" +
	            				toLabel +
	            				nbsp +
	            				centerLabel + 
	            				nbsp +
	            				fromLabel + 
		            			"</html>");
	            		
	            		
//	            		double quarter = distance * 0.25;
//	            		double totalOffset = (quarter * offset);
//	            		
//	            		double first = (quarter - (fromLabelSize / 2.0) - totalOffset) / spaceWidth;
//	            		double second = (quarter - ((fromLabelSize / 2.0) + (centerLabelSize / 2.0)) + totalOffset) / spaceWidth;
//	            		double third = (quarter - ((centerLabelSize / 2.0) + (toLabelSize / 2.0))) / spaceWidth;
//	            		double fourth = (quarter - (toLabelSize / 2.0)) / spaceWidth;
//	            		
//	            		this.setText("<html>" + 
//		            			"<strong><font color='blue'>" + toLabel + "</font></strong>" +
//	            				getNbsp((int)fourth) +  
//	            				getNbsp((int)third) +
//	            				"<strong><font color='purple'>" + centerLabel + "</font></strong>" +
//	            				getNbsp((int)second) + 
//	            				"<strong><font color='red'>" + fromLabel + "</font></strong>" +
//		            			getNbsp((int)first) + 
//	            				"</html>");
//	            	}
	            	
	            }
	            
	            	           
//	            g2.setTransform(orig);
//	            if(e.fromNode.getNodeName().startsWith("N") || e.fromNode.getNodeName().startsWith("List")){
//	            	// System.err.println("-----------------");
//	            	// System.err.println(distance);
//	            	// System.err.println(e.fromNode.getNodeName());
//		            // System.err.println(from);
//	            }
//	            
	            
//	            if(n.isMainArg){
//	            	this.setText(n.mainArgNo + ". " + this.getText());
//	            }
	            
//	            if(n.getNodeType() == Node.TYPE.Functor){
//	            	this.setText(this.getText() + "()");
//	            }
//	            
//	            if(!n.isMainArg && n.getNodeType() == Node.TYPE.Variable){
//	            	this.setText("<html><br>" + this.getText() + "</html>");
//	            }
	            
//	            this.setText("ariel");
	            
	            
	            
//	            vv.getGraphics().setColor(Color.blue);
//	            vv.getGraphics().fillRect(this.getX(), this.getY(), 10, 10);	           
	            
	            return this;
	        }
	        
	    };
	    
		
//		vv.getRenderContext().setEdgeLabelTransformer(new Transformer<Edge, String>() {
//            public String transform(Edge e) {
////            	if(e.startsWith("head") || e.startsWith("tail")){ //Lists
////            		return e.replaceAll("\\d","");            
////            	}else if(e.startsWith("arg") || e.startsWith("op") || e.startsWith("left") || e.startsWith("right") || e.startsWith("element") || e.startsWith("set") || e.startsWith("is")){ //Functions or operators
////            		if(e.split("_")[1].startsWith("fromFunctor")){
////            			return "";
////            		}else{
////            			return e.split("_")[0];
////            		}
////            	}else if(e.startsWith("clauseHeadListArg")){
////            		return "";
////            	}else{ //Just variables
////            		return e;
////            	}
//            	if(e.toLabel.startsWith("Out_")){
//            		return "Out_" + e.toLabel.split("_")[1];
//            	}else if(!e.fromLabel.equals("")){
//            		return e.fromLabel;
//            	}else{
////            		return e.toLabel.split("_")[0];
//            		return e.toLabel;
//            	}
//            }
//        });
					
		
		
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
	            if(!n.isMainArg && n.getType() == Node.TYPE.Variable){
	            	this.setText("<html><br>" + this.getText() + "</html>");
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
	        	// System.out.println(i.getType());
	        	Shape shape = null;
	        	Node.TYPE nodeType = i.getType();   
	        	
	        	
	        	if(i.isMainArg){
	        		int radius = 30;
	        		shape = new Ellipse2D.Double(-15, -15, radius, radius);
	        	}else if(nodeType == Node.TYPE.ListOperator){
	        		shape = new Rectangle(-15, -15, 22, 22);
	        	}else if(nodeType == Node.TYPE.Functor){
	//        		shape = new Rectangle(-15, -15, 20, 20);
	        		int width = (i.getName().length() < 5) ? 5 : i.getName().length(); // so the node doesn't look too small.        		
	        		width = width > 10 ? 10 : width; //enforce max rectangle width.
	        		
	        		width = (int)(width * 4.5);
	        		width += 3; //for the extra "()" 
	        		width += i.isMainArg ? 3: 0; //for the extra arg number at the start.
	        		
	        		if(FunctorNode.isListPredicate(i.getName())){
	        			shape = new Rectangle(-15, -15, width, 25);
	        		}else{
	        			shape = new Rectangle(-15, -15, width, 20);
	        		}
	        		
	        	}else if(nodeType == Node.TYPE.Operator){
	        		shape = new Rectangle(-15, -15, 20, 20);
	        	}else if(nodeType == Node.TYPE.Variable){
	        		if(i.getFromNodeCount() + i.getToNodeCount() >= 3){ // A variable in a Junction.
	        			shape = new Ellipse2D.Double(-2.5, -2.5, 5, 5);	   	        			
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
	        		shape = new Ellipse2D.Double(-15, -15, 20, 20);
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
			                	int offset = 6;
			                    g.drawImage(icon.getImage(), x - offset, y - offset - 3, getIconWidth(), getIconHeight(), null);
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
				                
				                int offset = 6;
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
	    

		vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Node, Edge>());
//		vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.CubicCurve<Node, Edge>());
//		vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.BentLine<Node, Edge>());		
//		vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
		vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
//
		vv.getRenderContext().setEdgeLabelRenderer(edgeLabelRenderer);
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
	
	
	static class MyRenderer implements Renderer.EdgeLabel<Node, Edge>{

		@Override
		public void labelEdge(RenderContext<Node, Edge> context, Layout<Node, Edge> layout, Edge edge, String label) {
			// TODO Auto-generated method stub
			// System.out.println("label ---> " + label);
			// System.out.println(edge);
			
			
		}
		
	}
	
	private static String getNbsp(int spaces){
		String space = "";
		for(int i = 0; i < spaces; i++){
			space += "&nbsp;";
		}
		return space;
	}

}
