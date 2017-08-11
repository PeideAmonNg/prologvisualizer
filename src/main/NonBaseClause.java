package main;
import java.util.ArrayList;
import java.util.List;

// A predicate clause that is conditionally true, with if-part structures e.g. round(Planet) :- not_flat(Planet).
public class NonBaseClause extends BaseClause {
	private List<Structure> childStructures = new ArrayList<>();

	public List<Structure> getChildStructures() {
		return childStructures;
	}

//	public void setChildStructures(List<Structure> childStructures) {
//		this.childStructures = childStructures;
//	}
	
	@Override
	public void process(String raw){
		
	}

}
