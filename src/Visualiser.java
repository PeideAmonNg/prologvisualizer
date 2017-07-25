import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.io.File;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.collections15.Transformer;

import Visual.FunctorNode;
import Visual.Node;
import Visual.SimpleGraphView;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractModalGraphMouse;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.DirectionalEdgeArrowTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;

public class Visualiser implements ActionListener  {
	
	public static final int LAYOUT_WIDTH = 1200; //1000
	public static final int LAYOUT_HEIGHT = 700; //600
	public static final int INFO_PANEL_HEIGHT = 70;
	public static final int SCREEN_MARGIN = 10;
	
	public Visualiser(){
			    
		JFrame frame = new JFrame("Prolog Visualiser");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	    JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));		
		container.setBackground(Color.WHITE);
	    	   	
//		JPanel infoPanel = new InfoPanel(container);
//		infoPanel.setPreferredSize(new Dimension(LAYOUT_WIDTH, INFO_PANEL_HEIGHT));
//		infoPanel.setBackground(Color.WHITE);
	    
		JTextField textField = new JTextField(1); 
		
		JButton button = new JButton("Visualise");
	    
	    JPanel inputPanel = new JPanel(); 
	    inputPanel.setLayout(new BorderLayout());
	    inputPanel.setSize(new Dimension(LAYOUT_WIDTH, inputPanel.getHeight()));
	    inputPanel.setBackground(Color.WHITE);	    
	    inputPanel.add(textField);
	    inputPanel.add(button, BorderLayout.EAST);
	    inputPanel.setBorder(BorderFactory.createEmptyBorder(SCREEN_MARGIN, SCREEN_MARGIN, 0, SCREEN_MARGIN));
	    
	    JPanel tempDrawingPanel = new JPanel();
		tempDrawingPanel.setPreferredSize(new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT));
//		tempDrawingPanel.setPreferredSize(new Dimension(500, 500));
		tempDrawingPanel.setBackground(Color.WHITE);
		
		
	    container.add(inputPanel);	    
//		container.add(infoPanel);
		container.add(tempDrawingPanel);
	
		frame.getContentPane().add(container);
		frame.pack();
		frame.setVisible(true); 
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
//		frame.setMinimumSize(new Dimension(LAYOUT_WIDTH + 20, LAYOUT_HEIGHT + INFO_PANEL_HEIGHT + 50));
//		frame.setSize(new Dimension(LAYOUT_WIDTH + 20, LAYOUT_HEIGHT + INFO_PANEL_HEIGHT + 70));	
//		frame.setResizable(false);

		System.out.println(LAYOUT_HEIGHT + INFO_PANEL_HEIGHT + 90);
		
		ActionListener actionListener = new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		
	    		try{
		    		ParserProcess pp = new ParserProcess();
		    		List<Node> nodes = pp.traverse(textField.getText());
		    		System.out.println("done traversing");
		    		
		    		frame.getContentPane().removeAll();
		    		VisualizationViewer vv = (VisualizationViewer) visualise(nodes);
//		    		JPanel tempPanel = new JPanel();
		    		final GraphZoomScrollPane tempPanel = new GraphZoomScrollPane(vv);
		    		tempPanel.setPreferredSize(new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT));
		    		tempPanel.add(vv);
		    		
		    		final AbstractModalGraphMouse graphMouse = new DefaultModalGraphMouse<String,Number>();
					vv.setGraphMouse(graphMouse);

					vv.addKeyListener(graphMouse.getModeKeyListener());
					vv.setToolTipText("<html><center>Type 'p' for Pick mode<p>Type 't' for Transform mode");
					
					final ScalingControl scaler = new CrossoverScalingControl();

					JButton plus = new JButton("+");
					plus.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							scaler.scale(vv, 1.1f, vv.getCenter());
						}
					});
					JButton minus = new JButton("-");
					minus.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							scaler.scale(vv, 1 / 1.1f, vv.getCenter());
						}
					});

					JButton reset = new JButton("reset");
					reset.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e) {
							vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT)
									.setToIdentity();
							vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW).setToIdentity();
						}
					});

					JPanel controls = new JPanel();
					controls.add(plus);
					controls.add(minus);
					controls.add(reset);
					frame.add(controls, BorderLayout.SOUTH);
		    		
		    		container.removeAll();
		    		container.add(inputPanel);
