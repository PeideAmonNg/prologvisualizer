package main;
import java.util.ArrayList;
import java.util.List;

import com.igormaznitsa.prologparser.terms.AbstractPrologTerm;

public class Predicate1 {
	
	public List<AbstractPrologTerm> clauses;
	
	boolean isRecursive = false;
	
	public Predicate1(boolean isRecursive){
		this.isRecursive = isRecursive;
		clauses = new ArrayList<AbstractPrologTerm>();		
	}
	
	public void add(AbstractPrologTerm clause){
		clauses.add(clause);
	}
	
}
