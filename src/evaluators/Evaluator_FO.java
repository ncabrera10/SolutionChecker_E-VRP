package evaluators;

import java.util.List;

import dataStructures.InstanceEVRPNLC;
import dataStructures.Node;
import dataStructures.NodeType;
import dataStructures.Route;
import dataStructures.Solution;
import utilities.Maths;

public class Evaluator_FO {

	public Evaluator_FO() {
		
		
	}
	
	/**
	 * This method evaluates a solution and returns the objective function
	 * @param solution
	 * @param instance
	 * @param precision
	 * @return
	 */
	public double evaluateObjectiveFunctionSolution(Solution solution, InstanceEVRPNLC instance, int precision) {
		
		//Evaluate if the solution exists:
		
			if (solution == null) {
				return Double.MAX_VALUE;
			}
			
		//Create some variables for the obj value, duration..
			
			double objectiveValue = 0, duration, energy, chargingAmount, chargingTime, waitingTime;
			List<Integer> nodesInRoute;
			double[][] timeMatrix = instance.getTimeMatrix();
			double[][] energyMatrix = instance.getEnergyMatrix();
			double[] processingTimes = instance.getProcessingTimes();
			int node1, node2;
			Node[] nodes = instance.getNodes();
			double serviceTimeT = 0;
			
		// For each route:
			
			for (Route route : solution.getRoutes()) {
				//System.out.println(route);
				energy = instance.getBatteryCapacity();
				nodesInRoute = route.getRoute();
				node1 = nodesInRoute.get(0);
				duration = 0;
				duration += processingTimes[node1];
				serviceTimeT += processingTimes[node1];
				if (nodes[node1].getType() == NodeType.CHARGING_STATION) {
					chargingAmount = route.getChargingAmount(0);
					chargingTime = instance.getChargingTime(nodes[node1], energy, chargingAmount, precision);
					waitingTime = +route.getWaitingTime(0);
					duration += (waitingTime + chargingTime);
					energy += chargingAmount;
					// System.out.println(chargingAmount + " - " + chargingTime + "(WAIT = " + waitingTime + ")");
					 
				}
				//System.out.println(node1 + " -> " + duration);
				for (int i = 0; i < nodesInRoute.size() - 1; i++) {
					node1 = nodesInRoute.get(i);
					node2 = nodesInRoute.get(i + 1);
					duration += (timeMatrix[node1][node2] + processingTimes[node2]);
					serviceTimeT += processingTimes[node2];
					energy -= energyMatrix[node1][node2];
					if (nodes[node2].getType() == NodeType.CHARGING_STATION) {
						chargingAmount = route.getChargingAmount(i + 1);
						chargingTime = instance.getChargingTime(nodes[node2], energy, chargingAmount, precision);
						waitingTime = route.getWaitingTime(i + 1);
						duration += (waitingTime + chargingTime);
						energy += chargingAmount;
						 //System.out.println(chargingAmount + " - " + chargingTime+ "(WAIT = " + waitingTime + ")");
						// + " (WAIT = " + waitingTime + ")");
					}
					 //System.out.println(node2 + " -> " + duration+"("+timeMatrix[node1][node2]+")");
				}
				objectiveValue += duration;
			}
			
		// Calculate the objective value:
			
			objectiveValue = Maths.floor(objectiveValue, instance.getNbDecimals());
		
		// Return the objective function value:
			
			return objectiveValue-serviceTimeT;
	}
	
	
	/**
	 * Evaluates the objective function of a route.
	 * 
	 * @param solution
	 *            a solution.
	 * @param instance
	 * @return the evaluation of the given <code>solution</code>.
	 */
	