//		    		container.add(infoPanel);
		    		vv.setBackground(Color.WHITE);
		    		tempPanel.setBackground(Color.white);
		    		container.add(tempPanel);	
		    		frame.getContentPane().add(container); 	
		    		frame.pack();
		    		frame.setVisible(true);
//		    		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//		    		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
//		    		frame.setMinimumSize(new Dimension(LAYOUT_WIDTH + 20, LAYOUT_HEIGHT + INFO_PANEL_HEIGHT + 50));
//		    		frame.setSize(new Dimension(LAYOUT_WIDTH + 20, LAYOUT_HEIGHT + INFO_PANEL_HEIGHT + 50));	
//		    		frame.setResizable(false);
		    		textField.requestFocus();
	    		}catch(Exception ex){	    			
	    			ex.printStackTrace();
	    		}
	    		
//	    		container.add(visualise(pp.traverse("sift([X | Tail], N, Result):- X =< N, sift(Tail, N, Result).")));	
	    	}
	    };
	    
    	button.addActionListener(actionListener);	
	    textField.addActionListener(actionListener);
         
         
	}
	
	class InfoPanel extends JPanel {
		private JPanel container;
		
		public InfoPanel(JPanel cont){
			container = cont;
		}
		
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            try{
            	System.out.println("hello");
	            Image gray = ImageIO.read(new File("./colours/gray.png"));
	            Image rect = ImageIO.read(new File("./colours/rectangle.png"));
	            Image tri = ImageIO.read(new File("./colours/triangle.png"));
	            Image green = ImageIO.read(new File("./colours/green.png"));
	            
	            System.out.println("after pink");
	            
	            String grayText = "Current Function's Arguments";
	            int imageWidth = 15, imageHeight = 15;
	            g.drawImage(gray, 10, SCREEN_MARGIN, imageWidth, imageHeight, container);
	            g.drawString(grayText, 30, SCREEN_MARGIN + (int)(15.0*0.75));
	            g.drawImage(rect, 10, SCREEN_MARGIN + imageHeight, imageWidth, imageHeight, container);
	            g.drawString("Functions/Lists/Operators", 30, SCREEN_MARGIN + imageHeight + (int)(15.0*0.75));
	            g.drawImage(tri, 10, SCREEN_MARGIN + (imageHeight*2), imageWidth, imageHeight, container);
	            g.drawString("Variables", 30, SCREEN_MARGIN + (imageHeight*2) + (int)(15.0*0.75));
            }catch(Exception ex){
            	ex.printStackTrace();
            }	            
        }
    }
		
		
	public JPanel visualise(List<Node> predicateClauses){
		SimpleGraphView sgv = new SimpleGraphView(predicateClauses); //We create our graph in here
		// The Layout<V, E> is parameterized by the vertex and edge types
//		Layout<Node, String> layout = new CircleLayout<>(sgv.graph);
//		FRLayout<Node, String> layout = new FRLayout<>(sgv.graph);
//		FRLayout2<Node, String> layout = new FRLayout2<>(sgv.graph);
		Layout<Node, String> layout = new ISOMLayout<>(sgv.graph);
		layout.initialize();
//		
//		SpringLayout<Node, String> layout = new SpringLayout<>(sgv.graph);
//		Layout<Node, String> layout = new KKLayout<>(sgv.graph);
//		DAGLayout<Node, String> layout = new DAGLayout<>(sgv.graph);
		
		//-----FRLayout configuration------//
//		layout.setRepulsionMultiplier(0.7);
//		layout.setMaxIterations(10000000);
//		layout.setRepulsionMultiplier(1);
		//-----FRLayout configuration------//

		
		
//		layout.setSize(new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT)); // sets the initial size of the space
		layout.setSize(new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT));
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
//		BasicVisualizationServer<Node,String> vv = new BasicVisualizationServer<Node,String>(layout);
		VisualizationViewer<Node,String> vv = new VisualizationViewer<Node,String>(layout, new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT));
//		vv.setPreferredSize(new Dimension(LAYOUT_WIDTH - 500, LAYOUT_HEIGHT)); //Sets the viewing area size
		vv.setPreferredSize(new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT));
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
		
		vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Node, String>());
