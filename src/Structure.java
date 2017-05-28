import java.util.List;

// Examples of a Structure: 
//		mother_jane.
//		mother(jane).
public class Structure {
	private String structure;
	private Predicate predicate;
	private List<Param> params;
	
	public Predicate getPredicate() {
		return predicate;
	}

	public void setPredicate(Predicate predicate) {
		this.predicate = predicate;
	}

	public List<Param> getParams() {
		return params;
	}

	public void setParams(List<Param> params) {
		this.params = params;
	}
	@Override
	public String toString(){
		return structure;
	}

	public void process(String raw, List<Predicate> predicates, List<Param> paramList){
		this.structure = raw;
		// Given raw Prolog code, find which params this Structure is using, and its predicate.		
		
	}
}
