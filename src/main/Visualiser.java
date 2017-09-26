package main;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.jdesktop.swingx.prompt.PromptSupport;

import Visual.Node;
import Visual.SimpleGraphView;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractModalGraphMouse;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;

public class Visualiser implements ActionListener  {
	
	public static final int LAYOUT_WIDTH = 1200; //1000
	public static final int LAYOUT_HEIGHT = 700; //600
	public static final int INFO_PANEL_HEIGHT = 70;
	public static final int SCREEN_MARGIN = 10;
	
	public Visualiser(){
		
		EventQueue.invokeLater(new Runnable() {
			@Override
            public void run(){
				JFrame frame = new JFrame("Prolog Visualiser");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
			    JPanel container = new JPanel();
				container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));		
				container.setBackground(Color.WHITE);
				
			    try{
			    	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			    	UIManager.getDefaults().put("TextField.font", UIManager.getFont("TextArea.font"));
			    }catch(Exception e){
			    	
			    }
				
				JTextField metaClauseField = new JTextField(11);
				JCheckBox directedEdgeEnabled = new JCheckBox("Enable directed edges");
				JTextArea clauseFieldArea = new JTextArea(5, 20);				
				JScrollPane clauseField = new JScrollPane(clauseFieldArea); 
				
				JButton button = new JButton("Visualise");
			    
		        PromptSupport.setPrompt("clause description here", metaClauseField);
		        PromptSupport.setPrompt("clause here", clauseFieldArea);      
		        
		        PromptSupport.setFontStyle(Font.ITALIC, metaClauseField);
		        PromptSupport.setFontStyle(Font.ITALIC, clauseFieldArea);
		        
				JPanel fieldPanel = new JPanel();
				fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.PAGE_AXIS));
				JPanel topPanel = new JPanel();
				topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
				topPanel.add(metaClauseField);
				topPanel.add(directedEdgeEnabled);
				
				fieldPanel.add(topPanel);
//				fieldPanel.add(metaClauseField);
				fieldPanel.add(clauseField);
		
				
			    JPanel formPanel = new JPanel(); 
			    formPanel.setLayout(new BorderLayout());
			    formPanel.setSize(new Dimension(LAYOUT_WIDTH, formPanel.getHeight()));
			    formPanel.setBackground(Color.WHITE);	    
			    formPanel.add(fieldPanel);
			    formPanel.add(button, BorderLayout.EAST);
			    formPanel.setBorder(BorderFactory.createEmptyBorder(SCREEN_MARGIN, SCREEN_MARGIN, 0, SCREEN_MARGIN));
			    
