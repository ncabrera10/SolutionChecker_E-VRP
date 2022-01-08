package dataStructures;

import java.util.List;

public interface Route extends Cloneable {

	public double getOF();

	public void setOF(double of);

	public int size();

	public double getStartTime();

	public int positionOf(int nodeID);

	public int getAtPosition(int position);

	public double getChargingAmount(int position);

	public double getWaitingTime(int position);

	public int getFirst();

	public int getLast();

	public List<Integer> getRoute();

	public String getKey();
	
	public int getRouteID();
}
