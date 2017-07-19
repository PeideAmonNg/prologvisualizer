import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.bridge.ViewBox;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

/**
 * Immutable class to get the Image representation of a svg resource.
 */
public class SvgImage
{
    /** Root node of svg document */
    private final GraphicsNode rootSvgNode;

    /** Loaded SVG document */
    private final SVGDocument svgDocument;

    /**
     * Load the svg resource from a URL into a document.
     * @param url location of svg resource.
     * @throws java.io.IOException when svg resource cannot be read.
     */
    public SvgImage(URL url)
        throws IOException
    {
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory factory = new
SAXSVGDocumentFactory(parser);
        svgDocument =
(SVGDocument)factory.createDocument(url.toString());
        rootSvgNode = getRootNode(svgDocument);
    }

    /**
     * Load the svg from a document.
     *
     * @param document svg resource
     */
    public SvgImage(SVGDocument document)
    {
        svgDocument = document;
        rootSvgNode = getRootNode(svgDocument);
    }

    /**
     * Get svg root from the given document.
     *
     * @param document svg resource
     */
    private static GraphicsNode getRootNode(SVGDocument document)
    {
        // Build the tree and get the document dimensions
        UserAgentAdapter userAgentAdapter = new UserAgentAdapter();
        BridgeContext bridgeContext = new
BridgeContext(userAgentAdapter);
        GVTBuilder builder = new GVTBuilder();

        return builder.build(bridgeContext, document);
    }

    /**
     * Get the svg root node of the document.
     *
     * @return svg root node.
     */
    public GraphicsNode getRootSvgNode()
    {
        return rootSvgNode;
    }

    /**
     * Get the svg document.
     * @return the svg document.
     */
    public SVGDocument getSvgDocument()
    {
        return svgDocument;
    }

    /**
     * Renders and returns the svg based image.
     *
     * @param width desired width
     * @param height desired height
     * @return image of the rendered svg.
     */
    public Image getImage(int width, int height)
    {
        // Paint svg into image buffer
        BufferedImage bufferedImage = new BufferedImage(width,
            height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();

        // For a smooth graphic with no jagged edges or rastorized look.
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Scale image to desired size
        Element elt = svgDocument.getRootElement();
        AffineTransform usr2dev = ViewBox.getViewTransform(null, elt, width, height, null);
        g2d.transform(usr2dev);

        rootSvgNode.paint(g2d);

        // Cleanup and return image
        g2d.dispose();
        return bufferedImage;
    }
}