	public double evaluateObjectiveFunctionRoute(Route route, InstanceEVRPNLC instance, int precision) {
		
		//Check if the route exists:
		
			if (route == null) {
				return -1;
			}
			
		//Initializes some important values:
			
			double objectiveValue = 0, duration, energy, chargingAmount, chargingTime, waitingTime;
			List<Integer> nodesInRoute;
			double[][] timeMatrix = instance.getTimeMatrix();
			double[][] energyMatrix = instance.getEnergyMatrix();
			double[] processingTimes = instance.getProcessingTimes();
			int node1, node2;
			Node[] nodes = instance.getNodes();
			double serviceTimeT = 0;
			
		//Recovers key aspects: nodes in route, battery capacity:
			
			energy = instance.getBatteryCapacity();
			nodesInRoute = route.getRoute();
			node1 = nodesInRoute.get(0);
			duration = 0;
			duration += processingTimes[node1];
			serviceTimeT += processingTimes[node1];
			
		// If the node is a charging station:
			
			if (nodes[node1].getType() == NodeType.CHARGING_STATION) {
				chargingAmount = route.getChargingAmount(0);
				chargingTime = instance.getChargingTime(nodes[node1], energy, chargingAmount, precision);
				// System.out.println("Evaluation (chargingTime) = " +
				// chargingTime);
				// System.out.println(energy + " -> " + (energy + chargingAmount));
				waitingTime = +route.getWaitingTime(0);
				duration += (waitingTime + chargingTime);
				energy += chargingAmount;
				// System.out.println(chargingAmount + " - " + chargingTime + "
				// (WAIT = " + waitingTime + ")");
			}
			
		// System.out.println(node1.getID() + " -> " + duration);
			
		// Iterates through the complete route:
			
			for (int i = 0; i < nodesInRoute.size() - 1; i++) {
				node1 = nodesInRoute.get(i);
				node2 = nodesInRoute.get(i + 1);
				duration += (timeMatrix[node1][node2] + processingTimes[node2]);
				energy -= energyMatrix[node1][node2];
				serviceTimeT += processingTimes[node2];
				if (nodes[node2].getType() == NodeType.CHARGING_STATION) {
					chargingAmount = route.getChargingAmount(i + 1);
					chargingTime = instance.getChargingTime(nodes[node2], energy, chargingAmount, precision);
					// System.out.println("Evaluation (chargingTime) = " +
					// chargingTime);
					// System.out.println(energy + " -> " + (energy +
					// chargingAmount));
					waitingTime = route.getWaitingTime(i + 1);
					duration += (waitingTime + chargingTime);
					energy += chargingAmount;
					// System.out.println(chargingAmount + " - " + chargingTime
					// + " (WAIT = " + waitingTime + ")");
				}
				// System.out.println(node2.getID() + " -> " + duration);
			}
			
		//Computes the final objective function:
			
			objectiveValue += duration;
			objectiveValue = Maths.floor(objectiveValue, instance.getNbDecimals());
			
		//Returns the objective function:
			
			return objectiveValue-serviceTimeT;
	}

	/**
	 * Evaluates the objective function of the given route.
	 * 
	 * @param solution
	 *            a solution.
	 * @param instance
	 * @return the evaluation of the given <code>solution</code>.
	 */

	public double evaluateRouting(Route route, InstanceEVRPNLC instance, int precision) {
		
		//Check if the route exists:
		
			if (route == null) {
				return -1;
			}
			
		//Initializes:
			
			double objectiveValue = 0, duration;
			List<Integer> nodesInRoute;
			double[][] timeMatrix = instance.getTimeMatrix();
			double[] processingTimes = instance.getProcessingTimes();
			int node1, node2;
			nodesInRoute = route.getRoute();
			node1 = nodesInRoute.get(0);
			double serviceTimeT = 0;
			duration = 0;
			duration += processingTimes[node1];
			serviceTimeT += processingTimes[node1];
			
		// Calculates the duration:
			
			for (int i = 0; i < nodesInRoute.size() - 1; i++) {
				node1 = nodesInRoute.get(i);
				node2 = nodesInRoute.get(i + 1);
				duration += (timeMatrix[node1][node2] + processingTimes[node2]);
				serviceTimeT += processingTimes[node1];
			}
			
		//Objective function:
			
			objectiveValue += duration;
			objectiveValue = Maths.floor(objectiveValue, instance.getNbDecimals());
			
		//Returns the objective function:
			
			return objectiveValue-serviceTimeT;
	}
}
