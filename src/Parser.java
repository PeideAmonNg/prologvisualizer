import java.awt.Dimension;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.igormaznitsa.prologparser.PrologCharDataSource;
import com.igormaznitsa.prologparser.PrologParser;
import com.igormaznitsa.prologparser.terms.AbstractPrologTerm;
import com.igormaznitsa.prologparser.terms.PrologAtom;
import com.igormaznitsa.prologparser.terms.PrologStructure;
import com.igormaznitsa.prologparser.terms.PrologTermType;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

public class Parser {
	
	public Parser(){
		visualise(generateStructure());
	}
	
	public List<Predicate> generateStructure(){
		System.out.println("hello world");
//		String append3_1 = "append([],L, L). \n "; // Predicate append/3 defined.
//		String append3_2 = "append([H|T],L2,[H|L3])  :-  append(T,L2,L3).";
//		String facts = " john_is_cold. raining. john_Forgot_His_Raincoat. fred_lost_his_car_keys. peter_footballer.";
		
		
		PrologCharDataSource source;
				
		List<Predicate> predicateList = new ArrayList<>();
			
		try {
			source = new PrologCharDataSource(new FileReader(new File("C:\\Users\\pc\\Desktop\\prologvisualizer\\prologProgram.pl")));
			final PrologParser parser = new PrologParser(null);
			AbstractPrologTerm sentence = parser.nextSentence(source);
			List<AbstractPrologTerm> sentences = new ArrayList<AbstractPrologTerm>();			
			
			// Read Prolog program code line by line.
			while(sentence != null){
				sentences.add(sentence);
								
				// Clauses with no bodies and 0-arity e.g. mother_jane.
				if(sentence.getType() == PrologTermType.ATOM){
					System.out.println(sentence + " is a " + PrologTermType.ATOM);
					
					PrologAtom atom = (PrologAtom) sentence;
					Predicate pred = new Predicate(atom.getText() + "/0");
					pred.addClause(sentence);
					predicateList.add(pred);
					
				}
				// Clauses with > 0-arity e.g. mother(jane). | mother(jane) :- has_child(jane). 
				else if(sentence.getType() == PrologTermType.STRUCT){
					System.out.println(sentence + " is a " + PrologTermType.STRUCT);
					
					PrologStructure elem1 = (PrologStructure) ((PrologStructure) sentence).getElement(0); // The head bit of a clause.
					String predicateStruct = elem1.getFunctor().getText() + "/" + elem1.getArity();
					Predicate pred = Predicate.getPredicate(predicateList, predicateStruct);
					if(pred == null){
						pred = new Predicate(predicateStruct);
						pred.addClause(sentence);
						predicateList.add(pred);
					}else{
						pred.addClause(sentence);
					}			
				
				}else{
//					System.out.println(sentence + "is a something else" );
					throw new RuntimeException();
				}
				
				sentence = parser.nextSentence();
			}
			
			return predicateList;
		} catch (Exception unexpected) {
			throw new RuntimeException(unexpected);
		}
	}
	
	public static void visualise(List<Predicate> predicates){
		System.out.println("in visualise");
		for(Predicate pred: predicates){
			System.out.println(pred.structure);
			for(AbstractPrologTerm clause: pred.getClauses()){
				System.out.println("\t" + clause.getType() + "\t" + clause.toString());
			}
		}
	}
	
	public static void main(String[] arg) {
		new Parser();
		System.out.println("----------------------------------------------------------------------");
		try{
			final PrologParser parser = new PrologParser(null);
			PrologStructure s = (PrologStructure)parser.nextSentence("mother(jane).");
			PrologStructure ss = (PrologStructure)parser.nextSentence("mother(jane) :- has_child(jane), is_female(jane).");
//			System.out.println(s.getFunctor().getText() + "/" + s.getArity());
//			System.out.println(parser.nextSentence("mother_jane.").getType());
//			System.out.println(parser.nextSentence("mother(jane).").getType());
//			System.out.println(ss.getFunctor().getText() + "/" + ss.getArity());
//			System.out.println(ss.getElement(0).getType());
//			System.out.println(s.getElement(0));
////			System.out.println(s.getElement(1));
//			System.out.println(ss.getFunctor());
//			PrologAtom a = (PrologAtom) parser.nextSentence("mother_jane.");
//			System.out.println(a.getText());
//			PrologStructure structElem1 = (PrologStructure)ss.getElement(0);
//			System.out.println(structElem1.getFunctor() + "/" + structElem1.getArity());
			System.out.println(s.getText());
			System.out.println(ss.getText());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		SimpleGraphView sgv = new SimpleGraphView(); //We create our graph in here
		 // The Layout<V, E> is parameterized by the vertex and edge types
		 Layout<Integer, String> layout = new CircleLayout<>(sgv.graph);
		 layout.setSize(new Dimension(300,300)); // sets the initial size of the space
		 // The BasicVisualizationServer<V,E> is parameterized by the edge types
		 BasicVisualizationServer<Integer,String> vv =
		 new BasicVisualizationServer<Integer,String>(layout);
		 vv.setPreferredSize(new Dimension(350,350)); //Sets the viewing area size

		 JFrame frame = new JFrame("Simple Graph View");
		 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 frame.getContentPane().add(vv);
		 frame.pack();
		 frame.setVisible(true); 
		 
		 
	}
	
}
