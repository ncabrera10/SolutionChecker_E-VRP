package constraints;

import java.io.PrintWriter;
import java.util.HashMap;

import dataStructures.InstanceEVRPNLC;
import dataStructures.Node;
import dataStructures.Route;
import dataStructures.Solution;

/**
 * This class allows to check that all customers are visited just one time.
 *
 */
public class Cons_CustomersVisited {
	
	public Cons_CustomersVisited() {
		
	}
	
	/**
	 * This method checks the constraint for all routes:
	 * @param solution
	 * @param instance
	 * @param output
	 * @param precision
	 * @return
	 */
	public boolean checkConstraint(Solution solution, InstanceEVRPNLC instance, boolean output, int precision,PrintWriter pw) {
		int nbVisits;
		HashMap<Integer, Integer> mapCustNbVisits = new HashMap<>();
		int nodeID, sizeRoute;
		Integer key;
		for (Route route : solution.getRoutes()) {
			sizeRoute = route.size();
			for (int i = 0; i < sizeRoute; i++) {
				nodeID = route.getAtPosition(i);
				key = mapCustNbVisits.get(nodeID);
				if (key == null) {
					mapCustNbVisits.put(nodeID, 1);
				} else {
					mapCustNbVisits.put(nodeID, key.intValue() + 1);
				}
			}
		}
		for (Node node : instance.getCustomerNodes()) {
			nodeID = node.getID();
			key = mapCustNbVisits.get(nodeID);
			if (key == null) {
				if (output) {
					pw.println("Node " + node.getID() + " is not visited in the solution");
				}
				return false;
			} else {
				nbVisits = key.intValue();
				if (nbVisits > 1) {
					if (output) {
						pw.println("Node " + node.getID() + " is visited " + nbVisits + " times");
					}
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * This method checks a route
	 * @param route
	 * @param instance
	 * @param output
	 * @param precision
	 * @return
	 */
	public boolean checkConstraint(Route route, InstanceEVRPNLC instance, boolean output, int precision) {
		throw new IllegalStateException("This constraint cannot be called for a route");
	}

	/**
	 * This methods checks all routes but goes a little bit deeper:
	 * @param solution
	 * @param instance
	 * @param output
	 * @param precision
	 * @return
	 */
	public boolean checkConstraintDeep(Solution solution, InstanceEVRPNLC instance, boolean output, int precision,PrintWriter pw) {
		boolean feasible = true;
		int nbVisits;
		HashMap<Integer, Integer> mapCustNbVisits = new HashMap<>();
		int nodeID, sizeRoute;
		Integer key;
		for (Route route : solution.getRoutes()) {
			sizeRoute = route.size();
			for (int i = 0; i < sizeRoute; i++) {
				nodeID = route.getAtPosition(i);
				key = mapCustNbVisits.get(nodeID);
				if (key == null) {
					mapCustNbVisits.put(nodeID, 1);
				} else {
					mapCustNbVisits.put(nodeID, key.intValue() + 1);
				}
			}
		}
		for (Node node : instance.getCustomerNodes()) {
			nodeID = node.getID();
			key = mapCustNbVisits.get(nodeID);
			if (key == null) {
				if (output) {
					pw.println("Node " + node.getID() + " is not visited in the solution");
				}
				feasible = false;
			} else {
				nbVisits = key.intValue();
				if (nbVisits > 1) {
					if (output) {
						pw.println("Node " + node.getID() + " is visited " + nbVisits + " times");
					}
					feasible = false;
				}
			}
		}
		return feasible;
	}

	/**
	 * This method checks a route more in deep
	 * @param route
	 * @param instance
	 * @param output
	 * @param precision
	 * @return
	 */
	public boolean checkConstraintDeep(Route route, InstanceEVRPNLC instance, boolean output, int precision) {
		throw new IllegalStateException("This constraint cannot be called for a route");
	}

}
