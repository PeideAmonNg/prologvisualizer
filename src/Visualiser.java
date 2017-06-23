

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
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
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoundedRangeModel;
import javax.swing.BoxLayout;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.collections15.Transformer;

import Visual.Node;
import Visual.SimpleGraphView;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ConstantDirectionalEdgeValueTransformer;
import edu.uci.ics.jung.visualization.decorators.DirectionalEdgeArrowTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;

public class Visualiser implements ActionListener  {
	
	public static final int LAYOUT_WIDTH = 1000;
	public static final int LAYOUT_HEIGHT = 600;
	public static final int COLOURS_PANEL_HEIGHT = 70;
	
	
	public Visualiser(){
		
		
		
	    
	    class MyPanel extends JPanel {

	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            String str = "append([H|T],L2,[H|L3])  :-  \nResult is L2 * 1, \nL2 \\= L5, \naaaaa(T,L2,L3), \nappend(T,L5,L3), \nappend(T,L6,L3).";
	            g.setFont(new Font(g.getFont().getName(), Font.BOLD, 10));
	            g.setColor(Color.black);
	            g.drawString(str, 20, 10+25+10+10);
	            g.setColor(Color.blue);
//	            g.drawString("Function", 20 , 10 + (int)(25.0 * 0.75));

//	            g.drawRect(10, 10, 100, 25);	
//	            
//	            g.drawLine(10, 35, 590, 35);
//	            g.drawLine(10, 35, 10, 100);
//	            g.drawLine(590, 35, 590, 100);
	        }

	    }
	    
		

		JFrame frame = new JFrame("Prolog Code Visualiser");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	    JPanel container = new JPanel();
	    container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
	    
	    class ColoursPanel extends JPanel {

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
		            int topOffset = 25;
		            g.drawImage(gray, 10, topOffset, imageWidth, imageHeight, container);
		            g.drawString(grayText, 30, topOffset + (int)(15.0*0.75));
		            g.drawImage(rect, 10, topOffset+15, imageWidth, imageHeight, container);
		            g.drawString("Functions/Lists/Operators", 30, topOffset + imageHeight + (int)(15.0*0.75));
		            g.drawImage(tri, 10, topOffset + imageHeight + imageHeight, imageWidth, imageHeight, container);
		            g.drawString("Variables", 30, topOffset + imageHeight + imageHeight + (int)(15.0*0.75));
//		            g.drawImage(green, 10, 10+15+15+15, 15, 15, container);
//		            g.drawString("Maths Operators", 30, 10+15+15+15+(int)(15.0*0.75));
	            }catch(Exception ex){
	            	ex.printStackTrace();
	            }	            
	        }
	    }
	    
	    		
		JPanel colours = new ColoursPanel();
		colours.setPreferredSize(new Dimension(LAYOUT_WIDTH, COLOURS_PANEL_HEIGHT));
		colours.setBackground(Color.WHITE);
	    
	    JTextField textField = new JTextField(1);
	    JButton button = new JButton("Visualise code");
	    button.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		
	    		try{
		    		ParserProcess pp = new ParserProcess();
		    		List<Node> nodes = pp.traverse(textField.getText());
		    		System.out.println("done traversing");
		    		frame.getContentPane().removeAll();
		    		String code = textField.getText();
		    		JPanel vv = visualise(nodes);
		    		JPanel tempPanel = new JPanel();
		    		container.removeAll();
		    		container.add(textField);
		    		container.add(button);
		    		container.add(colours);
		    		colours.setBackground(Color.WHITE);
		    		container.setBackground(Color.WHITE);
		    		vv.setBackground(Color.WHITE);
		    		container.add(vv);	
		    		frame.getContentPane().add(container); 	
		    		frame.pack();
		    		frame.setVisible(true);
	    		}catch(Exception ex){	    			
//	    			Toast.makeText(frame, ex.getMessage()).display();
	    		}
	    		
//	    		container.add(visualise(pp.traverse("sift([X | Tail], N, Result):- X =< N, sift(Tail, N, Result).")));	
	    	}
	    });
	    
	    
		container.add(textField);
		container.add(button);
		container.add(colours);
		container.setBackground(Color.WHITE);
		JPanel tempPanel = new JPanel();
		tempPanel.setPreferredSize(new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT));
		tempPanel.setBackground(Color.WHITE);
		container.add(tempPanel);
		
//		container.add(visualise(null));	
		frame.setMinimumSize(new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT + COLOURS_PANEL_HEIGHT + 90));
		System.out.println(LAYOUT_HEIGHT + COLOURS_PANEL_HEIGHT + 90);
		
		frame.getContentPane().add(container);
		frame.pack();
		frame.setVisible(true); 
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
	}
		
		
	public JPanel visualise(List<Node> predicateClauses){
		SimpleGraphView sgv = new SimpleGraphView(predicateClauses); //We create our graph in here
		// The Layout<V, E> is parameterized by the vertex and edge types
	//	Layout<Node, String> layout = new CircleLayout<>(sgv.graph);
	//	Layout<Node, String> layout = new FRLayout<>(sgv.graph);
	//	Layout<Node, String> layout = new FRLayout2<>(sgv.graph);
		Layout<Node, String> layout = new ISOMLayout<>(sgv.graph);
		
	//	Layout<Node, String> layout = new SpringLayout<>(sgv.graph);
	//	Layout<Node, String> layout = new KKLayout<>(sgv.graph);
	//	Layout<Node, String> layout = new DAGLayout<>(sgv.graph);
		
		layout.setSize(new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT)); // sets the initial size of the space
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		BasicVisualizationServer<Node,String> vv = new BasicVisualizationServer<Node,String>(layout);
		vv.setPreferredSize(new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT)); //Sets the viewing area size
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
		
