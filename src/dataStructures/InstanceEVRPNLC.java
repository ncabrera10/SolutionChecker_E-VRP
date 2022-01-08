package dataStructures;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import globalParameters.GlobalParameters;
import utilities.EuclideanCalculator;
import utilities.Maths;

public class InstanceEVRPNLC{

	private int capacityID;
	
	private final int mNbDecimals;
	/**
	 * name of instance
	 */
	private final String mName;
	/**
	 * Number of nodes of the problems
	 */
	private final int mNbNodes;
	/**
	 * Number of customers
	 */
	private final int mNbCustomers;
	/**
	 * Number of charging stations (CSs)
	 */
	private final int mNbCSs;
	/**
	 * Number of CS types
	 */
	private final int mNbCSType;
	/**
	 * Capacity tank of the vehicle
	 */
	private final double mBatteryCapacity;
	/**
	 * rate consume of the vehicle
	 */
	private final double mConsumptionRate;
	/**
	 * Maximal time of the charge
	 */
	private final double mSmax;
	/**
	 * Maximal time of the route
	 */
	private final double mTmax;
	/**
	 * Velocity of the vehicle
	 */
	private final double mSpeedFactor;
	/**
	 * Node types
	 */
	private final NodeType[] mTypeNodes;
	/**
	 * Nodes
	 */
	private final Node[] mNodes;
	/**
	 * Node ID associate with the depot
	 */
	private final int mDepotID;
	/**
	 * Node IDs of the customers
	 */
	private final int[] mCustomerIDs;
	/**
	 * Node IDs of the CSs
	 */
	private final int[] mCSIDs;
	/**
	 * Processing times at each node
	 */
	private final double[] mProcessingTimes;
	/**
	 * CS type name
	 */
	private final String[] mCSTypeName;
	/**
	 * /** CS type of each CS
	 */
	private final int[] mCSTypes;
	/**
	 * Number of chargers at each CS
	 */
	private final int[] mCapacityCSs;
	/**
	 * Number of breakpoints used to model the charging function for each CS
	 * type
	 */
	private final int[] mNbBreakPoints;
	/**
	 * Breakpoint used to model the charging function for each CS type
	 */
	private final double[][][] mPiecewisePoints;
	/**
	 * Slope associated with each segment used to model the charging function
	 * for each CS type
	 */
	private final double[][] mSlope;
	/**
	 * YIntercept associated with each segment used to model the charging
	 * function for each CS type
	 */
	private final double[][] mYIntercept;
	/**
	 * Array with the coordinates of each node
	 */
	private final double[][] mCoordinates;
	/**
	 * Array with the distance between each couple of nodes
	 */
	private final double[][] mDistanceMatrix;
	/**
	 * Array with the energy consumption between each couple of nodes
	 */
	private final double[][] mEnergyMatrix;
	/**
	 * Array with the travel time between each couple of nodes
	 */
	private final double[][] mTimeMatrix;

	private final HashMap<Node, Integer> mMapNodeCStoLocalID;

	private double mMaxWaitingTime = Double.MAX_VALUE;

	public InstanceEVRPNLC(String name, int nbDecimals, int nbNodes, int nbCustomers, int nbCSs, int nbCSType,
			double batteryCapacity, double consumptionRate, double tmax, double smax, double speedFactor, Node[] nodes,
			NodeType[] typeNodes, int depotID, int[] customerID, int[] csID, double[] processingTimes,
			String[] csTypeName, int[] csTypes, int[] nbBreakPoints, double[][][] piecewisePoints,
			double[][] coordinates) {
		super();
		this.mName = name;
		this.mNbDecimals = nbDecimals;
		this.mNbNodes = nbNodes;
		this.mNbCustomers = nbCustomers;
		this.mNbCSs = nbCSs;
		this.mNbCSType = nbCSType;
		this.mBatteryCapacity = batteryCapacity;
		this.mConsumptionRate = consumptionRate;
		this.mTmax = tmax;
		this.mSmax = smax;
		this.mSpeedFactor = speedFactor;
		this.mNodes = nodes;
		this.mTypeNodes = typeNodes;
		this.mDepotID = depotID;
		this.mCustomerIDs = customerID;
		this.mCSIDs = csID;
		this.mProcessingTimes = processingTimes;
		this.mCSTypeName = csTypeName;
		this.mCSTypes = csTypes;
		this.mNbBreakPoints = nbBreakPoints;
		this.mPiecewisePoints = piecewisePoints;
		this.mCoordinates = coordinates;
		this.mDistanceMatrix = EuclideanCalculator.calc(coordinates);
		this.mTimeMatrix = computeTimeMatrix(nbNodes, mDistanceMatrix, speedFactor);
		this.mEnergyMatrix = computeEnergyMatrix(nbNodes, mDistanceMatrix, consumptionRate);
		roundDistanceMatrix(mDistanceMatrix);
		this.mSlope = computeSlope(nbCSType, nbBreakPoints, piecewisePoints);
		this.mYIntercept = computeYIntercept(nbCSType, nbBreakPoints, piecewisePoints, mSlope);
		this.mCapacityCSs = new int[nbCSs];
		this.mMapNodeCStoLocalID = new HashMap<>();
		for (int k = 0; k < nbCSs; k++) {
			mMapNodeCStoLocalID.put(mNodes[mCSIDs[k]], k);
		}
		Arrays.fill(mCapacityCSs, Integer.MAX_VALUE);
		// System.out.println("Energy triangular inequality = " +
		// Utilities.verifyTriangularInequality(mEnergyMatrix));
		// System.out.println("Time triangular inequality = " +
		// Utilities.verifyTriangularInequality(mTimeMatrix));
		this.mMaxWaitingTime = tmax;
	}

