package constraints;

import java.io.PrintWriter;
import java.util.List;

import dataStructures.InstanceEVRPNLC;
import dataStructures.Node;
import dataStructures.NodeType;
import dataStructures.Route;
import dataStructures.Solution;

/**
 * This class allows to check the total route duration.
 *
 */
public class Cons_routeDuration {
	
	public Cons_routeDuration() {
		
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
		
			double duration, energy, chargingTime, chargingAmount;
			List<Integer> nodesInRoute;
			double[][] timeMatrix = instance.getTimeMatrix();
			double[][] energyMatrix = instance.getEnergyMatrix();
			double[] processingTimes = instance.getProcessingTimes();
			int node1, node2;
			Node[] nodes = instance.getNodes();
			energy = instance.getBatteryCapacity();
			nodesInRoute = route.getRoute();
			node1 = nodesInRoute.get(0);
			duration = processingTimes[node1] + route.getWaitingTime(0);
			
		//Check the first node:
			
			if (nodes[node1].getType() == NodeType.CHARGING_STATION) {
				chargingAmount = route.getChargingAmount(0);
				chargingTime = instance.getChargingTime(nodes[node1], energy, chargingAmount, precision);
				duration += chargingTime;
				energy += chargingAmount;
				if (duration - Math.pow(10, -precision) > instance.getTmax()) {
					if (output) {
						pw.println("Route " + route + " does not satisfy the maximum-duration limit " + duration
								+ " > " + instance.getTmax());
					}
					return false;
				}
			}
			
		//Check the remaining nodes:
			
			for (int i = 0; i < nodesInRoute.size() - 1; i++) {
				node1 = nodesInRoute.get(i);
				node2 = nodesInRoute.get(i + 1);
				duration += (timeMatrix[node1][node2] + processingTimes[node2] + route.getWaitingTime(i + 1));
				energy -= energyMatrix[node1][node2];
				if (nodes[node2].getType() == NodeType.CHARGING_STATION) {
					chargingAmount = route.getChargingAmount(i + 1);
					chargingTime = instance.getChargingTime(nodes[node2], energy, chargingAmount, precision);
					duration += chargingTime;
					energy += chargingAmount;
				}
				if (duration - Math.pow(10, -precision) > instance.getTmax()) {
					if (output) {
						pw.println("Route " + route + " does not satisfy the maximum-duration limit " + duration
								+ " > " + instance.getTmax());
					}
					return false;
				}
			}
		
		//If nothing happened:
			
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

		//Initializes the main parameters:
		
			boolean feasible = true;
			double duration, energy, chargingTime, chargingAmount;
			List<Integer> nodesInRoute;
			double[][] timeMatrix = instance.getTimeMatrix();
			double[][] energyMatrix = instance.getEnergyMatrix();
			double[] processingTimes = instance.getProcessingTimes();
			int node1, node2;
			Node[] nodes = instance.getNodes();
	
			energy = instance.getBatteryCapacity();
			nodesInRoute = route.getRoute();
			node1 = nodesInRoute.get(0);
			duration = processingTimes[node1] + route.getWaitingTime(0);
			
		//Checks if it is a chaging station:
			
			if (nodes[node1].getType() == NodeType.CHARGING_STATION) {
				chargingAmount = route.getChargingAmount(0);
				chargingTime = instance.getChargingTime(nodes[node1], energy, chargingAmount, precision);
				duration += chargingTime;
				energy += chargingAmount;
			}
			
		//Iterates through the complete route:
			
			for (int i = 0; i < nodesInRoute.size() - 1; i++) {
				node1 = nodesInRoute.get(i);
				node2 = nodesInRoute.get(i + 1);
				duration += (timeMatrix[node1][node2] + processingTimes[node2] + route.getWaitingTime(i + 1));
				energy -= energyMatrix[node1][node2];
				if (nodes[node2].getType() == NodeType.CHARGING_STATION) {
					chargingAmount = route.getChargingAmount(i + 1);
					chargingTime = instance.getChargingTime(nodes[node2], energy, chargingAmount, precision);
					duration += chargingTime;
					energy += chargingAmount;
				}
			}
			
		//If the duration does not satisfy the precision required:
			
			if (duration - Math.pow(10, -precision) > instance.getTmax()) {
				if (output) {
					pw.println("Route " + route + " does not satisfy the maximum-duration limit " + duration + " > "
							+ instance.getTmax());
				}
				feasible = false;
			}

		//Returns if it is feasible:
			
		return feasible;
	}

}
