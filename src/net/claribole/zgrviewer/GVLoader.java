/*   FILE: GVLoader.java
 *   DATE OF CREATION:   Mon Nov 27 08:30:31 2006
 *   Copyright (c) INRIA, 2006-2011. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
 *
 *   $Id: GVLoader.java 4942 2013-02-21 17:26:22Z epietrig $
 */

package net.claribole.zgrviewer;

import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.jdesktop.swingx.prompt.PromptSupport;
import org.w3c.dom.Document;

import Visual.Node;
import fr.inria.zvtm.engine.SwingWorker;
import fr.inria.zvtm.glyphs.VText;
import fr.inria.zvtm.svg.SVGReader;
import main.MetaPredicate;
import main.ParserProcess;

/* Multiscale feature manager */

class GVLoader {

    Object application; // instance of ZGRViewer or ZGRApplet

    GraphicsManager grMngr;
    ConfigManager cfgMngr;
    DOTManager dotMngr;

    GVLoader(Object app, GraphicsManager gm, ConfigManager cm, DOTManager dm){
    this.application = app;
    this.grMngr = gm;
    this.cfgMngr = cm;
    this.dotMngr = dm;
    }

    void open(short prg, boolean parser){// prg is the program to use DOTManager.*_PROGRAM, use the integrated parser or not
    if (ConfigManager.checkProgram(prg)){
        openDOTFile(prg, parser);
    }
    else {
        Object[] options = {"Yes", "No"};
        int option = JOptionPane.showOptionDialog(null, ConfigManager.getDirStatus(),
                              "Warning", JOptionPane.DEFAULT_OPTION,
                              JOptionPane.WARNING_MESSAGE, null,
                              options, options[0]);
        if (option == JOptionPane.OK_OPTION){
        openDOTFile(prg, parser);
        }
    }
    }