	public InstanceEVRPNLC(String name, int nbDecimals, int nbNodes, int nbCustomers, int nbCSs, int nbCSType,
			double batteryCapacity, double consumptionRate, double tmax, double smax, double speedFactor, Node[] nodes,
			NodeType[] typeNodes, int depotID, int[] customerID, int[] csID, double[] processingTimes,
			String[] csTypeName, int[] csTypes, int[] nbBreakPoints, double[][][] piecewisePoints,
			double[][] coordinates, double[][] timeMatrix, double[][] energyMatrix) {
		super();
		this.mName = name;
		this.mNbDecimals = nbDecimals;
		this.mNbNodes = nbNodes;
		this.mNbCustomers = nbCustomers;
		this.mNbCSs = nbCSs;
		this.mNbCSType = nbCSType;
		this.mBatteryCapacity = batteryCapacity;
		this.mConsumptionRate = consumptionRate;
		this.mTmax = tmax;
		this.mSmax = smax;
		this.mSpeedFactor = speedFactor;
		this.mNodes = nodes;
		this.mTypeNodes = typeNodes;
		this.mDepotID = depotID;
		this.mCustomerIDs = customerID;
		this.mCSIDs = csID;
		this.mProcessingTimes = processingTimes;
		this.mCSTypeName = csTypeName;
		this.mCSTypes = csTypes;
		this.mNbBreakPoints = nbBreakPoints;
		this.mPiecewisePoints = piecewisePoints;
		this.mCoordinates = coordinates;
		this.mDistanceMatrix = EuclideanCalculator.calc(coordinates);
		this.mTimeMatrix = timeMatrix;
		this.mEnergyMatrix = energyMatrix;
		roundDistanceMatrix(mDistanceMatrix);
		this.mSlope = computeSlope(nbCSType, nbBreakPoints, piecewisePoints);
		this.mYIntercept = computeYIntercept(nbCSType, nbBreakPoints, piecewisePoints, mSlope);
		this.mCapacityCSs = new int[nbCSs];
		this.mMapNodeCStoLocalID = new HashMap<>();
		for (int k = 0; k < nbCSs; k++) {
			mMapNodeCStoLocalID.put(mNodes[mCSIDs[k]], k);
		}
		Arrays.fill(mCapacityCSs, Integer.MAX_VALUE);
		this.mMaxWaitingTime = tmax;
		// System.out.println("Energy triangular inequality = " +
		// Utilities.verifyTriangularInequality(mEnergyMatrix));
		// System.out.println("Time triangular inequality = " +
		// Utilities.verifyTriangularInequality(mTimeMatrix));
	}

	public void setCSCapacity(int capacity) {
		Arrays.fill(mCapacityCSs, capacity);
	}

	public void setCSCapacity(int[] capacity) {
		System.arraycopy(capacity, 0, mCapacityCSs, 0, mNbCSs);
	}

	public double getMaxWaitingTime() {
		return this.mMaxWaitingTime;
	}

	public void setMaxWaitingTime(double maxWaitingTime) {
		this.mMaxWaitingTime = maxWaitingTime;
	}

	private void roundDistanceMatrix(double[][] distance) {
		for (int i = 0; i < mNbNodes; i++) {
			for (int j = i + 1; j < mNbNodes; j++) {
				distance[i][j] = Maths.floor(distance[i][j], mNbDecimals);
				distance[j][i] = distance[i][j];
			}
		}
	}

