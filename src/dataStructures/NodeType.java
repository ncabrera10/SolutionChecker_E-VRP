package dataStructures;

public enum NodeType {
	DEPOT(0), CUSTOMER(1), CHARGING_STATION(2);

	private final int id;

	NodeType(final int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	/**
	 * Convert the impact type given by the <code>id</code> in an
	 * {@link NodeType}.
	 * 
	 * @param id
	 *            a integer.
	 * @return the node type.
	 */
	public static NodeType convert(int id) {
		switch (id) {
		case 0:
			return DEPOT;
		case 1:
			return CUSTOMER;
		case 2:
			return CHARGING_STATION;
		default:
			throw new IllegalArgumentException("Node type unknown -> " + id);
		}
	}
}