    void openDOTFile(final short prg, final boolean parser){
    final JFileChooser fc = new JFileChooser(ConfigManager.m_LastDir!=null ? ConfigManager.m_LastDir : ConfigManager.m_PrjDir);
    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fc.setDialogTitle("Find DOT File");
    int returnVal= fc.showOpenDialog(grMngr.mainView.getFrame());
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        final SwingWorker worker=new SwingWorker(){
            public Object construct(){
            grMngr.reset();
            loadFile(fc.getSelectedFile(), prg, parser);
            return null;
            }
        };
        worker.start();
    }
    }
    
    void open(File f, short prg, boolean parser){// prg is the program to use DOTManager.*_PROGRAM, use the integrated parser or not
    	openDOTFile(f, prg, parser);
    }
        
    
    // Already have the file of interest.
    void openDOTFile(File f, final short prg, final boolean parser){
        final SwingWorker worker=new SwingWorker(){
            public Object construct(){
            grMngr.reset();
            loadFile(f, prg, parser);
            return null;
            }
        };
        worker.start();
    }

    void openSVGFile(){
    final JFileChooser fc = new JFileChooser(ConfigManager.m_LastDir!=null ? ConfigManager.m_LastDir : ConfigManager.m_PrjDir);
    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fc.setDialogTitle("Find SVG File");
    int returnVal= fc.showOpenDialog(grMngr.mainView.getFrame());
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        final SwingWorker worker=new SwingWorker(){
            public Object construct(){
            grMngr.reset();
            loadSVG(fc.getSelectedFile());
            return null;
            }
        };
        worker.start();
    }
    }

    void openOther(){
    new CallBox((ZGRViewer)application, grMngr);
    }

    void loadFile(File f, short prg, boolean parser){
        // f is the DOT file to load, prg is the program to use DOTManager.*_PROGRAM
        if (f.exists()){
            ConfigManager.m_LastDir=f.getParentFile();
            cfgMngr.lastFileOpened = f;
            dotMngr.lastProgramUsed = prg;
            if (grMngr.mainView.isBlank() == null){grMngr.mainView.setBlank(cfgMngr.backgroundColor);}
            dotMngr.load(f, prg, parser);
            // in case a font was defined in the SVG file, make it the font used here (to show in Prefs)
            ConfigManager.defaultFont = VText.getMainFont();
            grMngr.mainView.setTitle(ConfigManager.MAIN_TITLE+" - "+f.getAbsolutePath());
            grMngr.reveal();
            // do not remember camera's initial location (before global view)
            if (grMngr.previousLocations.size()==1){grMngr.previousLocations.removeElementAt(0);}
            if (grMngr.rView != null){
                grMngr.rView.getGlobalView(grMngr.mSpace.getCamera(1),100);
                grMngr.cameraMoved(null, null, 0);
            }
            cfgMngr.notifyPlugins(Plugin.NOTIFY_PLUGIN_FILE_LOADED);
        }
    }

    void loadSVG(File f){
        grMngr.gp.setMessage("Parsing SVG...");
        grMngr.gp.setProgress(10);
        grMngr.gp.setVisible(true);
        try {
            grMngr.gp.setProgress(30);
            cfgMngr.lastFileOpened = f;
            dotMngr.lastProgramUsed = DOTManager.SVG_FILE;
            Document svgDoc=f.getName().toLowerCase().endsWith(".svgz")?
                Utils.parse(new BufferedInputStream(new GZIPInputStream(
                new FileInputStream(f))),false):
            Utils.parse(f,false);
            grMngr.gp.setMessage("Building graph...");
            grMngr.gp.setProgress(80);
            if (grMngr.mainView.isBlank() == null){grMngr.mainView.setBlank(cfgMngr.backgroundColor);}
            SVGReader.load(svgDoc, grMngr.mSpace, true,
                f.toURI().toURL().toString());
            grMngr.seekBoundingBox();
            grMngr.buildLogicalStructure();
            ConfigManager.defaultFont = VText.getMainFont();
            grMngr.mainView.setTitle(ConfigManager.MAIN_TITLE+" - "+f.getAbsolutePath());
            grMngr.reveal();
            //do not remember camera's initial location (before global view)
            if (grMngr.previousLocations.size()==1){grMngr.previousLocations.removeElementAt(0);}
            if (grMngr.rView != null){
                grMngr.rView.getGlobalView(grMngr.mSpace.getCamera(1),100);
                grMngr.cameraMoved(null, null, 0);
            }
            grMngr.gp.setVisible(false);
            cfgMngr.notifyPlugins(Plugin.NOTIFY_PLUGIN_FILE_LOADED);
        }
        catch (Exception ex){
            grMngr.reveal();
            grMngr.gp.setVisible(false);
            ex.printStackTrace();
            JOptionPane.showMessageDialog(grMngr.mainView.getFrame(),Messages.loadError+f.toString());
        }
    }

    /** Method used by ZGRViewer - Applet to get the server-side generated SVG file.
     * Adds acceptance of gzip encoding in request and handles response with gzip
     * encoding. (i.e. SVGZ format).
     */
    void loadSVG(String svgFileURL){
    try {
        // Construct a URL, get the connection and set that gzip is an accepted
        // encoding. This gives the server a chance to dynamically deliver "svgz"
        // content.
        //
        URL url = new URL(svgFileURL);
        URLConnection c = url.openConnection();
        c.setRequestProperty("Accept-Encoding", "gzip");
        // Connection is opened when something is requested - the header or
        // the content. The encoding is needed to determine if data is in gzip format.
        //
        InputStream is = c.getInputStream();
        String encoding = c.getContentEncoding();
        if ("gzip".equals(encoding) || "x-gzip".equals(encoding) || svgFileURL.toLowerCase().endsWith(".svgz"))
        {
            // handle gzip stream
            is = new GZIPInputStream(is);
        }
        is = new BufferedInputStream(is);

        // parse the content of the stream
        Document svgDoc = AppletUtils.parse(is, false);
        if (svgDoc != null){
        if (grMngr.mainView.isBlank() == null){grMngr.mainView.setBlank(cfgMngr.backgroundColor);}
        SVGReader.load(svgDoc, grMngr.mSpace, true, svgFileURL);
        grMngr.seekBoundingBox();
        grMngr.buildLogicalStructure();
        ConfigManager.defaultFont = VText.getMainFont();
        grMngr.reveal();
        //do not remember camera's initial location (before global view)
        if (grMngr.previousLocations.size()==1){grMngr.previousLocations.removeElementAt(0);}
        if (grMngr.rView != null){
            grMngr.rView.getGlobalView(grMngr.mSpace.getCamera(1), 100);
        }
        grMngr.cameraMoved(null, null, 0);
        }
        else {
        System.err.println("An error occured while loading file " + svgFileURL);
        }
    }
    catch (Exception ex){grMngr.reveal();ex.printStackTrace();}
    }

    void load(String commandLine, String sourceFile){
        grMngr.reset();
        dotMngr.loadCustom(sourceFile, commandLine);
        //in case a font was defined in the SVG file, make it the font used here (to show in Prefs)
        ConfigManager.defaultFont = VText.getMainFont();
        grMngr.mainView.setTitle(ConfigManager.MAIN_TITLE+" - "+sourceFile);
        //  grMngr.getGlobalView();
        grMngr.reveal();
        // do not remember camera's initial location (before global view)
        if (grMngr.previousLocations.size()==1){grMngr.previousLocations.removeElementAt(0);}
        if (grMngr.rView != null){
            grMngr.rView.getGlobalView(grMngr.mSpace.getCamera(1),100);
            grMngr.cameraMoved(null, null, 0);
        }
        cfgMngr.notifyPlugins(Plugin.NOTIFY_PLUGIN_FILE_LOADED);
    }

    void reloadFile(){
        //XXX: TODO: support integrated parser during reload
        if (cfgMngr.lastFileOpened != null){
            grMngr.reset();
            if (dotMngr.lastProgramUsed == DOTManager.SVG_FILE){
                this.loadSVG(cfgMngr.lastFileOpened);
            }
            else {
                this.loadFile(cfgMngr.lastFileOpened, dotMngr.lastProgramUsed, false);
            }
        }
    }
    
    void getPrologClause(JMenuItem menu, final short prg, final boolean parser) {
    	JTextField metaClauseField = new JTextField(1);
//		JCheckBox directedEdgeEnabled = new JCheckBox("Enable directed edges");
		JTextArea clauseFieldArea = new JTextArea(5, 20);				
		JScrollPane clauseField = new JScrollPane(clauseFieldArea); 
		
		JButton button = new JButton("Visualise");
	    
        PromptSupport.setPrompt("clause description here", metaClauseField);
        PromptSupport.setPrompt("clause here", clauseFieldArea);      
        
        PromptSupport.setFontStyle(Font.ITALIC, metaClauseField);
        PromptSupport.setFontStyle(Font.ITALIC, clauseFieldArea);
        
		JPanel fieldPanel = new JPanel();
		fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.PAGE_AXIS));
