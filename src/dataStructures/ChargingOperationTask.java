package dataStructures;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import utilities.Maths;

public class ChargingOperationTask extends ChargingOperation {

	private double est;
	private double lst;
	private double ect;
	private double lct;
	private final double mMargin;

	public ChargingOperationTask(Node node, double start, double end, double margin) {
		super(node, start, end);
		this.mMargin = margin;
		this.est = start;
		this.lst = start + margin;
		this.ect = end;
		this.lct = end + margin;
	}

	public void reset() {
		this.est = getStart();
		this.lst = getStart() + mMargin;
		this.ect = getEnd();
		this.lct = getEnd() + mMargin;
	}

	public double getLatestStart() {
		return lst;
	}

	public void setEarliestStart(double newEST) {
		est = newEST;
		ect = newEST + getDuration();
	}

	public void setLatestStart(double newLST) {
		lst = newLST;
		lct = newLST + getDuration();
	}

	public double getEarliestStart() {
		return est;
	}

	public double getEarliestEnd() {
		return ect;
	}

	public double getLatestEnd() {
		return lct;
	}

	@Override
	public String toString() {
		return super.toString() + " TW = [ " + est + "(" + lst + ");" + ect + "(" + lct + ")]";
	}

	public static List<ChargingOperationTask> getChargingOperationTasksInRoute(InstanceEVRPNLC instance, Route route,
			int precision) {
		double energy, duration, chargingAmount, chargingTime;
		List<Integer> nodesInRoute;
		double[][] timeMatrix = instance.getTimeMatrix();
		double[][] energyMatrix = instance.getEnergyMatrix();
		double[] processingTimes = instance.getProcessingTimes();
		int node1, node2;
		Node[] nodes = instance.getNodes();
		List<ChargingOperationTask> listOpInRoute = new ArrayList<>();
		energy = instance.getBatteryCapacity();
		nodesInRoute = route.getRoute();
		node1 = nodesInRoute.get(0);
		duration = route.getStartTime() + processingTimes[node1] + route.getWaitingTime(0);
		List<Double> opStart = new ArrayList<>();
		List<Double> opEnd = new ArrayList<>();
		List<Node> opNode = new ArrayList<>();

		if (nodes[node1].getType() == NodeType.CHARGING_STATION) {
			chargingAmount = route.getChargingAmount(0);
			// chargingAmount = Maths.floor(chargingAmount, precision);
			chargingTime = instance.getChargingTime(nodes[node1], energy, chargingAmount, precision);
			opStart.add(Maths.floor(duration, precision));
			opEnd.add(Maths.floor(duration + chargingTime, precision));
			opNode.add(nodes[node1]);
			duration += chargingTime;
			energy += chargingAmount;
		}
		for (int i = 0; i < nodesInRoute.size() - 1; i++) {
			node1 = nodesInRoute.get(i);
			node2 = nodesInRoute.get(i + 1);
			duration += (timeMatrix[node1][node2] + processingTimes[node2] + route.getWaitingTime(i + 1));
			energy -= energyMatrix[node1][node2];
			if (nodes[node2].getType() == NodeType.CHARGING_STATION) {
				chargingAmount = route.getChargingAmount(i + 1);
				// chargingAmount = Maths.floor(chargingAmount, precision);
				chargingTime = instance.getChargingTime(nodes[node2], energy, chargingAmount, precision);
				opStart.add(Maths.floor(duration, precision));
				opEnd.add(Maths.floor(duration + chargingTime, precision));
				opNode.add(nodes[node2]);
				duration += chargingTime;
				energy += chargingAmount;
			}
		}
		double margin = Math.max(0, instance.getTmax() - duration);
//		if (margin < -10 * GlobalParameters.DECIMAL_PRECISION) {
//			System.out.println(route);
//			System.out.println("duration = " + duration);
//			throw new IllegalStateException();
//		}
		int nbOp = opStart.size();
		for (int i = 0; i < nbOp; i++) {
			listOpInRoute.add(new ChargingOperationTask(opNode.get(i), opStart.get(i), opEnd.get(i), margin));
		}
		return listOpInRoute;
	}

	public static List<ChargingOperationTask> getChargingOperationTasksAtCS(InstanceEVRPNLC instance,
			Solution solution, int precision, Node node) {
		List<ChargingOperationTask> listAll = getAllChargingOperationTasks(instance, solution, precision);
		List<ChargingOperationTask> listCS = new ArrayList<>();
		for (ChargingOperationTask operation : listAll) {
			if (operation.getNode().getID() == node.getID()) {
				listCS.add(operation);
			}
		}
		return listCS;
	}

	public static HashMap<Node, List<ChargingOperationTask>> getChargingOperationTasksPerCS(InstanceEVRPNLC instance,
			Solution solution, int precision) {
		HashMap<Node, List<ChargingOperationTask>> map = new HashMap<>();
		Node[] csNodes = instance.getCSNodes();
		List<ChargingOperationTask> list;
		for (Node node : csNodes) {
			list = getChargingOperationTasksAtCS(instance, solution, precision, node);
			if (!list.isEmpty()) {
				map.put(node, list);
			}
		}
		return map;
	}

	public static HashMap<Node, List<ChargingOperationTask>> getChargingOperationTasksPerCS(InstanceEVRPNLC instance,
			List<Route> routes, int precision) {
		HashMap<Node, List<ChargingOperationTask>> map = new HashMap<>();
		Node[] csNodes = instance.getCSNodes();
		List<ChargingOperationTask> list;
		for (Node node : csNodes) {
			list = getChargingOperationTasksInRoute(instance, null, precision);
			if (!list.isEmpty()) {
				map.put(node, list);
			}
		}
		return map;
	}

	public static List<List<ChargingOperationTask>> getChargingOperationTasksPerRoute(InstanceEVRPNLC instance,
			Solution solution, int precision) {
		List<List<ChargingOperationTask>> list = new ArrayList<>();
		for (Route route : solution.getRoutes()) {
			list.add(getChargingOperationTasksInRoute(instance, route, precision));
		}
		return list;
	}

	public static List<ChargingOperationTask> getAllChargingOperationTasks(InstanceEVRPNLC instance, Solution solution,
			int precision) {
		return getAllChargingOperationTasks(instance, solution.getRoutes(), precision);
	}

	public static List<ChargingOperationTask> getAllChargingOperationTasks(InstanceEVRPNLC instance,
			List<Route> routes, int precision) {
		List<ChargingOperationTask> list = new ArrayList<>();
		for (Route route : routes) {
			list.addAll(getChargingOperationTasksInRoute(instance, route, precision));
		}
		return list;
	}

	public static boolean canOverlap(ChargingOperationTask op, ChargingOperationTask opR) {
		return op.getEarliestStart() <= opR.getLatestEnd() && opR.getEarliestStart() <= op.getLatestEnd();
	}

	public static Comparator<ChargingOperationTask> ChargingOperationTaskComparator = new Comparator<ChargingOperationTask>() {

		@Override
		public int compare(ChargingOperationTask o1, ChargingOperationTask o2) {
			int s = (int) Math.signum(o1.getLatestStart() - o2.getLatestStart());
			if (s == 0) {
				return (int) Math.signum(o1.getEarliestEnd() - o2.getEarliestEnd());
			} else {
				return s;
			}
		}

	};

}