//		vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
//		vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
	
		
		vv.getRenderContext().setEdgeLabelTransformer(new Transformer<String, String>() {
            public String transform(String e) {
            	if(e.startsWith("head") || e.startsWith("tail")){ //Lists
            		return e.replaceAll("\\d","");            
            	}else if(e.startsWith("arg") || e.startsWith("op") || e.startsWith("is") || e.startsWith("of")){ //Functions or operators
            		return e.split("_")[0];
            	}else{ //Just variables
            		return e;
            	}
            }
        });
					
		vv.getRenderContext().setEdgeLabelClosenessTransformer(new Transformer<Context<Graph<Node, String>, String>, Number>() {
			@Override
			public Number transform(Context<Graph<Node, String>, String> arg0) {          
                return 0.5; //halfway on an edge.
			}
        });
		
		
		
		vv.getRenderContext().setLabelOffset(15);
		 
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
	            if(n.isMainArg){
	            	this.setText(n.mainArgNo + ". " + this.getText());
	            }
	            
	            if(n.getNodeType() == Node.TYPE.Functor){
	            	this.setText(this.getText() + "()");
	            }
	            
	            if(n.getNodeType() == Node.TYPE.Variable){
	            	this.setText("<html><br>" + this.getText() + "</html>");
	            }
	            	
	            return this;
	        }
	        
	    };
	    
	 // Transformer maps the vertex number to a vertex property
	    Transformer<Node,Paint> vertexColor = new Transformer<Node, Paint>() {
	        public Paint transform(Node node){
	        	return node.getNodeColor();
	        }
	    };
	    
	    Transformer<Node,Shape> vertexSize = new Transformer<Node, Shape>(){
	        public Shape transform(Node i){
	        	System.out.println(i.getClass().getTypeName());
	        	Shape shape = null;
	        	String nodeType = i.getClass().getTypeName();        	
	        	if(nodeType.equals("Visual.ListOperatorNode") || nodeType.equals("Visual.FunctorNode")){
	//        		shape = new Rectangle(-15, -15, 20, 20);
	        		int width = (i.getNodeName().length() < 5) ? 5 : i.getNodeName().length(); // so the node doesn't look too small.        		
	        		width = width > 10 ? 10 : width;
	        		
	        		shape = new Rectangle(-15, -15, (int)(width * 4.5), 20);
	        	}else if(nodeType.equals("Visual.OperatorNode")){
	        		shape = new Rectangle(-15, -15, 20, 20);
	        	}else if(nodeType.equals("Visual.VariableNode")){
					final GeneralPath p0 = new GeneralPath();
					int sizeFactor = 10;
					p0.moveTo(0.0f, -sizeFactor); //start at point 1
					p0.lineTo(sizeFactor*2, sizeFactor); //go to point 2
					p0.lineTo(-sizeFactor*2, sizeFactor); //go to point 3
					p0.closePath();
	        		shape = p0;
	        	}else if (nodeType.equals("Visual.OperatorNode")){
	        		shape = new Ellipse2D.Double(-15, -15, 20, 20);
	        	}else{
	        		shape = new Ellipse2D.Double(-15, -15, 20, 20);
	        	}
	        	
	            // in this case, the vertex is twice as large
	            return AffineTransform.getScaleInstance(2, 2).createTransformedShape(shape);
	            
	        }
	    };
	    vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);
	    vv.getRenderContext().setVertexShapeTransformer(vertexSize);    
	    vv.getRenderContext().setVertexLabelRenderer(vertexLabelRenderer);
	    vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
	    
	    vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
	   
	    
	    
	    vv.addPreRenderPaintable(new VisualizationViewer.Paintable(){
	        public void paint(Graphics g) {
	//        	BufferedImage img = null;
	//            try {
	//                img = ImageIO.read(new File("circle.png"));
	//            } catch (IOException e) {
	//            }
	//            Dimension d = vv.getSize();
	//            if(img!=null){
	//            	g.drawImage(img,0,0,d.width,d.height,vv);
	//            }
	            
	//            g.drawRect(30, 30, 10, 10);
	//            vv.image
	            g.drawString("hellohellohellohellohellohellohellohello", 30, 20);
	            vv.fireStateChanged();
	            int x = 20;
	            int y = 20;
	            Font font = null;
	            FontMetrics metrics;
	            int swidth;
	            int sheight;
	            String str = "GraphZoomScrollPane Demo";
	            
	            
	                Dimension d = vv.getSize();
	                if(font == null) {
	                    font = new Font(g.getFont().getName(), Font.BOLD, 30);
	                    metrics = g.getFontMetrics(font);
	                    swidth = metrics.stringWidth(str);
	                    sheight = metrics.getMaxAscent()+metrics.getMaxDescent();
	                    x = (d.width-swidth)/2;
	                    y = (int)(d.height-sheight*1.5);
	                }
	                g.setFont(font);
	                Color oldColor = g.getColor();
	                g.setColor(Color.BLUE);
	//                g.drawString(str, x, y);
	//                g.setColor(oldColor);
	                font = new Font(g.getFont().getName(), Font.BOLD, 15);
	                g.setFont(font);
	                
	//                g.drawRect(10, 0, 580, 600-20-25);
	            
	        }
	        public boolean useTransform() { return false; }
	    });
	
		return vv;
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		 	
		
	}

}
