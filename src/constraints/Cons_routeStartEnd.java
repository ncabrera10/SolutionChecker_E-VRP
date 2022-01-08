package constraints;

import java.io.PrintWriter;

import dataStructures.InstanceEVRPNLC;
import dataStructures.Route;
import dataStructures.Solution;

/**
 * This class allows to check that every route starts and ends at the depot.
 * @author nick0
 *
 */
public class Cons_routeStartEnd {
	
	public Cons_routeStartEnd() {
		
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
		for (Route route : solution.getRoutes()) {
			if (!checkConstraint(route, instance, output, precision,pw)) {
				return false;
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
	public boolean checkConstraint(Route route, InstanceEVRPNLC instance, boolean output, int precision,PrintWriter pw) {
		if (route.getFirst() != instance.getDepotID()) {
			if (output) {
				pw.println("Route " + route + " does not start at the depot");
			}
			return false;
		}
		if (route.getLast() != instance.getDepotID()) {
			if (output) {
				pw.println("Route " + route + " does not start at the depot");
			}
			return false;
		}
		return true;
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
		for (Route route : solution.getRoutes()) {
			feasible = feasible & checkConstraintDeep(route, instance, output, precision,pw);
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
	public boolean checkConstraintDeep(Route route, InstanceEVRPNLC instance, boolean output, int precision,PrintWriter pw) {
		return checkConstraint(route, instance, output, precision,pw);
	}

}
