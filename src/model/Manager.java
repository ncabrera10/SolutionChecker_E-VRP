package model;

import java.io.IOException;

/**
 * Class to manage the different options
 */

public class Manager {

	public Manager() throws IOException, InterruptedException{

	}
	
	/**
	 * Runs the checker
	 */
	
	public Checker runCheck(int se,int A,int B,int C,String D, int E,int F) {
		
		//Creates a checker:
		
		Checker ch = new Checker(se,A,B,C,D,E,F);
		
		//Returns the checker:
		
		return ch;
	}
}
