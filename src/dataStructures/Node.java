package dataStructures;

/**
 * This class represents a node of the graph.
 */

public class Node {

	/**
	 * Node id
	 */
	private int id;
	
	/**
	 * Type of this node
	 */
	
	private int type;
	
	/**
	 * X coordinate
	 */
	private double cx;
	
	/**
	 * Y coordinate
	 */
	private double cy;
	
	/**
	 * Service time
	 */
	
	private double serv_time;
	
	/**
	 * Charging function type
	 */
	private String func_type;
	
	/**
	 * This method creates a new node
	 * @param i
	 * @param t
	 * @param x
	 * @param y
	 */
	public Node(int i,int t,double x,double y,String f_t) {
		
		// Initializes main values
		id = i;
		type = t;
		cx = x;
		cy = y;
		serv_time = 0.0;
		func_type = f_t;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the cx
	 */
	public double getCx() {
		return cx;
	}

	/**
	 * @param cx the cx to set
	 */
	public void setCx(double cx) {
		this.cx = cx;
	}

	/**
	 * @return the cy
	 */
	public double getCy() {
		return cy;
	}

	/**
	 * @param cy the cy to set
	 */
	public void setCy(double cy) {
		this.cy = cy;
	}

	/**
	 * @return the serv_time
	 */
	public double getServ_time() {
		return serv_time;
	}

	/**
	 * @param serv_time the serv_time to set
	 */
	public void setServ_time(double serv_time) {
		this.serv_time = serv_time;
	}

	/**
	 * @return the func_type
	 */
	public String getFunc_type() {
		return func_type;
	}

	/**
	 * @param func_type the func_type to set
	 */
	public void setFunc_type(String func_type) {
		this.func_type = func_type;
	}
	
	
}
