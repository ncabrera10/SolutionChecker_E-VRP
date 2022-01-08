package dataStructures;

import java.util.ArrayList;
import java.util.List;

public class Solution {

	/**
	 * Instance id
	 */
	private String instance_id;
	
	/**
	 * Solver used for this solution
	 */
	private String solver;
	
	/**
	 * Is this optimal?
	 */
	private String optimal;
	
	/**
	 * Parameters used for the run:
	 */
	private String cpu;
	private String cores;
	private String ram;
	private String language;
	private String os;
	private String cputime;
	
	/**
	 * Stores the list of routes
	 */
	private List<Route> mListRoutes;
	/**
	 * The objective function
	 */
	private double mObj;

	/**
	 * Constructs a new Solution
	 */
	public Solution() {
		this.mListRoutes = new ArrayList<>();
	}

	/**
	 * Returns the objective function
	 * @return
	 */
	public double getOF() {
		return mObj;
	}

	/**
	 * Modifies the objective function
	 * @param obj
	 */
	public void setOF(double obj) {
		this.mObj = obj;
	}

	/**
	 * Returns the number of routes
	 * @return
	 */
	public int getNbRoutes() {
		return this.mListRoutes.size();
	}

	/**
	 * Adds a route to the route list
	 * @param route
	 */
	public void addRoute(Route route) {
		mListRoutes.add(route);
	}

	/**
	 * Returns a route
	 * @param i
	 * @return
	 */
	public Route getRoute(int i) {
		return mListRoutes.get(i);
	}

	/**
	 * Returns a character with the objective function and the routes info
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Obj = " + getOF() + "| #Routes = " + this.getNbRoutes() + "\n");
		for (Route route : mListRoutes) {
			sb.append(route.toString() + "\n");
		}
		return sb.toString();
	}

	/**
	 * Returns the list of routes
	 * @return
	 */
	public List<Route> getRoutes() {
		return this.mListRoutes;
	}

	/**
	 * @return the instance_id
	 */
	public String getInstance_id() {
		return instance_id;
	}

	/**
	 * @param instance_id the instance_id to set
	 */
	public void setInstance_id(String instance_id) {
		this.instance_id = instance_id;
	}

	/**
	 * @return the solver
	 */
	public String getSolver() {
		return solver;
	}

	/**
	 * @param solver the solver to set
	 */
	public void setSolver(String solver) {
		this.solver = solver;
	}

	/**
	 * @return the optimal
	 */
	public String getOptimal() {
		return optimal;
	}

	/**
	 * @param optimal the optimal to set
	 */
	public void setOptimal(String optimal) {
		this.optimal = optimal;
	}

	/**
	 * @return the cpu
	 */
	public String getCpu() {
		return cpu;
	}

	/**
	 * @param cpu the cpu to set
	 */
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	/**
	 * @return the cores
	 */
	public String getCores() {
		return cores;
	}

	/**
	 * @param cores the cores to set
	 */
	public void setCores(String cores) {
		this.cores = cores;
	}

	/**
	 * @return the ram
	 */
	public String getRam() {
		return ram;
	}

	/**
	 * @param ram the ram to set
	 */
	public void setRam(String ram) {
		this.ram = ram;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the os
	 */
	public String getOs() {
		return os;
	}

	/**
	 * @param os the os to set
	 */
	public void setOs(String os) {
		this.os = os;
	}

	/**
	 * @return the cputime
	 */
	public String getCputime() {
		return cputime;
	}

	/**
	 * @param cputime the cputime to set
	 */
	public void setCputime(String cputime) {
		this.cputime = cputime;
	}

	
}
