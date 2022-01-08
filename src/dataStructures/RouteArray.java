package dataStructures;

import java.util.ArrayList;
import java.util.List;

public class RouteArray implements Route {

	/**
	 * Route ID
	 */
	
	private int routeID;
	/**
	 * List with the nodes visited on the route
	 */
	private final ArrayList<Integer> mRoute;
	
	/**
	 * Waiting times at each node
	 */
	private final ArrayList<Double> mWaitingTimes;
	
	/**
	 * Charging amounts at each node
	 */
	private final ArrayList<Double> mChargingAmounts;
	
	/**
	 * Objective function for this route
	 */
	private double mOF;
	
	/**
	 * starting point of this route
	 */
	private double mStart = 0;

	/**
	 * Creates a new route and initializes the main parameters
	 */
	public RouteArray() {
		this.mRoute = new ArrayList<>();
		this.mWaitingTimes = new ArrayList<>();
		this.mChargingAmounts = new ArrayList<>();
		this.mOF = Double.MAX_VALUE;
	}

	@Override
	public int size() {
		return mRoute.size();
	}

	@Override
	public double getStartTime() {
		return mStart;
	}

	public void setStartTime(double start) {
		this.mStart = start;
	}

	@Override
	public int positionOf(int nodeID) {
		return mRoute.indexOf(nodeID);
	}

	@Override
	public int getAtPosition(int position) {
		return mRoute.get(position);
	}

	public void insert(int nodeID, double waitingTime, double chargingAmount) {
		mRoute.add(nodeID);
		mWaitingTimes.add(waitingTime);
		mChargingAmounts.add(chargingAmount);
	}

	public void insertAtPosition(int nodeID, double waitingTime, double chargingAmount, int position) {
		mRoute.add(position, nodeID);
		mWaitingTimes.add(position, waitingTime);
		mChargingAmounts.add(position, chargingAmount);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[\t");
		for (Integer i : this.mRoute)
			sb.append(i + "\t");
		sb.append("]\n");
		for (int i = 0; i < this.mRoute.size(); i++) {
			sb.append(mWaitingTimes.get(i) + "\t");
		}
		sb.append("]\n");
		for (int i = 0; i < this.mRoute.size(); i++) {
			sb.append(mChargingAmounts.get(i) + "\t");
		}
		sb.append("]");
		sb.append("\n Start time = " + mStart);
		sb.append("\n OF = " + mOF);
		return sb.toString();
	}

	/**
	 * To guarantee object encapsulation this method returns a hard copy of the list
	 * of nodes in the route. Therefore, the method runs in O(n), where n is the
	 * number of nodes in the route. If client classes do not really need a copy of
	 * the list, they can iterate through the route more efficiently using method
	 * {@link #get(int)} within a <code>for</code> loop.
	 */
	@Override
	public List<Integer> getRoute() {
		return new ArrayList<>(this.mRoute);
	}

	@Override
	public int getFirst() {
		return this.mRoute.get(0);
	}

	@Override
	public int getLast() {
		return this.mRoute.get(this.mRoute.size() - 1);
	}

	@Override
	public double getChargingAmount(int position) {
		return mChargingAmounts.get(position);
	}

	@Override
	public double getWaitingTime(int position) {
		return mWaitingTimes.get(position);
	}
	
	

	// public RouteI reverseRoute(double batteryCapacity, double[][]
	// energyConsumption) {
	// RouteI route = new RouteArray();
	// int nbNodes = this.size();
	// // System.out.println(this);
	// for (int k = nbNodes - 1; k >= 0; k--) {
	// if (getChargingAmount(k) > 0) {
	// int k2 = k - 1;
	// double energyNeeded = 0;
	// do {
	// energyNeeded += energyConsumption[getAtPosition(k2 +
	// 1)][getAtPosition(k2)];
	// k2--;
	// } while (k2 >= 0 && getChargingAmount(k2) == 0);
	// if (k2 > 0) {
	// energyNeeded += energyConsumption[getAtPosition(k2 +
	// 1)][getAtPosition(k2)];
	// }
	// double energyRemaining = 0;
	// int k1 = k + 1;
	// do {
	// energyRemaining += energyConsumption[getAtPosition(k1)][getAtPosition(k1
	// - 1)];
	// k1++;
	// } while (k1 < nbNodes && getChargingAmount(k1) == 0);
	// if (k1 < nbNodes) {
	// energyRemaining += energyConsumption[getAtPosition(k1)][getAtPosition(k1
	// - 1)];
	// }
	// if (k1 == nbNodes) {
	// energyRemaining = Math.max(0, batteryCapacity - energyRemaining);
	// // System.out.println("energyRemaining = " +
	// // energyRemaining);
	// // System.out.println("energyNeeded = " + energyNeeded);
	// energyNeeded = energyNeeded - energyRemaining;
	// }
	// route.insert(this.getAtPosition(k), 0, energyNeeded);
	// } else {
	// route.insert(this.getAtPosition(k), 0, 0);
	// }
	// }
	// return route;
	// }

	/**
	 * @return the routeID
	 */
	public int getRouteID() {
		return routeID;
	}

	/**
	 * @param routeID the routeID to set
	 */
	public void setRouteID(int routeID) {
		this.routeID = routeID;
	}

	@Override
	public String getKey() {
		StringBuilder s = new StringBuilder();
		int nbNodes = this.size();
		for (int k = 0; k < nbNodes - 1; k++) {
			s.append(mRoute.get(k));
			s.append("_");
		}
		s.append(mRoute.get(nbNodes - 1));
		return s.toString();
	}

	@Override
	public double getOF() {
		return mOF;
	}

	@Override
	public void setOF(double of) {
		this.mOF = of;
	}

}
