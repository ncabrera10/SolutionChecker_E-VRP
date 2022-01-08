package dataStructures;

/**
 * The Class <code>AbstractObjectID</code> is the basis for all object with an
 * ID and a Name.
 * 
 * 
 */
public abstract class AbstractObjectID {

	/**
	 * The object ID.
	 */
	private final int mID;

	/**
	 * Builds a new <code>AbstractObjectID</code>.
	 * 
	 * @param iD
	 *            the id.
	 */
	public AbstractObjectID(int iD) {
		super();
		this.mID = iD;
	}

	/**
	 * Gets the object ID.
	 * 
	 * @return the object ID.
	 */
	public int getID() {
		return mID;
	}

}