package massiveChecking;

import java.io.File;

import globalParameters.GlobalParameters;
import globalParameters.GlobalParametersReader;
import model.Checker;

/**
 * This is the main class to test several solutions in just one run.
 * @author nick0
 *
 */
public class Main_Checker {

	/**
	 * Main method.
	 */
	public static void main(String[] args) {
		
		//1. Read the config file:
		
			GlobalParametersReader.initialize("./config/parametersGlobal.xml");
		
		//2. Reads all the files in the solutions folder:
			
			File folder = new File(GlobalParameters.SOLUTIONS_FOLDER);
			File[] listOfFiles = folder.listFiles();
		
		//3. Checks one by one:
			
			for (int i = 0; i < listOfFiles.length; i++) {
				  if (listOfFiles[i].isFile()) {
					  
					  new Checker(listOfFiles[i].getName());
					  
				  }
			}
	}
}
