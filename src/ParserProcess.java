import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.igormaznitsa.prologparser.PrologCharDataSource;
import com.igormaznitsa.prologparser.PrologParser;
import com.igormaznitsa.prologparser.operators.Operator;
import com.igormaznitsa.prologparser.operators.OperatorType;
import com.igormaznitsa.prologparser.terms.AbstractPrologTerm;
import com.igormaznitsa.prologparser.terms.PrologList;
import com.igormaznitsa.prologparser.terms.PrologStructure;
import com.igormaznitsa.prologparser.terms.PrologTermType;

import Visual.FunctorNode;
import Visual.ListOperatorNode;
import Visual.MainArgumentNode;
import Visual.Node;
import Visual.OperatorNode;
import Visual.VariableNode;

public class ParserProcess {
	
	private List<Node> nodes = new ArrayList<>();
	private List<List<Node>> predicateClauses = new ArrayList<>();
	
	
	private int listArgumentCount = 1;
	
	public ParserProcess(){
		PrologCharDataSource source; 
		
		
		try{
			source = new PrologCharDataSource(new FileReader(new File(new File("").getAbsolutePath() + "/prologProgram.pl")));
			
			
			final PrologParser parser = new PrologParser(null);
			AbstractPrologTerm sentence = parser.nextSentence(source);
			
			 
			 
			 
			 while(sentence != null){
				 traverseClauseBody(sentence, new MainArgumentNode(""));
				 predicateClauses.add(this.nodes);
				 this.nodes = new ArrayList<>();
				 listArgumentCount = 1;
				 sentence = parser.nextSentence(source);
				 System.out.println("----------------------------------------------------------------------------");
			 }
			 
			
//			traverseClauseBody(sentence, null);
			
			
			// Case: A non-base clause i.e. Clause containing ":-"
//			if(sentence.getType() == PrologTermType.STRUCT){ 
//				System.out.println("hi1");
//				PrologStructure prologStructureSentence = (PrologStructure) sentence;
//				if(prologStructureSentence.getFunctor().getType().toString() == "OPERATOR"){
//					System.out.println("hi2");
//					if(((Operator)(prologStructureSentence.getFunctor())).getOperatorType() == OperatorType.XFX){ // XFX is ":-".
//						System.out.println("hi3");
//						System.out.println(prologStructureSentence.getElement(1));
//						traverseClauseBody(prologStructureSentence.getElement(1), null);					
//					}else{
//						throw new Exception("3.Hey re-check the if-else condition.");
//					}
//				}else if(prologStructureSentence.getFunctor().getType() == PrologTermType.ATOM){
//					System.out.println("ok");
//					System.out.println();
//					System.out.println(prologStructureSentence.getText().toString());
//				}else{					
//					throw new Exception("2.Hey re-check the if-else condition.");
//				}
//									
//			}else{ // Example clause here: mother_jane.
//				throw new Exception("1.Hey re-check the if-else condition.");
//			}
			
			System.out.println("-----------------------------The End----------------------------------");
			
			for(List<Node> nodeList: predicateClauses){
				for(Node n: nodeList){
					System.out.print(n.getNode() + "\t");
				}
				System.out.println();
				System.out.println("------------------------------------------------------------------------------");
			}
			
			for(List<Node> nodeList: predicateClauses){
				for(Node n: nodeList){
					System.out.print(n.getNode() + "\t");
					System.out.print(n.getClass().toString() + "->" + n.getNode() + "\t");
				}
				System.out.println();
				System.out.println(nodeList.size());
				System.out.println("------------------------------------------------------------------------------");
			}
			
//			System.out.println(nodes.size());
//			for(Node n: nodes){
//				System.out.println(n.getNode());
//			}
//			for(Node n: nodes){
//				System.out.println(n);
//			}
			
//			System.out.println(parser.nextSentence("mother(jane).").getType() == PrologTermType.STRUCT);
		}catch(Exception e){
			e.printStackTrace();
			
		}
	}
	
