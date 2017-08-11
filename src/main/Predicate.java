package main;
import java.util.ArrayList;
import java.util.List;

import com.igormaznitsa.prologparser.terms.AbstractPrologTerm;

public class Predicate {
	public static enum PredicateType {RULE, FACT};
	
	public String structure; //e.g. append/3
	public List<AbstractPrologTerm> clauses; // collection of all clauses of this predicate.
	
	public Predicate(String struct){
		structure = struct;
		clauses = new ArrayList<>();
	}
	
	public String getStructure(){
		return structure;
	};
	
	public void addClause(AbstractPrologTerm clause){
		clauses.add(clause);
	}
	
	public List<AbstractPrologTerm> getClauses(){
		return clauses;
	}
	
	public static Predicate getPredicate(List<Predicate> predicates, String struct){
		for(Predicate pred: predicates){
			if(pred.getStructure().equals(struct)){
				return pred;
			}
		}
		
		return null;
	}
	
}
