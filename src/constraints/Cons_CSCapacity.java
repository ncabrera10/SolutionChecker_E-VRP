package constraints;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import dataStructures.ChargingOperationTask;
import dataStructures.InstanceEVRPNLC;
import dataStructures.Route;
import dataStructures.Solution;
import utilities.Maths;
import dataStructures.Node;

/**
 * This class allows for checking the capacity constraints for each charging station.
 * @author nick0
 *
 */
public class Cons_CSCapacity {
	
	public Cons_CSCapacity() {
		
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
		HashMap<Node, List<ChargingOperationTask>> map = new HashMap<>();
		for (Node node : instance.getCSNodes()) {
			map.put(node, new ArrayList<ChargingOperationTask>());
		}
		List<ChargingOperationTask> list = ChargingOperationTask.getAllChargingOperationTasks(instance, solution,
				precision);
		for (ChargingOperationTask op : list) {
			map.get(op.getNode()).add(op);
			// System.out.println(op);
		}

		int nbEVs, k, nbOperations, capacity, l;
		LinkedList<Integer> opInExecution = new LinkedList<>();
		for (Entry<Node, List<ChargingOperationTask>> entry : map.entrySet()) {
			List<ChargingOperationTask> listOperations = entry.getValue();
			// Collections.sort(listOperations, (a, b) -> a.getStart() <
			// b.getStart() ? -1 : 0);
			Collections.sort(listOperations, ChargingOperationTask.ChargingOperationComparator);
			nbOperations = listOperations.size();
			capacity = instance.getCapacityCS(entry.getKey());
			//System.out.println(capacity);
			opInExecution.clear();
			opInExecution.add(0);
			k = 1;
			nbEVs = 1;
			Iterator<Integer> itOpInExecution;
			while (k < nbOperations) {
				itOpInExecution = opInExecution.iterator();
				while (itOpInExecution.hasNext()) {
					l = itOpInExecution.next();
					if (listOperations.get(k).getStart()
							- listOperations.get(l).getEnd() >= -Math.pow(10, -precision)) {
						itOpInExecution.remove();
						nbEVs--;
					}
				}
				opInExecution.add(k);
				nbEVs++;
				if (nbEVs > capacity) {
					if (output) {
						pw.println("Too many EVs charging at the same time at CS " + entry.getKey().getID()
								+ " // " + nbEVs + " > " + capacity);
						pw.println("Operations at CS " + entry.getKey().getID());
						for (Integer idx : opInExecution) {
							pw.println(listOperations.get(idx));
						}
						// List<ChargingOperation> list2 =
						// ChargingOperation.getAllChargingOperations(instance,
						// solution,
						// instance.getNbDecimals());
						// for (ChargingOperation op : list2) {
						// System.out.println(op);
						// }
					}
					return false;
				}
				k++;
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
		HashMap<Node, List<ChargingOperationTask>> map = new HashMap<>();
		for (Node node : instance.getCSNodes()) {
			map.put(node, new ArrayList<ChargingOperationTask>());
		}
		List<ChargingOperationTask> list = ChargingOperationTask.getAllChargingOperationTasks(instance, solution,
				precision);
		for (ChargingOperationTask op : list) {
			map.get(op.getNode()).add(op);
		}
		double decimalPrecision = Math.pow(10, -precision);
		int nbEVs, k, nbOperations, capacity, l;
		LinkedList<Integer> opInExecution = new LinkedList<>();
		for (Entry<Node, List<ChargingOperationTask>> entry : map.entrySet()) {
			k = 1;
			nbEVs = 1;
			List<ChargingOperationTask> listOperations = entry.getValue();
			Collections.sort(listOperations, ChargingOperationTask.ChargingOperationComparator);
			nbOperations = listOperations.size();
			capacity = instance.getCapacityCS(entry.getKey());
			opInExecution.clear();
			opInExecution.add(0);
			Iterator<Integer> itOpInExecution;
			while (k < nbOperations) {
				itOpInExecution = opInExecution.iterator();
				while (itOpInExecution.hasNext()) {
					l = itOpInExecution.next();
					if (Maths.floor(listOperations.get(k).getStart() - listOperations.get(l).getEnd(),
							precision) >= -decimalPrecision) {
						itOpInExecution.remove();
						nbEVs--;
					}
				}
				opInExecution.add(k);
				nbEVs++;
				if (nbEVs > capacity) {
					if (output) {
						pw.println("Too many EVs charging at the same time at CS " + entry.getKey().getID()
								+ " t " + nbEVs + " > " + capacity);
						System.out.println("Operations at CS " + entry.getKey().getID());
						for (Integer idx : opInExecution) {
							pw.println(listOperations.get(idx));
						}
						int k2 = opInExecution.get(opInExecution.size() - 2);
						pw.println((listOperations.get(k).getStart() - listOperations.get(k2).getEnd()) + " vs "
								+ decimalPrecision);
					}
					feasible = false;
				}
				k++;
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