	private double[][] computeYIntercept(int nbCSType, int[] nbBreakPoints, double[][][] piecewisePoints,
			double[][] slope) {
		double[][] yIntercept = new double[nbCSType][];
		for (int i = 0; i < nbCSType; i++) {
			int nbSegments = nbBreakPoints[i] - 1;
			yIntercept[i] = new double[nbSegments];
			for (int b = 0; b < nbSegments; b++) {
				yIntercept[i][b] = Maths.floor(piecewisePoints[i][b][1] - slope[i][b] * piecewisePoints[i][b][0],
						mNbDecimals);
			}
		}
		return yIntercept;
	}

	private double[][] computeSlope(int nbCSType, int[] nbBreakPoints, double[][][] piecewisePoints) {
		double[][] slope = new double[nbCSType][];
		for (int i = 0; i < nbCSType; i++) {
			int nbSegments = nbBreakPoints[i] - 1;
			slope[i] = new double[nbSegments];
			for (int b = 0; b < nbSegments; b++) {
				slope[i][b] = Maths.floor((piecewisePoints[i][b + 1][1] - piecewisePoints[i][b][1])
						/ (piecewisePoints[i][b + 1][0] - piecewisePoints[i][b][0]), mNbDecimals);
			}
		}
		return slope;
	}

	private double[][] computeTimeMatrix(int nbNodes, double[][] distanceMatrix, double speedFactor) {
		double[][] timeMatrix = new double[nbNodes][nbNodes];
		for (int i = 0; i < nbNodes; i++) {
			for (int j = i + 1; j < nbNodes; j++) {
				timeMatrix[i][j] = Maths.floor(distanceMatrix[i][j] / speedFactor, mNbDecimals);
				timeMatrix[j][i] = timeMatrix[i][j];
			}
		}
		return timeMatrix;
	}

	private double[][] computeEnergyMatrix(int nbNodes, double[][] distanceMatrix, double consumptionRate) {
		double[][] energyMatrix = new double[nbNodes][nbNodes];
		for (int i = 0; i < nbNodes; i++) {
			for (int j = i + 1; j < nbNodes; j++) {
				energyMatrix[i][j] = Maths.floor(distanceMatrix[i][j] * consumptionRate, mNbDecimals);
				energyMatrix[j][i] = energyMatrix[i][j];
			}
		}
		return energyMatrix;
	}


	public int getNbNodes() {
		return mNbNodes;
	}

	public int getNbCustomers() {
		return mNbCustomers;
	}

	public int getNbCSs() {
		return mNbCSs;
	}

	public int getNbCSType() {
		return mNbCSType;
	}

	public double getBatteryCapacity() {
		return mBatteryCapacity;
	}

	public double getConsumptionRate() {
		return mConsumptionRate;
	}

	public double getSmax() {
		return mSmax;
	}

	public double getTmax() {
		return mTmax;
	}

	public double getSpeedFactor() {
		return mSpeedFactor;
	}

	public NodeType[] getTypeNodes() {
		return mTypeNodes;
	}

	public int getDepotID() {
		return mDepotID;
	}

	public int[] getCustomerIDs() {
		return mCustomerIDs;
	}

	public int[] getCSIDs() {
		return mCSIDs;
	}

	public double[] getProcessingTimes() {
		return mProcessingTimes;
	}

	public String[] getCSTypeNames() {
		return mCSTypeName;
	}

	public int[] getCSTypes() {
		return mCSTypes;
	}

	public int[] getNbBreakPoints() {
		return mNbBreakPoints;
	}

	public double[][][] getPiecewisePoints() {
		return mPiecewisePoints;
	}

	public double[][] getSlope() {
		return mSlope;
	}

	public double[][] getYIntercept() {
		return mYIntercept;
	}

	public double[][] getCoordinates() {
		return mCoordinates;
	}

	public double[][] getCoordinatesCustomers() {
		Node[] customerNodes = this.getCustomerNodes();
		double[][] coordinatesCustomers = new double[mNbCustomers][mNbCustomers];
		int id;
		List<Integer> listID = new ArrayList<>();
		for (int i = 0; i < mNbCustomers; i++) {
			id = customerNodes[i].getID();
			listID.add(id);
			coordinatesCustomers[i][0] = mCoordinates[id][0];
			coordinatesCustomers[i][1] = mCoordinates[id][1];
		}
		return coordinatesCustomers;
	}