			    JPanel tempDrawingPanel = new JPanel();
				tempDrawingPanel.setPreferredSize(new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT));
				tempDrawingPanel.setBackground(Color.WHITE);
				
				
			    container.add(formPanel);	    
				container.add(tempDrawingPanel);
			
				frame.getContentPane().add(container);
				
				JButton plus = new JButton("+");
				JButton minus = new JButton("-");
				JButton reset = new JButton("reset");

				JPanel controls = new JPanel();
				controls.add(plus);
				controls.add(minus);
				controls.add(reset);
				frame.add(controls, BorderLayout.SOUTH);
				
				frame.pack();
				frame.setVisible(true); 
				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
				
				frame.requestFocusInWindow();
		
				
				ActionListener actionListener = new ActionListener(){
			    	public void actionPerformed(ActionEvent e){
			    		
			    		try{
			    			
			    			String metaClause = metaClauseField.getText().trim();
			    			List<MetaPredicate> preds = new ArrayList<>();
			    			String[] clauses;
			    			
			    			if(!metaClause.equals("")){
			    				System.err.println(metaClause);
			    				clauses = metaClause.split("\\)\\s*,");
			    				
			    				for(int i = 0; i < clauses.length; i++) {
			    					String tempClause = clauses[i].trim();
			    					if(!tempClause.endsWith(")")) {
			    						tempClause = tempClause + ")";
			    					}
			    					clauses[i] = tempClause;

			    				}
			    				
			    				
			    				if(clauses != null) {
			    					for(String mc : clauses) {
			    						int openBracIndex = mc.indexOf("("), closeBracIndex = mc.indexOf(")");
						    			String[] args = mc.substring(openBracIndex + 1, closeBracIndex).split(",");
						    			int arity = args.length;
						    			
						    			MetaPredicate pred = new MetaPredicate(mc.substring(0, openBracIndex), arity);
						    			preds.add(pred);
						    			
						    			for(int i = 0; i < args.length; i++){
						    				pred.addRoleName(args[i].trim().toLowerCase());
						    			}
						    			
						    			System.err.println(pred.getRoleNames());
			    					}
			    				}
			    				
			    				
				    			
			    			}
			    			
			    			
			    			
				    		ParserProcess pp = new ParserProcess(preds);
				    		List<Node> nodes = pp.traverse(clauseFieldArea.getText().trim());
				    		
				    		Stack<Node> mainBranch = findMainBranch(nodes, directedEdgeEnabled.isSelected());
				    		
				    		
				    		
				    		frame.getContentPane().removeAll();
				    		VisualizationViewer vv = (VisualizationViewer) GraphExtension.visualise(nodes, mainBranch, directedEdgeEnabled.isSelected());
		//		    		JPanel tempPanel = new JPanel();
				    		final GraphZoomScrollPane tempPanel = new GraphZoomScrollPane(vv);
				    		tempPanel.setPreferredSize(new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT));
				    		tempPanel.add(vv);
				    		
				    		final AbstractModalGraphMouse graphMouse = new DefaultModalGraphMouse<String,Number>();
				    		graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
				    		vv.setGraphMouse(graphMouse);
		
							vv.addKeyListener(graphMouse.getModeKeyListener());
							vv.setToolTipText("<html><center>Type 'p' for Pick mode<p>Type 't' for Transform mode");
							
							final ScalingControl scaler = new CrossoverScalingControl();
		
							JButton plus = new JButton("+");
							plus.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									scaler.scale(vv, 1.2f, vv.getCenter());
								}
							});
							JButton minus = new JButton("-");
							minus.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									scaler.scale(vv, 1 / 1.2f, vv.getCenter());
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
				    		container.add(formPanel);
				    		vv.setBackground(Color.WHITE);
				    		tempPanel.setBackground(Color.white);
				    		container.add(tempPanel);	
				    		frame.getContentPane().add(container); 	
				    		frame.pack();
				    		frame.setVisible(true);
			    		}catch(Exception ex){	    			
			    			ex.printStackTrace();
			    		}
			    		
			    	}
			    };
			    
		    	button.addActionListener(actionListener);	
			    
			}
		});			    		
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
	            Image gray = ImageIO.read(new File("/resources/gray.png"));
	            Image rect = ImageIO.read(new File("/resources/rectangle.png"));
	            Image tri = ImageIO.read(new File("/resources/triangle.png"));
	            Image green = ImageIO.read(new File("/resources/green.png"));
	            
	            
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

	@Override
	public void actionPerformed(ActionEvent arg0) {
		 	
		
	}
	
	private Stack<Node> path  = new Stack<Node>();   // the current path
    private Set<Node> onPath  = new HashSet<Node>();     // the set of vertices on the path
    private Set<Stack<Node>> paths = new HashSet<>();

    // use DFS
    private void enumerate(Graph G, Node v, Node t) {

        // add node v to current path from s
        path.push(v);
        onPath.add(v);

        // found path from s to t - currently prints in reverse order because of stack
        if (v.equals(t)) {
        	Stack<Node> clone = new Stack<>();
            for (Node item : path){
            	clone.push(item);
            }
            
        	paths.add(clone);
        // consider all neighbors that would continue path with repeating a node
        }else {
        	
            for (Node w : (Collection<Node>)G.getNeighbors(v)){
                if (!onPath.contains(w)) enumerate(G, w, t);
            }
        }

        // done exploring from v, so remove from path
        path.pop();
        onPath.remove(v);
    }
		
	public Stack<Node> findMainBranch(List<Node> nodes, boolean directedEdgeEnabled){
		
		SimpleGraphView gv = new SimpleGraphView(nodes, directedEdgeEnabled);
		Set<Node> inputNodes = new HashSet<>();
		Node outputNode = null;
		
		for(Node n : gv.graph.getVertices()){
			if(n.isMainArg){
				if(n.getToNodeCount() == 0){
					outputNode = n;
				}else{
					inputNodes.add(n);
				}
			}						
		}
					
		for(Node inputNode : inputNodes){
			enumerate(gv.graph, inputNode, outputNode);
		};
		
		Stack<Node> mainBranch = findMainBranch();

		this.paths.clear();
		
		// Meaning there is no output node for this Prolog clause. In this case, we make one up. Find paths between two input nodes with most list processors.
		if(mainBranch == null){ 

			gv = new SimpleGraphView(nodes, directedEdgeEnabled);
			inputNodes = new HashSet<>();
			Set<Node >outputNodes = new HashSet<>();
			
			for(Node node : gv.graph.getVertices()){
				if(node.isMainArg){					
					inputNodes.add(node);
					outputNodes.add(node);				
				}						
			}
						
			for(Node inputNode : inputNodes){
				for(Node outNode : outputNodes){
					if(inputNode != outputNode){
						enumerate(gv.graph, inputNode, outNode);
					}
				}				
			}
			
			mainBranch = findMainBranch();
		}
		
		this.paths.clear();		
		
		return mainBranch;
		
	}
	
	private Stack<Node> findMainBranch(){

		int maxListVarCount = -1, minNodeCount = Integer.MAX_VALUE, minMainArg = Integer.MAX_VALUE;
		Stack<Node> mainBranch = null;
		for(Stack<Node> path : paths){
			
			int listVarCount = 0, nodeCount = 0, mainArg = Integer.MAX_VALUE;
			
			Iterator<Node> iter = path.iterator();
			while (iter.hasNext()){
				Node n = iter.next();
			    if(n.getType() == Node.TYPE.ListOperator || (n.isMainArg && n.getName().contains("List"))){
			    	listVarCount++;
			    }
			    
			    if(n.isMainArg && n.mainArgNo < mainArg){
			    	mainArg = n.mainArgNo;
			    }
			    
			    nodeCount++;
			}
			
			
			if(path != null && nodeCount != 0){
				if(listVarCount > maxListVarCount || (listVarCount == maxListVarCount && nodeCount < minNodeCount) || (listVarCount == maxListVarCount && mainArg < minMainArg)){
					mainBranch = path;
					maxListVarCount = listVarCount;				
					minNodeCount = nodeCount;					
					minMainArg = mainArg;
				}
			}
			
		}
		
		return mainBranch;
	}

}
