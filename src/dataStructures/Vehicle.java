package dataStructures;

import java.util.ArrayList;
import java.util.Hashtable;

public class Vehicle {

	private int type;
	
	private int departure_node;
	
	private int arrival_node;
	
	private int max_travel_time;
	
	private int speed_factor;
	
	private double battery_capacity;
	
	private double consumption_rate;
	
	private Hashtable<String,ArrayList<Double>> battery_levels;
	
	private Hashtable<String,ArrayList<Double>> charging_times;

	/**
	 * Creates a new vehicle type
	 * @param t
	 * @param dep
	 * @param arr
	 * @param max
	 * @param spe
	 */
	
	public Vehicle(int t,int dep,int arr,int max,int spe,double cons_rate, double bat_cap,Hashtable<String,ArrayList<Double>>bat_lev,Hashtable<String,ArrayList<Double>>cha_tim) {
		
		type = t;
		departure_node = dep;
		arrival_node = arr;
		max_travel_time = max;
		speed_factor = spe;
		battery_capacity = bat_cap;
		consumption_rate = cons_rate;
		battery_levels = bat_lev;
		charging_times = cha_tim;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the departure_node
	 */
	public int getDeparture_node() {
		return departure_node;
	}

	/**
	 * @param departure_node the departure_node to set
	 */
	public void setDeparture_node(int departure_node) {
		this.departure_node = departure_node;
	}

	/**
	 * @return the arrival_node
	 */
	public int getArrival_node() {
		return arrival_node;
	}

	/**
	 * @param arrival_node the arrival_node to set
	 */
	public void setArrival_node(int arrival_node) {
		this.arrival_node = arrival_node;
	}

	/**
	 * @return the max_travel_time
	 */
	public int getMax_travel_time() {
		return max_travel_time;
	}

	/**
	 * @param max_travel_time the max_travel_time to set
	 */
	public void setMax_travel_time(int max_travel_time) {
		this.max_travel_time = max_travel_time;
	}

	/**
	 * @return the speed_factor
	 */
	public int getSpeed_factor() {
		return speed_factor;
	}

	/**
	 * @param speed_factor the speed_factor to set
	 */
	public void setSpeed_factor(int speed_factor) {
		this.speed_factor = speed_factor;
	}

	/**
	 * @return the battery_capacity
	 */
	public double getBattery_capacity() {
		return battery_capacity;
	}

	/**
	 * @param battery_capacity the battery_capacity to set
	 */
	public void setBattery_capacity(double battery_capacity) {
		this.battery_capacity = battery_capacity;
	}

	/**
	 * @return the consumption_rate
	 */
	public double getConsumption_rate() {
		return consumption_rate;
	}

	/**
	 * @param consumption_rate the consumption_rate to set
	 */
	public void setConsumption_rate(double consumption_rate) {
		this.consumption_rate = consumption_rate;
	}

	/**
	 * @return the battery_levels
	 */
	public Hashtable<String,ArrayList<Double>> getBattery_levels() {
		return battery_levels;
	}

	/**
	 * @param battery_levels the battery_levels to set
	 */
	public void setBattery_levels(Hashtable<String,ArrayList<Double>>battery_levels) {
		this.battery_levels = battery_levels;
	}

	/**
	 * @return the charging_times
	 */
	public Hashtable<String,ArrayList<Double>> getCharging_times() {
		return charging_times;
	}

	/**
	 * @param charging_times the charging_times to set
	 */
	public void setCharging_times(Hashtable<String,ArrayList<Double>>charging_times) {
		this.charging_times = charging_times;
	}

	
}
