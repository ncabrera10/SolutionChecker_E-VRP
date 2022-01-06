package dataStructures;

import java.util.Hashtable;

/**
 * This class represents the graph
 *
 */
public class Graph {

	/**
	 * Number of nodes
	 */
	
	private int numNodes;
	
	/**
	 * Graph nodes
	 */
	private Hashtable<Integer,Node> nodes;
	
	/**
	 * Euclidian, etc..
	 */
	private String typeOfDistance;
	
	/**
	 * Number of decimals for all the calculations
	 */
	private int numberOfDecimals;
	
	/**
	 * This methods creates an empty graph
	 */
	public Graph() {
		
		//Initializes the main attributes
		nodes = new Hashtable<Integer,Node>();
		
	}
	
	/**
	 * This method adds a new node to the graph
	 * @param id
	 * @param type
	 * @param cx
	 * @param cy
	 * @param func_type type of charging function (if type = 2)
	 */
	public void addNode(int id, int type, double cx, double cy,String func_type) {
		
		// Adds a node to the graph.
		
			nodes.put(id,new Node(id,type,cx,cy,func_type));
		
		// Updates the count:
			
			numNodes++;
	}

	
	public void setParamAdd(String t,int num) {
		
		typeOfDistance = t;
		numberOfDecimals = num;
	}
	
	/**
	 * @return the numNodes
	 */
	public int getNumNodes() {
		return numNodes;
	}

	/**
	 * @param numNodes the numNodes to set
	 */
	public void setNumNodes(int numNodes) {
		this.numNodes = numNodes;
	}

	/**
	 * @return the nodes
	 */
	public Hashtable<Integer,Node> getNodes() {
		return nodes;
	}

	/**
	 * @param nodes the nodes to set
	 */
	public void setNodes(Hashtable<Integer,Node> nodes) {
		this.nodes = nodes;
	}

	/**
	 * @return the typeOfDistance
	 */
	public String getTypeOfDistance() {
		return typeOfDistance;
	}

	/**
	 * @param typeOfDistance the typeOfDistance to set
	 */
	public void setTypeOfDistance(String typeOfDistance) {
		this.typeOfDistance = typeOfDistance;
	}

	/**
	 * @return the numberOfDecimals
	 */
	public int getNumberOfDecimals() {
		return numberOfDecimals;
	}

	/**
	 * @param numberOfDecimals the numberOfDecimals to set
	 */
	public void setNumberOfDecimals(int numberOfDecimals) {
		this.numberOfDecimals = numberOfDecimals;
	}
	
	
}
