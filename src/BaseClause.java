import java.util.ArrayList;
import java.util.List;

// A predicate clause that is always true, without if structures, e.g. round(earth).
public class BaseClause {
	private List<Param> params = new ArrayList<>();
	private Structure structure;
	
	public List<Param> getParams() {
		return params;
	}
//	public void setParams(List<Param> params) {
//		this.params = params;
//	}
	public Structure getStructure() {
		return structure;
	}
//	public void setStructure(Structure structure) {
//		this.structure = structure;
//	}
	
	public void process(String raw){
//	Given raw Prolog code, set params and structure here.
	}

	
}
