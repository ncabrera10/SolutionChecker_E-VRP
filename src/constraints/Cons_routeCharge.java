package constraints;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import dataStructures.InstanceEVRPNLC;
import dataStructures.Route;
import dataStructures.Solution;

/**
 * This class allows to check that during all the route the charge of the battery is > 0.
 *
 */
public class Cons_routeCharge {
	
	public Cons_routeCharge() {
		
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
		
		//Initializes some parameters:
		
			double energy;
			BigDecimal energyD;
			List<Integer> nodesInRoute;
			int node1, node2;
			double[][] energyMatrix = instance.getEnergyMatrix();
			energy = instance.getBatteryCapacity();
			energyD = new BigDecimal(instance.getBatteryCapacity(), new MathContext(instance.getNbDecimals()));
			nodesInRoute = route.getRoute();
			node1 = nodesInRoute.get(0);
			energy += route.getChargingAmount(0);
			energyD = energyD.add(new BigDecimal(route.getChargingAmount(0), new MathContext(instance.getNbDecimals())));
		
		//Check the first node:
			
			if (energyD.doubleValue() - Math.pow(10, -precision) > instance.getBatteryCapacity()) {
				if (output) {
					pw.println("Route " + route + " does not satisfy the charge " + energy + ">"
							+ instance.getBatteryCapacity() + " after at node " + node1);
					pw.println("energy = " + energyD.toEngineeringString());
				}
				return false;
			}
			
		//Check the complete route:
			
			for (int i = 0; i < nodesInRoute.size() - 1; i++) {
				node1 = nodesInRoute.get(i);
				node2 = nodesInRoute.get(i + 1);
				energy -= energyMatrix[node1][node2];
				energyD = energyD
						.subtract(new BigDecimal(energyMatrix[node1][node2], new MathContext(instance.getNbDecimals())));
				if (energyD.doubleValue() + Math.pow(10, -precision) < 0) {
	
					if (output) {
						pw.println(
								"Route " + route + " does not satisfy the charge constraints when arriving at node "
										+ nodesInRoute.get(i + 1) + " -> " + energyD.doubleValue() + " < 0 ");
						pw.println("energy = " + energyD.toEngineeringString());
					}
					return false;
				}
				energy += route.getChargingAmount(i + 1);
				energyD = energyD
						.add(new BigDecimal(route.getChargingAmount(i + 1), new MathContext(instance.getNbDecimals())));
				if (energyD.doubleValue() - Math.pow(10, -precision) > instance.getBatteryCapacity()) {
					if (output) {
						pw.println(
								"Route " + route + " does not satisfy the charge constraints after charging at node "
										+ nodesInRoute.get(i + 1) + " -> " + +energyD.doubleValue() + ">"
										+ instance.getBatteryCapacity());
						pw.println("energy = " + energyD.toEngineeringString());
					}
					return false;
				}
			}
			
		//Returns true if nothing happened:
			
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
		// System.out.println("----------Time constraints--------");
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
		
		//Initializes the main parameters:
		
			boolean feasible = true;
			BigDecimal energyD;
			List<Integer> nodesInRoute;
			int node1, node2;
			double[][] energyMatrix = instance.getEnergyMatrix();
			energyD = new BigDecimal(instance.getBatteryCapacity(), new MathContext(instance.getNbDecimals()));
			nodesInRoute = route.getRoute();
			node1 = nodesInRoute.get(0);
			energyD = energyD.add(new BigDecimal(route.getChargingAmount(0), new MathContext(instance.getNbDecimals())));
		
		//Check the first node:
			
			if (energyD.doubleValue() - Math.pow(10, -precision) > instance.getBatteryCapacity()) {
				if (output) {
					pw.println("Route " + route + " does not satisfy the charge " + energyD.doubleValue() + ">"
							+ instance.getBatteryCapacity() + " after at node " + node1);
					pw.println("energy = " + energyD.toEngineeringString());
				}
				feasible = false;
			}
			
		//Checks the complete route:
			
			for (int i = 0; i < nodesInRoute.size() - 1; i++) {
				node1 = nodesInRoute.get(i);
				node2 = nodesInRoute.get(i + 1);
				energyD = energyD
						.subtract(new BigDecimal(energyMatrix[node1][node2], new MathContext(instance.getNbDecimals())));
				if (energyD.doubleValue() + Math.pow(10, -precision) < 0) {
	
					if (output) {
						pw.println(
								"Route " + route + " does not satisfy the charge constraints when arriving at node "
										+ nodesInRoute.get(i + 1) + " -> " + energyD.doubleValue() + " < 0 " + " add = "
										+ Math.pow(10, -precision));
						pw.println("energy = " + energyD.toEngineeringString());
					}
					feasible = false;
				}
				energyD = energyD
						.add(new BigDecimal(route.getChargingAmount(i + 1), new MathContext(instance.getNbDecimals())));
				if (energyD.doubleValue() - Math.pow(10, -precision) > instance.getBatteryCapacity()) {
					if (output) {
						pw.println(
								"Route " + route + " does not satisfy the charge constraints after charging at node "
										+ nodesInRoute.get(i + 1) + " -> " + energyD.doubleValue() + ">"
										+ instance.getBatteryCapacity());
						pw.println("energy = " + energyD.toEngineeringString());
					}
					feasible = false;
				}
			}
			
		//Return true if nothing happened:
			
			return feasible;
	}

}