	public double[] getCoordinatesDepot() {
		return mCoordinates[mDepotID];
	}

	public double[][] getDistanceMatrix() {
		return mDistanceMatrix;
	}

	public double[][] getEnergyMatrix() {
		return mEnergyMatrix;
	}

	public double[][] getTimeMatrix() {
		return mTimeMatrix;
	}

	public Node[] getCustomerNodes() {
		Node[] customers = new Node[this.mNbCustomers];
		for (int j = 0; j < this.mNbCustomers; j++) {
			customers[j] = mNodes[mCustomerIDs[j]];
		}
		return customers;
	}

	public Node[] getCSNodes() {
		Node[] cs = new Node[this.mNbCSs];
		for (int j = 0; j < this.mNbCSs; j++) {
			cs[j] = mNodes[mCSIDs[j]];
		}
		return cs;
	}

	public Node getDepotNode() {
		return mNodes[mDepotID];
	}

	public Node[] getNodes() {
		return mNodes;
	}

	public Node getNode(int id) {
		return mNodes[id];
	}

	public double getSoCAtferCharge(Node node, double initialSoC, double chargingTime) {
		if (!node.getType().equals(NodeType.CHARGING_STATION)) {
			throw new IllegalArgumentException("Method can only be used with a charging station");
		}
		if (chargingTime < 0) {
			throw new IllegalArgumentException("The charging time has to be larger than 0 -> " + chargingTime);
		}
		int localCSID = mMapNodeCStoLocalID.get(node);
		int typeCS = mCSTypes[localCSID];
		double timeArrival = getTime(typeCS, initialSoC);
		double energyDeparture = getSoC(typeCS, timeArrival + chargingTime);
		return energyDeparture;
	}

	public double getChargingTime(Node node, double soc, double chargingAmount, int nbDecimals) {
		if (!node.getType().equals(NodeType.CHARGING_STATION)) {
			throw new IllegalArgumentException("Method can only be used with a charging station");
		}
		if (chargingAmount < 0) {
			throw new IllegalArgumentException("The charging amount has to be larger than 0 -> " + chargingAmount);
		}
		double initialEnergy = soc;
		if (soc - Math.pow(10, -nbDecimals) > this.getBatteryCapacity()) {
			System.out.println("The energy is too large with respect to the battery capacity -> " + (soc));
		}
		initialEnergy = Math.min(soc, this.getBatteryCapacity());
		double finalEnergy = soc + chargingAmount;
		if (finalEnergy - Math.pow(10, -nbDecimals) > this.getBatteryCapacity()) {
			throw new IllegalArgumentException(
					"The charging amount is too large with respect to the battery capacity -> " + (finalEnergy)
							+ " chargingAmount = " + chargingAmount + " soc = " + soc);

		}
		finalEnergy = Math.min(finalEnergy, this.getBatteryCapacity());
		int localCSID = mMapNodeCStoLocalID.get(node);
		int typeCS = mCSTypes[localCSID];
		double timeArrival = getTime(typeCS, initialEnergy);
		double timeDeparture = getTime(typeCS, finalEnergy);
		double chargingTime = timeDeparture - timeArrival;
		return chargingTime;
	}

	public double getChargingTime(Node node, double soc, double chargingAmount) {
		double finalEnergy = soc + chargingAmount;
		int localCSID = mMapNodeCStoLocalID.get(node);
		int typeCS = mCSTypes[localCSID];
		double timeArrival = getTime(typeCS, soc);
		double timeDeparture = getTime(typeCS, finalEnergy);
		double chargingTime = timeDeparture - timeArrival;
		return chargingTime;
	}

	private boolean isCSStrictlyFaster(int csType1, int csType2) {
		return mSlope[csType1][0] > mSlope[csType2][0];
	}

	public boolean isCSStrictlyFaster(Node cs1, Node cs2) {
		if (!cs1.getType().equals(NodeType.CHARGING_STATION) || !cs2.getType().equals(NodeType.CHARGING_STATION)) {
			throw new IllegalArgumentException("Method can only be used with a charging station");
		}
		int csType1 = mCSTypes[this.getLocalCSID(cs1)];
		int csType2 = mCSTypes[this.getLocalCSID(cs2)];
		return isCSStrictlyFaster(csType1, csType2);
	}

	private boolean isCSFaster(int csType1, int csType2) {
		return mSlope[csType1][0] >= mSlope[csType2][0];
	}

