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
import Visual.Visualiser;

public class ParserProcess {

	private List<Node> nodes = new ArrayList<>();
	private List<List<Node>> predicateClauses = new ArrayList<>();

	public int[] prologProgramFileClauseNodeCount = new int[] { 5, 17, 8, 6, 6, 6, 9, 9, 9, 10, 10 };
	public int[] siftFileClauseNodeCount = new int[] { 6, 11, 10 };
	// public int[] queensFileClauseNodeCount = new int[]{6, 11, 10};
	// public int[] routeFileClauseNodeCount = new int[]{6, 11, 10};

	public int[][] correctNodeCountArray = new int[][] { prologProgramFileClauseNodeCount, siftFileClauseNodeCount };

	private int listArgumentCount = 1;
	private int listOperatorNodeCount = 1;
	private int mainArgCount = 1;
	private boolean isOuterMostClause = true;
	private String prologProgramFilePath = "/prologProgram.pl";
	private String filePath = "/sift.pl";

	public String[] files = new String[] { prologProgramFilePath, filePath };

	public ParserProcess() {
		PrologCharDataSource source;

		try {
			source = new PrologCharDataSource(new FileReader(new File(new File("./prologfiles").getPath() + filePath)));

			final PrologParser parser = new PrologParser(null);
			AbstractPrologTerm sentence = parser.nextSentence(source);


			while (sentence != null) {
				traverseClauseBody(sentence, new MainArgumentNode(""), true);
				predicateClauses.add(this.nodes);
				this.nodes = new ArrayList<>();
				listArgumentCount = 1;
				mainArgCount = 1;
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

			
			regressionTestBuiltNodeCount();
			Visualiser vis = new Visualiser();
			vis.visualise(this.predicateClauses);

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	// Recursively traverse clauses in the body of a current clause.
	// parentNode used for situations like NY is Y + 1, in this case parentNode
	// is NY.
	private Node traverseClauseBody(AbstractPrologTerm term, Node parentNode, boolean isPartOfClauseHead)
			throws Exception {
		System.out.println("in traverseClauseBody");
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

					// if(parentNode==null){
					// parentNode = new
					// ContainerNode(((PrologStructure)((PrologStructure)
					// term).getElement(0)).getFunctor().getText());
					// }

					System.out.println(((PrologStructure) term).getElement(0));
					System.out.println(((PrologStructure) term).getElement(1));

					traverseClauseBody(((PrologStructure) term).getElement(0), parentNode, false);
					traverseClauseBody(((PrologStructure) term).getElement(1), parentNode, false);

				} else if ((((PrologStructure) term).getFunctor()).getText().equals("is")) { // Example:
																								// NY
																								// is
																								// Y
																								// +
																								// 1
					System.out.println("------is OP------");
					System.out.println(((Operator) ((PrologStructure) term).getFunctor()).getOperatorType()); // ==
																												// PrologTermType.XFX
					System.out.println(((PrologStructure) term).getElement(0)); // ==
																				// VariableNode
																				// i.e.
																				// NY.
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
					if (((PrologStructure) term).getFunctor().getText().equals(":-")) {
						System.out.println("------ :- OP------");
						System.out.println(((PrologStructure) term).getElement(0)); // ==
																					// VariableNode
																					// i.e.
																					// NY.
						System.out.println(((PrologStructure) term).getElement(1));
						// System.out.println(((PrologStructure)((PrologStructure)term).getElement(1)).getFunctor().getText());
						// // ==
						traverseClauseBody((((PrologStructure) term).getElement(0)), new MainArgumentNode(""), true);
						traverseClauseBody((((PrologStructure) term).getElement(1)), null, false);
					} else {
						System.out.println("------equality OP------");
						System.out.println(((PrologStructure) term).getFunctor().getType());
						String op = ((PrologStructure) term).getFunctor().getText().trim();
						if (op.equals(">") || op.equals("<") || op.equals(">=") || op.equals("=<") || op.equals("==")
								|| op.equals("\\=")) {
							Node leftNode = retrieveNode((((PrologStructure) term).getElement(0).getText()),
									Node.TYPE.Variable, false);
							Node rightNode = traverseClauseBody(((PrologStructure) term).getElement(1), null, false);
							Node equalityNode = retrieveNode(((PrologStructure) term).getFunctor().getText(),
									Node.TYPE.Operator, true);

							return equalityNode;
						} else {
							System.out.println(((PrologStructure) term).getFunctor().getText());
							throw new Exception("an XFX operator not handled");
						}
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
			} else { // Else: the last clause (no more comma following).
				System.out.println("---LAST CLAUSE IN BODY---");
				System.out.println(((PrologStructure) term).getFunctor().getType());
				System.out.println("predicate name is " + ((PrologStructure) term).getFunctor().getText());
				System.out.println(((PrologStructure) term).getElement(0));
				System.out.println(((PrologStructure) term).getElement(1));
				System.out.println(((PrologStructure) term).getArity());
				System.out.println("^^^^^Fly " + ((PrologStructure) term).getElement(1).getText());

				if (parentNode == null) {
					Node fNode = retrieveNode(((PrologStructure) term).getFunctor().getText(), Node.TYPE.Functor, true);
					parentNode = fNode;
					System.out.println(
							"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
				}

				// if(isMainArg && parentNode==null){
				// System.out.println("1.made new MainArgNode in LAST CLAUSE");
				// Node mainArgNode = retrieveNode("MainArg",
				// Node.TYPE.MainArgument, true);
				// }

				int arity = ((PrologStructure) term).getArity();
				int index = 0;
				while (index < arity) {
					if (isPartOfClauseHead) {

						if (parentNode != null && parentNode.getNodeType() == Node.TYPE.MainArgument) {
							System.out.println("calling with " + parentNode + " " + isPartOfClauseHead);
							Node mainArgNode = retrieveNode("MainArg", Node.TYPE.MainArgument, true);
							traverseClauseBody(((PrologStructure) term).getElement(index), null, isPartOfClauseHead);
						} else {
							traverseClauseBody(((PrologStructure) term).getElement(index), null, isPartOfClauseHead);
						}
					} else {
						traverseClauseBody(((PrologStructure) term).getElement(index), parentNode, isPartOfClauseHead);
					}

					index++;
				}
			}
		} else if (term.getType() == PrologTermType.ATOM) { // Example: NY is Y
															// + 1. 1 is an
															// ATOM. Y is a VAR.
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
				return retrieveNode(term.getText(), Node.TYPE.Variable, true);
			} else {
				return retrieveNode(term.getText(), Node.TYPE.Variable, false);
			}

		} else if (term.getType() == PrologTermType.LIST) {
			System.out.println("---LIST TERM---");
			System.out.println(term);
			System.out.println(((PrologList) term).getArity());

			if (((PrologList) term).getElement(0) != null || ((PrologList) term).getElement(1) != null) { // Empty
																											// list
				// throw new Exception("need to implement List with arguments");
				System.out.println("need to implement list arg");
				System.out.println(((PrologList) term).getElement(0).getText());
				Node listHeadNode = retrieveNode(((PrologList) term).getElement(0).getText(), Node.TYPE.Variable,
						false);
				Node listTailNode = retrieveNode(((PrologList) term).getElement(1).getText(), Node.TYPE.Variable,
						false);

				if (isPartOfClauseHead) {

					Node listNode = retrieveNode(null, Node.TYPE.ListOperator, true);

				} else {
					Node listNode = retrieveNode(null, Node.TYPE.ListOperator, true);
				}

			} else {
				if (isPartOfClauseHead) {
					Node listNode = retrieveNode(null, Node.TYPE.ListOperator, true);

				} else {
					Node listNode = retrieveNode(null, Node.TYPE.ListOperator, true);
					return listNode;
				}

			}

		} else {
			System.out.println(term.getType());
			throw new Exception("cant go here");
		}

		return null;
	}

	// Returns an existing VariableNode, otherwise a newly created one.
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
			System.out.println("Created new FunctorNode");
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

		this.nodes.add(n);

		return n;
	}

	
	
	private void regressionTestBuiltNodeCount(){
		try{
						
			for(int i = 0; i < this.files.length; i++){
				PrologCharDataSource source = new PrologCharDataSource(new FileReader(new File(new File("./prologfiles").getAbsolutePath() + files[i])));				
				final PrologParser parser = new PrologParser(null);
				AbstractPrologTerm sentence = parser.nextSentence(source);
				 
				int j = 0;
				while(sentence != null){
					traverseClauseBody(sentence, new MainArgumentNode(""), true);

					assert this.nodes.size() == correctNodeCountArray[i][j];
					this.nodes = new ArrayList<>();
					listArgumentCount = 1;
					mainArgCount = 1;
					sentence = parser.nextSentence(source);
					j++;
				}
				
				System.out.println("------REGRESSION TESTED ON FILE" + this.files[i] + "------");
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
