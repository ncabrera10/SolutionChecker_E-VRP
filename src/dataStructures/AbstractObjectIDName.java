package dataStructures;

/**
 * The Class <code>AbstractObjectID</code> is the basis for all object with an
 * ID and a Name.
 * 
 * 
 */
public abstract class AbstractObjectIDName extends AbstractObjectID {

	/**
	 * The object name;
	 */
	private final String mName;

	/**
	 * Builds a new <code>AbstractObjectIDName</code>.
	 * 
	 * @param id
	 *            the id.
	 * @param originalID
	 *            the original id.
	 */
	public AbstractObjectIDName(int id, String name) {
		super(id);
		this.mName = name;
	}

	/**
	 * Gets the equipment name.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return mName;
	}

}