	public boolean isCSFaster(Node cs1, Node cs2) {
		if (!cs1.getType().equals(NodeType.CHARGING_STATION) || !cs2.getType().equals(NodeType.CHARGING_STATION)) {
			throw new IllegalArgumentException("Method can only be used with a charging station");
		}
		int csType1 = mCSTypes[this.getLocalCSID(cs1)];
		int csType2 = mCSTypes[this.getLocalCSID(cs2)];
		return isCSFaster(csType1, csType2);
	}

	public double getMaxChargingTime(Node cs) {
		if (!cs.getType().equals(NodeType.CHARGING_STATION)) {
			throw new IllegalArgumentException("Method can only be used with a charging station");
		}
		int csType = mCSTypes[this.getLocalCSID(cs)];
		return mPiecewisePoints[csType][mNbBreakPoints[csType] - 1][0];
	}

	private double getTime(int typeCS, double soc, int nbDecimals) {
		int b = 0;
		// double socFloor = Maths.floor(soc, nbDecimals);
		// double socFloor = soc;
		if (soc - Math.pow(10, -nbDecimals) > mPiecewisePoints[typeCS][mNbBreakPoints[typeCS] - 1][1]) {
			if (soc - Math.pow(10,
					-GlobalParameters.PRECISION) > mPiecewisePoints[typeCS][mNbBreakPoints[typeCS] - 1][1]) {
				throw new IllegalStateException("Unknown breakpoint -> " + soc + " vs "
						+ mPiecewisePoints[typeCS][mNbBreakPoints[typeCS] - 1][1]);
			} else {
				return mPiecewisePoints[typeCS][mNbBreakPoints[typeCS] - 1][0];
			}
		}
		while (b < (mNbBreakPoints[typeCS] - 1) && mPiecewisePoints[typeCS][b + 1][1] < soc) {
			b++;
		}
		if (b == (mNbBreakPoints[typeCS] - 1)) {
			throw new IllegalStateException(
					"Unknown breakpoint -> " + soc + " vs " + mPiecewisePoints[typeCS][mNbBreakPoints[typeCS] - 1][1]);
		}
		return (soc - mYIntercept[typeCS][b]) / mSlope[typeCS][b];
	}

	private double getTime(int typeCS, double energy) {
		return getTime(typeCS, energy, this.mNbDecimals);
	}

	public double getTime(Node node, double energy) {
		if (!node.getType().equals(NodeType.CHARGING_STATION)) {
			throw new IllegalArgumentException("Method can only be used with a charging station");
		}
		int localCSID = mMapNodeCStoLocalID.get(node);
		int typeCS = mCSTypes[localCSID];
		return getTime(typeCS, energy);
	}

	private double getSoC(int typeCS, double time, int nbDecimals) {
		int b = 0;
		double timeFloor = Maths.floor(time, nbDecimals);
		while (b < (mNbBreakPoints[typeCS] - 1) && mPiecewisePoints[typeCS][b + 1][0] < timeFloor) {
			b++;
		}
		if (b == (mNbBreakPoints[typeCS] - 1)) {
			return this.getBatteryCapacity();
			// throw new IllegalStateException("Unknown breakpoint -> " +
			// timeFloor + " vs "
			// + mPiecewisePoints[typeCS][mNbBreakPoints[typeCS] - 1][0]);
		}
		return timeFloor * mSlope[typeCS][b] + mYIntercept[typeCS][b];
	}

	private double getSoC(int typeCS, double time) {
		return getSoC(typeCS, time, this.mNbDecimals);
	}

	public double[][] getSupportingPoints(Node node) {
		if (!node.getType().equals(NodeType.CHARGING_STATION)) {
			throw new IllegalArgumentException("Method can only be used with a charging station");
		}
		int localCSID = mMapNodeCStoLocalID.get(node);
		int typeCS = mCSTypes[localCSID];
		double[][] spArray = new double[2][mPiecewisePoints[typeCS].length];
		for (int k = 0; k < mPiecewisePoints[typeCS].length; k++) {
			spArray[0][k] = mPiecewisePoints[typeCS][k][0];
			spArray[1][k] = mPiecewisePoints[typeCS][k][1];
		}
		// return mPiecewisePoints[typeCS];
		return spArray;
	}

	public double[] getSlope(Node node) {
		if (!node.getType().equals(NodeType.CHARGING_STATION)) {
			throw new IllegalArgumentException("Method can only be used with a charging station");
		}
		int localCSID = mMapNodeCStoLocalID.get(node);
		int typeCS = mCSTypes[localCSID];
		return mSlope[typeCS];
	}