//		vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Orthogonal<Node, String>());
//		vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
//		vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
	
//		vv.getRenderContext().setLabelOffset(-5);
		
		vv.getRenderContext().setEdgeLabelTransformer(new Transformer<String, String>() {
            public String transform(String e) {
//            	if(e.startsWith("head") || e.startsWith("tail")){ //Lists
//            		return e.replaceAll("\\d","");            
//            	}else if(e.startsWith("arg") || e.startsWith("op") || e.startsWith("left") || e.startsWith("right") || e.startsWith("element") || e.startsWith("set") || e.startsWith("is")){ //Functions or operators
//            		if(e.split("_")[1].startsWith("fromFunctor")){
//            			return "";
//            		}else{
//            			return e.split("_")[0];
//            		}
//            	}else if(e.startsWith("clauseHeadListArg")){
//            		return "";
//            	}else{ //Just variables
//            		return e;
//            	}
            	return e.split("_")[0];
            }
        });
					
		vv.getRenderContext().setEdgeLabelClosenessTransformer(new Transformer<Context<Graph<Node, String>, String>, Number>() {
			@Override
			public Number transform(Context<Graph<Node, String>, String> arg0) {          
                return 0.5; //halfway on an edge.
			}
        });
		
		
		
		 
		vv.getRenderContext().setEdgeArrowTransformer(new DirectionalEdgeArrowTransformer<Node, String>(5, 5, 0));
		
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
	            if(!n.isMainArg && n.getNodeType() == Node.TYPE.Variable){
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
	        	System.out.println(i.getClass().getTypeName());
	        	Shape shape = null;
	        	String nodeType = i.getClass().getTypeName();   
	        	
	        	
	        	if(i.isMainArg){
	        		int radius = 30;
	        		shape = new Ellipse2D.Double(-15, -15, radius, radius);
	        	}else if(nodeType.equals("Visual.ListOperatorNode")){
	        		shape = new Rectangle(-15, -15, 22, 22);
	        	}else if(nodeType.equals("Visual.FunctorNode")){
	//        		shape = new Rectangle(-15, -15, 20, 20);
	        		int width = (i.getNodeName().length() < 5) ? 5 : i.getNodeName().length(); // so the node doesn't look too small.        		
	        		width = width > 10 ? 10 : width; //enforce max rectangle width.
	        		
	        		width = (int)(width * 4.5);
	        		width += nodeType.equals("Visual.FunctorNode") ? 3 : 0; //for the extra "()" 
	        		width += i.isMainArg ? 3: 0; //for the extra arg number at the start.
	        		
	        		if(FunctorNode.isListPredicate(i.getNodeName())){
	        			shape = new Rectangle(-15, -15, width, 25);
	        		}else{
	        			shape = new Rectangle(-15, -15, width, 20);
	        		}
	        		
	        	}else if(nodeType.equals("Visual.OperatorNode")){
	        		shape = new Rectangle(-15, -15, 20, 20);
	        	}else if(nodeType.equals("Visual.VariableNode")){
	        		if(i.nodesFrom.size() + i.nodesTo.size() >= 3){ // This VariableNode now becomes a Junction.
	        			shape = new Ellipse2D.Double(-2.5, -2.5, 5, 5);	   	        			
	        		}else{
						final GeneralPath p0 = new GeneralPath();
						int sizeFactor = 10;
						p0.moveTo(0.0f, -sizeFactor); //start at point 1
						p0.lineTo(sizeFactor*2, sizeFactor); //go to point 2
						p0.lineTo(-sizeFactor*2, sizeFactor); //go to point 3
						p0.closePath();
		        		shape = p0;
	        		}
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
				// TODO Auto-generated method stub
				try{
					if(node.isMainArg){
	//					return new ImageIcon("./colours/argumentArrows.svg");
	//					JSVGCanvas svg = new JSVGCanvas();
	//			        svg.setURI("file:./colours/argumentArrows.svg");
						
				        SvgImage svgimage = new SvgImage(new URL("file:./colours/5.svg"));
	//					return new ImageIcon("./colours/green.png");
				        return new ImageIcon(svgimage.getImage(45, 45));
						
					}else if(node.getNodeType() == Node.TYPE.ListOperator){
//						SvgImage svgimage = new SvgImage(new URL("file:./colours/listProcessor.svg"));
//						return new ImageIcon(svgimage.getImage(45, 45));
						ImageIcon icon = new ImageIcon("./colours/listProcessor.png");
						System.err.println(icon.getIconWidth() + " " + icon.getIconHeight());
//						return icon;
						return new Icon() {
			                public int getIconHeight() {
			                    return 45;
			                }

			                public int getIconWidth() {
			                    return 45;
			                }
			                
			                int offset = 6;
			                public void paintIcon(Component c, Graphics g, int x, int y) {
			                    g.drawImage(icon.getImage(), x - offset, y - offset - 3, getIconWidth(), getIconHeight(), null);
			                }
			            };
					}else if(node.getNodeType() == Node.TYPE.Functor){
						String functor = node.getNodeName();
						
						if(FunctorNode.isListPredicate(functor)){
//							ImageIcon icon = new ImageIcon("./colours/not_member.png");
							ImageIcon icon = new ImageIcon(FunctorNode.getIconPath(functor));
							System.err.println(icon.getIconWidth() + " " + icon.getIconHeight());
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
				Node.TYPE type = node.getNodeType();
				if(type == Node.TYPE.ListOperator || (FunctorNode.isListPredicate(node.getNodeName()))){
					return null;
				}else{
					return node.getNodeName();
				}
			}
	    	
	    };
	    
	    vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);
	    vv.getRenderContext().setVertexShapeTransformer(vertexShape);    
	    vv.getRenderContext().setVertexLabelRenderer(vertexLabelRenderer);
//	    vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
	    vv.getRenderContext().setVertexLabelTransformer(vertexLabelTransformer);
	    vv.getRenderContext().setVertexIconTransformer(vertexIcon);
	    

        vv.getRenderer().setVertexRenderer(new MultiVertexRenderer<>());
        
	    vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
	   
	    
	    
//	    vv.getRenderContext().
	    
//	    vv.addPreRenderPaintable(new VisualizationViewer.Paintable(){
//	        public void paint(Graphics g) {
//	//        	BufferedImage img = null;
//	//            try {
//	//                img = ImageIO.read(new File("circle.png"));
//	//            } catch (IOException e) {
//	//            }
//	//            Dimension d = vv.getSize();
//	//            if(img!=null){
//	//            	g.drawImage(img,0,0,d.width,d.height,vv);
//	//            }
//	            
//	//            g.drawRect(30, 30, 10, 10);
//	//            vv.image
//	            g.drawString("hellohellohellohellohellohellohellohello", 30, 20);
//	            vv.fireStateChanged();
//	            int x = 20;
//	            int y = 20;
//	            Font font = null;
//	            FontMetrics metrics;
//	            int swidth;
//	            int sheight;
//	            String str = "GraphZoomScrollPane Demo";
//	            
//	            
//	                Dimension d = vv.getSize();
//	                if(font == null) {
//	                    font = new Font(g.getFont().getName(), Font.BOLD, 30);
//	                    metrics = g.getFontMetrics(font);
//	                    swidth = metrics.stringWidth(str);
//	                    sheight = metrics.getMaxAscent()+metrics.getMaxDescent();
//	                    x = (d.width-swidth)/2;
//	                    y = (int)(d.height-sheight*1.5);
//	                }
//	                g.setFont(font);
//	                Color oldColor = g.getColor();
//	                g.setColor(Color.BLUE);
//	//                g.drawString(str, x, y);
//	//                g.setColor(oldColor);
//	                font = new Font(g.getFont().getName(), Font.BOLD, 15);
//	                g.setFont(font);
//	                
//	//                g.drawRect(10, 0, 580, 600-20-25);
//	            
//	        }
//	        public boolean useTransform() { return false; }
//	    });
//	
//	    
	    
//	    vv.addGraphMouseListener(new GraphMouseListener<Node>() {
//			
//			@Override
//			public void graphReleased(Node arg0, MouseEvent arg1) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void graphPressed(Node arg0, MouseEvent arg1) {
//				// TODO Auto-generated method stub
//				System.out.println("pressed");System.out.println(arg0);
//				
//			}
//			
//			@Override
//			public void graphClicked(Node arg0, MouseEvent arg1) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
	    
		return vv;
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		 	
		
	}

}
