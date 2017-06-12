package Visual;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;

public class Visualiser {

	
@SuppressWarnings("serial")
public void visualise(List<List<Node>> predicateClauses){
	SimpleGraphView sgv = new SimpleGraphView(predicateClauses.get(0)); //We create our graph in here
	// The Layout<V, E> is parameterized by the vertex and edge types
	//		 Layout<Integer, String> layout = new CircleLayout<>(sgv.graph);
//	Layout<Integer, String> layout = new FRLayout<>(sgv.graph);
//	Layout<Integer, String> layout = new FRLayout2<>(sgv.graph);
	Layout<Node, String> layout = new ISOMLayout<>(sgv.graph);
//	Layout<Integer, String> layout = new SpringLayout<>(sgv.graph);
//	Layout<Node, String> layout = new KKLayout<>(sgv.graph);
//	Layout<Integer, String> layout = new DAGLayout<>(sgv.graph);
	
	layout.setSize(new Dimension(300,300)); // sets the initial size of the space
	// The BasicVisualizationServer<V,E> is parameterized by the edge types
	BasicVisualizationServer<Node,String> vv = new BasicVisualizationServer<Node,String>(layout);
	vv.setPreferredSize(new Dimension(500,500)); //Sets the viewing area size
//	vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
	//		 vv.getRenderContext().setEdgeShapeTransformer(
	//				    new EdgeShape.Line<Integer,String>());
	//		 vv.getRenderContext().setEdgeShapeTransformer(
	//				    new EdgeShape.Orthogonal<Integer,String>());
	vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Orthogonal<>());
	
	
    DefaultVertexLabelRenderer vertexLabelRenderer = new DefaultVertexLabelRenderer(Color.black){
        @Override
        public <V> Component getVertexLabelRendererComponent(
            JComponent vv, Object value, Font font, 
            boolean isSelected, V vertex) 
        {
            super.getVertexLabelRendererComponent(
                vv, value, font, isSelected, vertex);
            setForeground(Color.black);
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
            Ellipse2D circle = new Ellipse2D.Double(-15, -15, 30, 30);
            // in this case, the vertex is twice as large
            return AffineTransform.getScaleInstance(2, 2).createTransformedShape(circle);
            
        }
    };
    vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);
    vv.getRenderContext().setVertexShapeTransformer(vertexSize);    
    vv.getRenderContext().setVertexLabelRenderer(vertexLabelRenderer);
    vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
    
    vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
    
    
	JFrame frame = new JFrame("Simple Graph View");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.getContentPane().add(vv);
	frame.pack();
	frame.setVisible(true); 
}

}