	public double[] getYIntercept(Node node) {
		if (!node.getType().equals(NodeType.CHARGING_STATION)) {
			throw new IllegalArgumentException("Method can only be used with a charging station");
		}
		int localCSID = mMapNodeCStoLocalID.get(node);
		int typeCS = mCSTypes[localCSID];
		return mYIntercept[typeCS];
	}

	public double getSlope(Node node, double soc) {
		int segment = getSegment(node, soc);
		int localCSID = mMapNodeCStoLocalID.get(node);
		int typeCS = mCSTypes[localCSID];
		return mSlope[typeCS][segment];

	}

	public int getSegment(Node node, double soc) {
		if (!node.getType().equals(NodeType.CHARGING_STATION)) {
			throw new IllegalArgumentException("Method can only be used with a charging station");
		}
		int localCSID = mMapNodeCStoLocalID.get(node);
		int typeCS = mCSTypes[localCSID];
		int nbPoints = mNbBreakPoints[typeCS];
		int k = 0;
		while (k < nbPoints && mPiecewisePoints[typeCS][k][1] <= soc) {
			k++;
		}
		if (k == 0) {
			throw new IllegalStateException(mPiecewisePoints[typeCS][k][1] + " vs " + soc);
		}
		return k - 1;

	}

	public int getCapacityCS(Node node) {
		if (!node.getType().equals(NodeType.CHARGING_STATION)) {
			throw new IllegalArgumentException("Method can only be used with a charging station");
		}
		int localCSID = mMapNodeCStoLocalID.get(node);
		return mCapacityCSs[localCSID];

	}

	public int[] getCapacityCSs() {
		return mCapacityCSs;
	}

	public int getLocalCSID(Node node) {
		return mMapNodeCStoLocalID.get(node);
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("---------- Instance " + mName + " ----------\n");
		s.append(mNbNodes + " " + mNbCustomers + " " + mNbCSs + " " + mNbCSType + "\n");
		s.append("--- General ---\n");
		s.append("battery " + mBatteryCapacity + "\n");
		s.append("Tmax " + mTmax + "\n");
		s.append("typeNodes " + Arrays.toString(mTypeNodes) + "\n");
		s.append("processingTime " + Arrays.toString(mProcessingTimes) + "\n");
		s.append("depotID " + mDepotID + "\n");
		s.append("customerIDs " + Arrays.toString(mCustomerIDs) + "\n");
		s.append("CSIDs " + Arrays.toString(mCSIDs) + "\n");
		s.append("CS Capacity " + Arrays.toString(mCapacityCSs) + "\n");
		s.append("CSMap " + mMapNodeCStoLocalID + "\n");
		s.append("CSTypes" + Arrays.toString(mCSTypes) + "\n");
		s.append("--- CS Types ---\n");
		for (int c = 0; c < mNbCSType; c++) {
			s.append("slope " + Arrays.toString(mSlope[c]) + "\n");
			s.append("yIntercept" + Arrays.toString(mYIntercept[c]) + "\n");
			for (int b = 0; b < mNbBreakPoints[c]; b++) {
				s.append(mPiecewisePoints[c][b][0] + " ");
			}
			s.append("\n");
			for (int b = 0; b < mNbBreakPoints[c]; b++) {
				s.append(mPiecewisePoints[c][b][1] + " ");
			}
			s.append("\n");
		}
		s.append("--- Matrix distance ---\n");
		for (int n = 0; n < mNbNodes; n++) {
			s.append(Arrays.toString(mDistanceMatrix[n]) + "\n");
		}
		s.append("--- Matrix time ---\n");
		for (int n = 0; n < mNbNodes; n++) {
			s.append(Arrays.toString(mTimeMatrix[n]) + "\n");
		}
		s.append("--- Matrix energy ---\n");
		for (int n = 0; n < mNbNodes; n++) {
			s.append(Arrays.toString(mEnergyMatrix[n]) + "\n");
		}
		return s.toString();
	}

	public int getNbDecimals() {
		return mNbDecimals;
	}

	public double getFastestChargingSlope() {
		double max = 0;
		for (int t = 0; t < this.getNbCSType(); t++) {
			if (mSlope[t][0] > max) {
				max = mSlope[t][0];
			}
		}
		return max;
	}

	/**
	 * @return the capacityID
	 */
	public int getCapacityID() {
		return capacityID;
	}

	/**
	 * @param capacityID the capacityID to set
	 */
	public void setCapacityID(int capacityID) {
		this.capacityID = capacityID;
	}

}

