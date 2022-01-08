package dataStructures;

/**
 * Represents the locations of nodes in a vehicle routing problem. Using this
 * class is possible to represent customers, CS, and the depot.
 */
public class Node extends AbstractObjectIDName {

	/**
	 * Coordinate x of the node.
	 */
	private final NodeType mType;

	/**
	 * Constructor
	 */
	public Node(int id, String name, NodeType type) {
		super(id, name);
		this.mType = type;
	}

	public NodeType getType() {
		return mType;
	}

	@Override
	public String toString() {
		return "(" + this.getID() + ";" + this.getType() + ")";
	}
}
