import com.igormaznitsa.prologparser.PrologParser;
import com.igormaznitsa.prologparser.terms.PrologStructure;

public class Parser {
	public static void main(String[] arg){
		final PrologParser parser = new PrologParser(null);
		String append3 = "append([],L,L). " + "append([H|T],L2,[H|L3])  :-  append(T,L2,L3)."; // Predicate append/3 defined.
		
		
	  try {
			PrologStructure structure;
			structure = (PrologStructure) parser.nextSentence(append3);
			System.out.println(structure);
			structure = (PrologStructure) parser.nextSentence("append([H|T],L2,[H|L3])  :-  append(T,L2,L3).");
//			System.out.println(((PrologStructure) structure.getElement(0)).getClass());
//			System.out.println(structure.getElement(0).getClass().getSimpleName().equals("PrologStructure"));
			
			// System.out.println(structure.getElement(0).getText()+'
			// '+structure.getElement(1).getText());
		} catch (Exception unexpected) {
			throw new RuntimeException(unexpected);
		}
	}
}
