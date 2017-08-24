package main;

import java.util.ArrayList;
import java.util.List;

public class MetaPredicate {
	private String name;
	private int arity;
	private List<String> roleNames;
	
	public MetaPredicate(String name, int arity){
		this.name = name;
		this.arity = arity;
		this.roleNames = new ArrayList<>();
		
	}
	
	public String getRoleName(int arg){
		return this.roleNames.get(arg);
	}
	
	public void addRoleName(String roleName){
		this.roleNames.add(roleName);
	}
	
	public String getPred(){
		return name + "/" + arity;
	}
}
