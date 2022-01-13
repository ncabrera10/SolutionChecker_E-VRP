package controller;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import globalParameters.GlobalParametersReader;
import model.Manager;
import view.ManagerView;

/**
 * This class controls both the view and the models. 
 * From this class the program is ran and controlled. 
 */

public class Controller {

	/**
	 * View Object
	 */
	
	private ManagerView view;
	
	/**
	 * Model Object
	 */
	
	private Manager model;
	
	/**
	 * Constructor
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public Controller() throws IOException, InterruptedException
	{
		view = new ManagerView();
		model = new Manager();
	}
	
	/**
	 * This method runs the program. 
	 */
	
	public void run() {
		
		// Creates a scanner:
		
		Scanner sc = new Scanner(System.in);
		
		// Initializes the option chosen by the user:
		
		int option = -1;
		
		// Initializes a boolean variable to end the app:
		
		boolean fin = false;
		
		// Initializes a boolean stating if a selection was made:
		
		boolean numeroEncontrado = false;
		
		// While still running:
		
		while(!fin)
		{
			// Print the menu:
				
			view.printMenu();
				
			// If the user puts an incorrect value:
				
			while (!numeroEncontrado){
				try {
					option = sc.nextInt();
					numeroEncontrado = true;
				} catch (InputMismatchException e) {
					System.out.println("You entered an invalid option");
					view.printMenu();
					sc = new Scanner(System.in);
				}
			} numeroEncontrado = false;
			
			// Try-catch to avoid starting again (due to small errors):
					
			try { 
				int iterador = 0;
				switch(option)
				{
					//Case when the user presses 1: Uses the set of instances of coindreau et al (2019)
					
					case 1:
						// Reads the global parameters:
						
						if(iterador == 0) {
							GlobalParametersReader.initialize("./config/parametersGlobal.xml");
						}
						
						iterador++;
						view.printMessage("Select A (0: random uniform, 1: clustered, 2: mixture of both");
						int A = Integer.parseInt(sc.next());
						view.printMessage("Select B (Number of costumers)");
						int B = Integer.parseInt(sc.next());
						view.printMessage("Select C (Number of CSs)");
						int C = Integer.parseInt(sc.next());
						view.printMessage("Select D (t: p-median,  f: randomly located)");
						String D = (sc.next());
						view.printMessage("Select E (Instance ID for each combination 0-4)");
						int E = Integer.parseInt(sc.next());
						model.runCheck(1,A,B,C,D,E,-1);
						System.out.println("tc"+A+"c"+B+"s"+C+"c"+D+E);
						System.out.println("The outputs are at ./results/");
						fin=true;
						sc.close();
						
					break;
					
					case 2:
						
						if(iterador == 0) {
							GlobalParametersReader.initialize("./config/parametersGlobal.xml");
						}
						
						iterador++;
						view.printMessage("Select A (0: random uniform, 1: clustered, 2: mixture of both");
						int A2 = Integer.parseInt(sc.next());
						view.printMessage("Select B (Number of costumers)");
						int B2 = Integer.parseInt(sc.next());
						view.printMessage("Select C (Number of CSs)");
						int C2 = Integer.parseInt(sc.next());
						view.printMessage("Select D (t: p-median,  f: randomly located)");
						String D2 = (sc.next());
						view.printMessage("Select E (Instance ID for each combination 0-4)");
						int E2 = Integer.parseInt(sc.next());
						view.printMessage("Select F (Capacity for each CSs 1 or 2)");
						int F2 = Integer.parseInt(sc.next());
						model.runCheck(2,A2,B2,C2,D2,E2,F2);
						
						System.out.println("tc"+A2+"c"+B2+"s"+C2+"c"+D2+E2+"-C"+F2);
						System.out.println("The outputs are at ./results/");
						fin=true;
						sc.close();
						
					break;
					
					case 3:
						fin=true;
						sc.close();
					break;
				}
			}catch(Exception e) { // If an error ocurred.
				e.printStackTrace(); System.out.println("Something happen. We recommend you to start over");
			}
		}
	}
}
