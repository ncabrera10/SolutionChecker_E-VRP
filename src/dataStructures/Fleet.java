package dataStructures;

import java.util.ArrayList;
import java.util.Hashtable;

public class Fleet {

	/**
	 * Contains the vehicles in the fleet
	 */
	private ArrayList<Vehicle> vehicles;
	
	public Fleet() {
		
		vehicles = new ArrayList<Vehicle>();
		
	}

	public void addVehicle(int t,int dep,int arr,int max,int spe,double cons_rate, double bat_cap,Hashtable<String,ArrayList<Double>>bat_lev,Hashtable<String,ArrayList<Double>>cha_tim) {
		
		//Adds the vehicle:
		
		vehicles.add(new Vehicle(t,dep,arr,max,spe,cons_rate,bat_cap,bat_lev,cha_tim));
		
	}
	
	
	/**
	 * @return the vehicles
	 */
	public ArrayList<Vehicle> getVehicles() {
		return vehicles;
	}

	/**
	 * @param vehicles the vehicles to set
	 */
	public void setVehicles(ArrayList<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}
	
	
}