	// Recursively traverse clauses in the body of a current clause.
	// parentNode used for situations like NY is Y + 1, in this case parentNode is NY.
	private Node traverseClauseBody(AbstractPrologTerm term, Node parentNode) throws Exception{
		if(term.getType() == PrologTermType.STRUCT){
			System.out.println("---struct---");
			
			// If: there are still clauses separated by comma.
			if(((PrologStructure) term).getFunctor().getType().toString() == "OPERATOR"){
				if(((PrologStructure)term).getFunctor().getText().equals(",")){
					System.out.println("------comma OP------, ");

					System.out.println(((PrologStructure) term).getFunctor().toString());
					System.out.println(term.toString() + ((PrologStructure) term).getFunctor().getType().toString());
					System.out.println(((PrologStructure) term).getFunctor().getType() == PrologTermType.OPERATOR);
					
//					if(parentNode==null){
//						parentNode = new ContainerNode(((PrologStructure)((PrologStructure) term).getElement(0)).getFunctor().getText());
//					}
					
					System.out.println(((PrologStructure)term).getElement(0));
					System.out.println(((PrologStructure)term).getElement(1));
					
					traverseClauseBody(((PrologStructure)term).getElement(0), parentNode);
					traverseClauseBody(((PrologStructure)term).getElement(1), parentNode);


				}else if((((PrologStructure)term).getFunctor()).getText().equals("is")){ // Example: NY is Y + 1
					System.out.println("------is OP------");
					System.out.println(((Operator)((PrologStructure)term).getFunctor()).getOperatorType()); // == PrologTermType.XFX
					System.out.println(((PrologStructure)term).getElement(0)); // == VariableNode i.e. NY.
					System.out.println(((PrologStructure)((PrologStructure)term).getElement(1)).getFunctor().getText()); // == 
					Node variableNode = retrieveNode((((PrologStructure)term).getElement(0).getText()), Node.TYPE.Variable);
					Node arithmeticOpNode = traverseClauseBody(((PrologStructure)term).getElement(1), variableNode);					
					variableNode.addFromNode(arithmeticOpNode);
										
					return variableNode;
					
				}else if(((Operator)(((PrologStructure) term).getFunctor())).getOperatorType() == OperatorType.XFX){ // :-
					System.out.println("------ :- OP------");					
					System.out.println(((PrologStructure)term).getElement(0)); // == VariableNode i.e. NY.
					System.out.println(((PrologStructure)term).getElement(1)); 
					System.out.println(((PrologStructure)((PrologStructure)term).getElement(1)).getFunctor().getText()); // == 
					traverseClauseBody((((PrologStructure)term).getElement(0)), new MainArgumentNode(""));
					traverseClauseBody((((PrologStructure)term).getElement(1)), null);
					
				}else if(((Operator)(((PrologStructure) term).getFunctor())).getOperatorType() == OperatorType.YFX){ // +,-,*,/
					System.out.println("------maths OP------");
					// Aim: To make a OpNode and join the input nodes to OpNode.
					System.out.println(((Operator)(((PrologStructure) term).getFunctor())).getOperatorType());
					System.out.println(((PrologStructure) term).getFunctor().getText());
					System.out.println("yay");
					
					Node arithmeticOpNode = new OperatorNode(((PrologStructure) term).getFunctor().getText());
					this.nodes.add(arithmeticOpNode);
					
					Node leftVarNode = retrieveNode(((PrologStructure) term).getElement(0).getText(), Node.TYPE.Variable);
					Node rightVarNode = traverseClauseBody(((PrologStructure) term).getElement(1), arithmeticOpNode);
					arithmeticOpNode.addFromNode(leftVarNode);
					arithmeticOpNode.addFromNode(rightVarNode);
					
					return arithmeticOpNode;
					
				}else{		
					System.out.println(term);
					System.out.println(((Operator)(((PrologStructure) term).getFunctor())).getOperatorType());
					System.out.println(((PrologStructure) term).getFunctor().toString());
					throw new Exception("else statement in traverseClauseBody");
				}
			}else{ // Else: the last clause (no more comma following).
				System.out.println("---LAST CLAUSE IN BODY---");
				System.out.println(((PrologStructure)term).getFunctor().getType());			
				System.out.println("predicate name is " + ((PrologStructure)term).getFunctor().getText());
				System.out.println(((PrologStructure)term).getElement(0));			
				System.out.println(((PrologStructure)term).getElement(1));
				System.out.println(((PrologStructure)term).getElement(2));				
				System.out.println(((PrologStructure)term).getArity());			
				System.out.println("^^^^^Fly " + ((PrologStructure) term).getElement(1).getText());
				System.out.println("^^^^^Fly " + ((PrologStructure) term).getElement(2).getText());
						
				if(parentNode == null){
					Node fNode = retrieveNode(((PrologStructure) term).getFunctor().getText(), Node.TYPE.Functor);
					this.nodes.add(fNode);
					parentNode = fNode;
					System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
				}
				
			
				int arity = ((PrologStructure) term).getArity();
				int index = 0;
				while(index < arity){
					if(parentNode != null && parentNode.getNodeType() == Node.TYPE.MainArgument){
						traverseClauseBody(((PrologStructure) term).getElement(index), parentNode);
					}else{
						traverseClauseBody(((PrologStructure) term).getElement(index), null);
					}
					
					index++;
				}
			
			}			
		}else if(term.getType() == PrologTermType.ATOM){ // Example: NY is Y + 1. 1 is an ATOM. Y is a VAR.
			System.out.println("---ATOM TERM---");
			System.out.println(term);
			return retrieveNode(term.getText(), Node.TYPE.Variable);
			
		}else if(term.getType() == PrologTermType.VAR){
			System.out.println("---VAR TERM---");
			System.out.println(term);
//			return retrieveNode(term.getText(), Node.TYPE.Variable);
			if(parentNode != null) return retrieveNode(term.getText(), Node.TYPE.Variable);
			else return retrieveNode(term.getText(), Node.TYPE.Variable);
		}else if(term.getType() == PrologTermType.LIST){
			System.out.println("---LIST TERM---");
			System.out.println(term);
			System.out.println(((PrologList) term).getArity());
			
			
			if(((PrologList) term).getElement(0) != null || ((PrologList) term).getElement(1) != null){
//				throw new Exception("need to implement List with arguments");
				System.out.println("need to implement list arg");
				System.out.println(((PrologList) term).getElement(0).getText());
				Node listHeadNode = retrieveNode(((PrologList) term).getElement(0).getText(), Node.TYPE.Variable);
				Node listTailNode = retrieveNode(((PrologList) term).getElement(1).getText(), Node.TYPE.Variable);
				
				Node listNode = retrieveNode(null, Node.TYPE.ListOperator);
				
			}else{
				return retrieveNode(null, Node.TYPE.ListOperator);
			}
		
		}else{
			System.out.println(term.getType());
			throw new Exception("cant go here");
		}
		
		return null;
	}
	
	
	// Returns an existing VariableNode, otherwise a newly created one.
	private Node retrieveNode(String nodeName, Node.TYPE type) throws Exception{
//		if(nodeName.equals("") || nodeName == null){
//			throw new Exception("nodeName cant be empty or null");
//		}
		
		for(Node n: this.nodes){
			if(n.getNode().equals(nodeName)){
				return n;
			}
		}
		
		Node n;
		if(type == Node.TYPE.Variable){
			n = new VariableNode(nodeName);			
		}else if(type == Node.TYPE.Functor){
			n = new FunctorNode(nodeName);
			System.out.println("Created new FunctorNode");
		}else if(type == Node.TYPE.ListOperator){
			n = new ListOperatorNode("List" + this.listArgumentCount);
			this.listArgumentCount++;
		}else if(type == Node.TYPE.MainArgument){
			n = new MainArgumentNode(nodeName);
		}else if(type == Node.TYPE.Operator){
			n = new OperatorNode(nodeName);
		}else{
			System.out.println(type);
			throw new Exception("can't go here in retrieveNode");
		}
		
		if(type != Node.TYPE.Functor){
			this.nodes.add(n);
		}
							
		return n;
	}
}
