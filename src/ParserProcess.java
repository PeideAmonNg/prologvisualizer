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
	private int listOperatorNodeCount = 1;
	private int mainArgCount = 1;
	private boolean isOuterMostClause = true;

	
	//----------------------------------Variables used for regressionTestBuiltNodeCount()----------------------------------
//	public int[] prologProgramFileClauseNodeCount = new int[] { 5, 17, 8, 6, 6, 6, 9, 9, 9, 10, 10 };
	public int[] prologProgramFileClauseNodeCount = new int[] { 2, 14, 4, 4, 4, 4, 6, 6, 6, 8, 8};
//	public int[] siftFileClauseNodeCount = new int[] { 6, 11, 10 };
	public int[] siftFileClauseNodeCount = new int[] { 3, 8, 7 };
	public int[][] correctNodeCountArray = new int[][] { prologProgramFileClauseNodeCount, siftFileClauseNodeCount };
	private String prologProgramFilePath = "/prologProgram.pl";
	private String siftFilePath = "/sift.pl";
	private String testFilePath = "/pro.pl";
	private String routePath = "/route.pl";
	public String[] files = new String[] { prologProgramFilePath, siftFilePath };
	//END-------------------------------Variables used for regressionTestBuiltNodeCount()-------------------------------END
	

	public ParserProcess() {
//		run();
//		regressionTestBuiltNodeCount();
	}

	private void initializeFields(){
		this.nodes = new ArrayList<>();
		listArgumentCount = 1;
		mainArgCount = 1;
	}
	
	private void run(){
		PrologCharDataSource source;

		try {
			source = new PrologCharDataSource(new FileReader(new File(new File("./prologfiles").getPath() + prologProgramFilePath)));

			final PrologParser parser = new PrologParser(null);
			AbstractPrologTerm sentence = parser.nextSentence(source);


			while (sentence != null) {
				traverseClauseBody(sentence, new MainArgumentNode(""), true);
				predicateClauses.add(this.nodes);
				initializeFields();
				sentence = parser.nextSentence(source);
				System.out.println("----------------------------------------------------------------------------");
			}



			System.out.println("-----------------------------The End----------------------------------");
	
			
			for (List<Node> nodeList : predicateClauses) {
				for (Node n : nodeList) {
					System.out.print(n.getNodeName() + "\t");
				}
				System.out.println();
				for (Node n : nodeList) {
					System.out.print(n.getClass().toString() + "->" + n.getNodeName() + "\t");
				}
				System.out.println();
				System.out.println(nodeList.size());
				System.out.println("------------------------------------------------------------------------------");
			}

			
			
			
			Visualiser vis = new Visualiser();
			vis.visualise(this.predicateClauses.get(0));

		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	
	public List<Node> traverse(String sent) throws Exception{
		initializeFields();
		
			final PrologParser parser = new PrologParser(null);
			AbstractPrologTerm sentence = parser.nextSentence(sent);
			traverseClauseBody(sentence, new MainArgumentNode(""), true);
			predicateClauses.add(this.nodes);
			initializeFields();
			return predicateClauses.get(0);
			
		
	}
	
	// Recursively traverse clauses in the body of the current outer clause being parsed.
	// parentNode used for situations like NY is Y + 1, in this case parentNode is NY.
	// isPartOfClauseHead is used to distinguish weather the parameters are in a functor that is in a clause head (MainArgumentNode parameter) or 
	// in a functor that is in the functor body (VariableNode parameter).
	public Node traverseClauseBody(AbstractPrologTerm term, Node parentNode, boolean isPartOfClauseHead)
			throws Exception {
		System.out.println("in traverseClauseBody");
		
		if (parentNode == null){
			System.out.println("in traverseClauseBody, parentNode is null");
		}else{
			System.out.println("in traverseClauseBody, parentNode is" + parentNode.getClass().getName());
		}
		System.out.println(term.getType());
		
		if (term.getType() == PrologTermType.STRUCT) {
			System.out.println("---struct---");

			// If: there are still clauses separated by comma.
			if (((PrologStructure) term).getFunctor().getType().toString() == "OPERATOR") {
				System.out.println("is an operator");
				if (((PrologStructure) term).getFunctor().getText().equals(",")) {
					System.out.println("------comma OP------, ");

					System.out.println(((PrologStructure) term).getFunctor().toString());
					System.out.println(term.toString() + ((PrologStructure) term).getFunctor().getType().toString());
					System.out.println(((PrologStructure) term).getFunctor().getType() == PrologTermType.OPERATOR);

					System.out.println(((PrologStructure) term).getElement(0));
					System.out.println(((PrologStructure) term).getElement(1));

					traverseClauseBody(((PrologStructure) term).getElement(0), parentNode, false);
					traverseClauseBody(((PrologStructure) term).getElement(1), parentNode, false);

				} else if ((((PrologStructure) term).getFunctor()).getText().equals("is")) { // Example: NY is Y +1
					System.out.println("------is OP------");
					System.out.println(((Operator) ((PrologStructure) term).getFunctor()).getOperatorType()); // == PrologTermType.XFX
					System.out.println(((PrologStructure) term).getElement(0)); // == VariableNode i.e. NY.
					System.out
							.println(((PrologStructure) ((PrologStructure) term).getElement(1)).getFunctor().getText()); // ==
					Node variableNode = retrieveNode((((PrologStructure) term).getElement(0).getText()),
							Node.TYPE.Variable, false);
					Node arithmeticOpNode = traverseClauseBody(((PrologStructure) term).getElement(1), variableNode,
							false);
					variableNode.addFromNode(arithmeticOpNode);

					return variableNode;

				} else if (((Operator) (((PrologStructure) term).getFunctor())).getOperatorType() == OperatorType.XFX) { // :-, <, >, ==, \=, etc.
					System.out.println("------ XFX OP------");
					String op = ((PrologStructure) term).getFunctor().getText().trim();
					if(op.equals(":-")){
						System.out.println("------ :- OP------");
						System.out.println(((PrologStructure) term).getElement(0)); // == VariableNode i.e. NY.
						System.out.println(((PrologStructure) term).getElement(1));
						// System.out.println(((PrologStructure)((PrologStructure)term).getElement(1)).getFunctor().getText());
						// // ==
						traverseClauseBody((((PrologStructure) term).getElement(0)), new MainArgumentNode(""), true);
						traverseClauseBody((((PrologStructure) term).getElement(1)), null, false); //we are now traversing the clause body, so the parentNode is set to null, as opposed to the original MainArgumentNode.
					}else if (op.equals(">") || op.equals("<") || op.equals(">=") || op.equals("=<") || op.equals("==")
							|| op.equals("\\=")) {
						System.out.println("------equality OP------");
						System.out.println(((PrologStructure) term).getFunctor().getType());
						
						
							Node leftNode = retrieveNode((((PrologStructure) term).getElement(0).getText()),
									Node.TYPE.Variable, false);
							Node rightNode = traverseClauseBody(((PrologStructure) term).getElement(1), null, false);
							Node equalityNode = retrieveNode(((PrologStructure) term).getFunctor().getText(),
									Node.TYPE.Operator, true);
							equalityNode.addFromNode(leftNode);
							equalityNode.addFromNode(rightNode);

							return equalityNode;
					}else{
						System.out.println(((PrologStructure) term).getFunctor().getText());
						throw new Exception("an XFX operator not handled");
					}				
				} else if (((Operator) (((PrologStructure) term).getFunctor())).getOperatorType() == OperatorType.YFX) { // +,-,*,/
					System.out.println("------maths OP------");
					// Aim: To make a OpNode and join the input nodes to OpNode.
					System.out.println(((Operator) (((PrologStructure) term).getFunctor())).getOperatorType());
					System.out.println(((PrologStructure) term).getFunctor().getText());
					System.out.println("yay");

					Node arithmeticOpNode = retrieveNode(((PrologStructure) term).getFunctor().getText(),
							Node.TYPE.Operator, true);
					Node leftVarNode = retrieveNode(((PrologStructure) term).getElement(0).getText(),
							Node.TYPE.Variable, false);
					Node rightVarNode = traverseClauseBody(((PrologStructure) term).getElement(1), arithmeticOpNode,
							false);
					arithmeticOpNode.addFromNode(leftVarNode);
					arithmeticOpNode.addFromNode(rightVarNode);

					return arithmeticOpNode;

				} else {
					System.out.println(term);
					System.out.println(((Operator) (((PrologStructure) term).getFunctor())).getOperatorType());
					System.out.println(((PrologStructure) term).getFunctor().toString());
					throw new Exception("else statement in traverseClauseBody");
				}
			} else { // At this point, the term is a functor e.g. increment(X, Result) 
				System.out.println("---LAST CLAUSE IN BODY---");
				System.out.println(((PrologStructure) term).getFunctor().getType());
				System.out.println("predicate name is " + ((PrologStructure) term).getFunctor().getText());
				System.out.println(((PrologStructure) term).getElement(0));
//				System.out.println(((PrologStructure) term).getElement(1));
				System.out.println(((PrologStructure) term).getArity());
//				System.out.println("^^^^^Fly " + ((PrologStructure) term).getElement(1).getText());
				
				Node fNode = null;
				
				if (parentNode == null){
					System.out.println("in traverseClauseBody, parentNode is null");
				}else{
					System.out.println("in traverseClauseBody, parentNode is" + parentNode.getClass().getName());
				}
				
				if (!((PrologStructure) term).getFunctor().getText().equals("not") && (parentNode == null || !isPartOfClauseHead)) { 
					fNode = retrieveNode(((PrologStructure) term).getFunctor().getText(), Node.TYPE.Functor, true);
					System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
				}
				
				if (parentNode == null){
					System.out.println("about to loop the parameters, the parentNode is null");
				}else{
					System.out.println("about to loop the parameters, the parentNode is " + parentNode.getClass().getName());
				}
				
				
				final int arity = ((PrologStructure) term).getArity();
				int index = 0;
				System.out.println("1. arity is " + ((PrologStructure) term).getArity());
				System.out.println("2. arity is " + arity);
//				System.out.println("second parameter is " + ((PrologStructure) term).getElement(1));
				while (index < arity) { // Go through each parameter in the functor.
					System.out.println("current parameter is " + ((PrologStructure) term).getElement(index));
					if (isPartOfClauseHead) {
						if (parentNode != null && parentNode.getNodeType() == Node.TYPE.MainArgument) { //Not a functor nested within a functor e.g. Increment(decrement(X, Result), Result)
							System.out.println("1.parentNode is " + parentNode.getClass().getName());
//							Node mainArgNode = retrieveNode("Arg", Node.TYPE.MainArgument, true);
							Node n = traverseClauseBody(((PrologStructure) term).getElement(index), null, isPartOfClauseHead);
//							mainArgNode.addFromNode(n);
							n.setMainArg(index + 1);
						}else{
							System.out.println("2.parentNode is null");
							Node n = traverseClauseBody(((PrologStructure) term).getElement(index), null, isPartOfClauseHead);
							fNode.addFromNode(n);
						}
					} else {
						System.out.println("functor that is not in the clause head.");
						Node n = traverseClauseBody(((PrologStructure) term).getElement(index), null, isPartOfClauseHead);
						
						if(((PrologStructure) term).getFunctor().getText().equals("not") && index == 0){
							n.setNodeName("!" + n.getNodeName());
						}else{
							fNode.addFromNode(n);
						}
						
					}

					index++;
					System.out.println("incremented index");
					System.out.println("index is now " + index + ", arity is " + arity);
				}
				
				return fNode;
			}
		} else if (term.getType() == PrologTermType.ATOM) { // Example: NY is Y + 1. 1 is an ATOM. Y is a VAR.
			System.out.println("---ATOM TERM---");
			System.out.println(term);
			return retrieveNode(term.getText(), Node.TYPE.Variable, false);

		} else if (term.getType() == PrologTermType.VAR) {
			System.out.println("---VAR TERM---");
			System.out.println(term);
			System.out.println(parentNode == null);
			System.out.println(isPartOfClauseHead);
			// return retrieveNode(term.getText(), Node.TYPE.Variable);

			if (term.getText().equals("_")) {
				System.out.println("_______________________________________________________________________________________________");
				return retrieveNode(term.getText(), Node.TYPE.Variable, true);
			} else {
				return retrieveNode(term.getText(), Node.TYPE.Variable, false);
			}

		} else if (term.getType() == PrologTermType.LIST) {
			System.out.println("---LIST TERM---");
			System.out.println(term);
			System.out.println(((PrologList) term).getArity());

			PrologList list = ((PrologList) term);
			Node listNode = retrieveNode(null, Node.TYPE.ListOperator, true);
		
			System.out.println("need to implement list arg");
			Node listHeadNode, listTailNode;
			
			if(list.getElement(0) != null){
				listHeadNode = retrieveNode(list.getElement(0).getText(), Node.TYPE.Variable, false);
				listNode.addFromNode(listHeadNode);
			}
			
			if(list.getElement(1) != null){
				listTailNode = retrieveNode(list.getElement(1).getText(), Node.TYPE.Variable, false);
				listNode.addFromNode(listTailNode);
			}
			
			return listNode;

		} else {
			System.out.println(term.getType());
			throw new Exception("cant go here");
		}

		return null;
	}

	
	// Factory method for creating Nodes: Returns an existing Node, otherwise a newly created one.
	private Node retrieveNode(String nodeName, Node.TYPE type, boolean isOverride) throws Exception {
		// if(nodeName.equals("") || nodeName == null){
		// throw new Exception("nodeName cant be empty or null");
		// }

		if (!isOverride) {
			for (Node n : this.nodes) {
				if (n.getNodeName().equals(nodeName)) {
					return n;
				}
			}
		}

		Node n;
		if (type == Node.TYPE.Variable) {
			n = new VariableNode(nodeName);
		} else if (type == Node.TYPE.Functor) {
			n = new FunctorNode(nodeName);
		} else if (type == Node.TYPE.ListOperator) {
			n = new ListOperatorNode("List" + this.listArgumentCount);
			this.listArgumentCount++;
		} else if (type == Node.TYPE.MainArgument) {
			n = new MainArgumentNode(nodeName + this.mainArgCount);
			this.mainArgCount++;
		} else if (type == Node.TYPE.Operator) {
			n = new OperatorNode(nodeName);
		} else {
			System.out.println(type);
			throw new Exception("can't go here in retrieveNode");
		}
		
		System.out.println("created a " + type + " node.");
		this.nodes.add(n);

		return n;
	}

	
	
	// Go through each file, build the structure of nodes and test the number of nodes expected to be correctly generated for each clause.
	private void regressionTestBuiltNodeCount(){
		try{
						
			for(int i = 0; i < this.files.length; i++){
				PrologCharDataSource source = new PrologCharDataSource(new FileReader(new File(new File("./prologfiles").getAbsolutePath() + files[i])));				
				final PrologParser parser = new PrologParser(null);
				AbstractPrologTerm sentence = parser.nextSentence(source);
				 
				this.nodes = new ArrayList<>();
				listArgumentCount = 1;
				mainArgCount = 1;
				
				int j = 0;
				while(sentence != null){
					System.out.println("Starting new sentence");
					traverseClauseBody(sentence, new MainArgumentNode(""), true);
					
					System.out.println(this.files[i]);
					System.out.println(sentence);
					for (Node n : this.nodes) {
						System.out.print(n.getNodeName() + "\t");
					}
					System.out.println();
					for (Node n : this.nodes) {
						System.out.print(n.getClass().toString() + "->" + n.getNodeName() + "\t");
					}
					System.out.println();
					System.out.println("expected " + correctNodeCountArray[i][j] + " nodes, getting " + this.nodes.size());
					assert this.nodes.size() == correctNodeCountArray[i][j];
					this.nodes = new ArrayList<>();
					listArgumentCount = 1;
					mainArgCount = 1;
					sentence = parser.nextSentence(source);
					j++;
				}
				
				System.out.println("------REGRESSION TESTED ON FILE " + this.files[i] + "------");
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