//		fieldPanel.setLayout(new GridLayout());
//		JPanel topPanel = new JPanel();
//		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
//		topPanel.add(metaClauseField);
//		topPanel.add(directedEdgeEnabled);
		
		metaClauseField.setMaximumSize(new Dimension(Integer.MAX_VALUE, metaClauseField.getPreferredSize().height));
		
		fieldPanel.add(metaClauseField);
		fieldPanel.add(clauseField);
		
    	JTextField xField = new JTextField(5);
        JTextField yField = new JTextField(5);
        
        UIManager.put("OptionPane.minimumSize",new Dimension(grMngr.getPanelWidth(), 200)); 
        
        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Clause description"));
        myPanel.add(xField);
//        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("Clause"));
        myPanel.add(yField);

        int result = JOptionPane.showConfirmDialog(grMngr.mainView.getFrame(), fieldPanel, 
                 "Enter Prolog clause", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
           System.out.println("x value: " + metaClauseField.getText());
           System.out.println("y value: " + clauseFieldArea.getText());
        }
                
        List<Node> nodes = getPrologStructure(metaClauseField.getText(), clauseFieldArea.getText()); for(Node n: nodes) System.out.println(n);
        transformPrologToDot(nodes);
        open(new File("."), DOTManager.DOT_PROGRAM, false);
    }
    
    private List<Node> getPrologStructure(String metaClauseField, String clauseFieldArea) {
    	String metaClause = metaClauseField.trim();
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
		
		try {
			ParserProcess pp = new ParserProcess(preds);
			List<Node> nodes = pp.traverse(clauseFieldArea.trim());
			return nodes;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
    }
    
    private void transformPrologToDot(List<Node> nodes) {
    	
    }
}
