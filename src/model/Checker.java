package model;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import dataStructures.InstanceEVRPNLC;
import dataStructures.Node;
import dataStructures.NodeType;
import dataStructures.Route;
import dataStructures.RouteArray;
import dataStructures.Solution;
import evaluators.Evaluator_Constraints;
import evaluators.Evaluator_FO;
import globalParameters.GlobalParameters;
import utilities.Utilities;
import utilities.XMLParser;

/**
 * This class is a checker !
 * @author nick0
 *
 */
public class Checker {

	/**
	 * 
	 */
	private int A;
	
	/**
	 * 
	 */
	private int B;
	
	/**
	 * 
	 */
	private int C;
	
	/**
	 * 
	 */
	private String D;
	
	/**
	 * 
	 */
	private int E;
	

	public Checker(int se,int a,int b,int c,String d, int e) {
		
		//Initialize main parameters of the instance:
		
			A = a;
			B = b;
			C = c;
			D = d;
			E = e;
		
		//Read the instance:
		
			InstanceEVRPNLC instance = this.readInstance_C();
			if(se == 2) {
				instance.setCSCapacity(1); //or 2;
			}
			
		//Reads the solution:
			
			Solution solution = this.readSolution_C();
			
		//Print the report:
			
			this.printReport(solution,instance);
		
		
	}
	
	public Checker(String solutionFileName) {
		
		//Reads the solution:
		
			Solution solution = this.readSolution(solutionFileName);
		
		//Read the instance:
			
			InstanceEVRPNLC instance = this.readInstance(solution.getInstance_id());
		
		// Is this from the first set or the second one ? TODO adapt with the new format
			
			//instancePath = "Montoya_2017";
			
			
			String[] parts = solutionFileName.split("-");
			if(parts.length > 1) {
				if(parts[1].charAt(1) == 1) {
					instance.setCapacityID(1);
					instance.setCSCapacity(1);
				}else {
					instance.setCapacityID(2);
					instance.setCSCapacity(2);
				}
				
			}
			
		//Print the report:
			
			this.printReport(solution,instance);
		
	}
	
	public void printReport(Solution solution,InstanceEVRPNLC instance) {
		
		//Creates a path to print the report:
		
			String ruta = GlobalParameters.RESULT_FOLDER+"Report_"+solution.getInstance_id()+"-"+solution.getSolver()+"C-"+instance.getCapacityID()+".txt";
			String ruta_details = GlobalParameters.RESULT_FOLDER+"DetailsReport_"+solution.getInstance_id()+"-"+solution.getSolver()+"C-"+instance.getCapacityID()+".txt";
			String ruta_shortReport = GlobalParameters.RESULT_FOLDER+"ShortReport_"+solution.getInstance_id()+"-"+solution.getSolver()+"C-"+instance.getCapacityID()+".txt";
			
		//Main logic:
			
		try {
			PrintWriter pw = new PrintWriter(new File(ruta));
			PrintWriter pw2 = new PrintWriter(new File(ruta_details));
			PrintWriter pw3 = new PrintWriter(new File(ruta_shortReport));
			
			//Headline:
			
			pw.println("Solver:"+solution.getSolver());
			pw.println("Instance:"+solution.getInstance_id());
			pw3.println("Solver:"+solution.getSolver());
			pw3.println("Instance:"+solution.getInstance_id());
			pw3.println("CPU_Time:"+solution.getCputime());
			pw3.println("SolutionIsOptimal:"+solution.getOptimal());
			
			//Check if the solution complies with the constraints:
				
				Evaluator_Constraints evaluator_cons = new Evaluator_Constraints();
				boolean cons1 = evaluator_cons.evaluateRouteCharge(solution,instance,GlobalParameters.PRECISION,pw2);
				boolean cons2 = evaluator_cons.evaluateRouteDuration(solution,instance,GlobalParameters.PRECISION,pw2);
				boolean cons3 = evaluator_cons.evaluateRouteStartEnd(solution,instance,GlobalParameters.PRECISION,pw2);
				boolean cons4 = evaluator_cons.evaluateCustomersVisited(solution,instance,GlobalParameters.PRECISION,pw2);
				boolean cons5 = evaluator_cons.evaluateCSCapacity(solution,instance,GlobalParameters.PRECISION,pw2);
				
				pw.println("----------------Constraints----------------");
				pw.println("Route charge constraints: "+cons1);
				pw.println("Route duration constraints: "+cons2);
				pw.println("Route start-end constraints: "+cons3);
				pw.println("All customers must be visited constraints: "+cons4);
				pw.println("CS's capacities constraints: "+cons5);
				if(cons1 && cons2 && cons3 && cons4 && cons5) {
					pw.println("The solution is valid");
					pw3.println("Feasibility:"+1);
				}else {
					pw.println("The solution is invalid");
					pw3.println("Feasibility:"+0);
				}
				
			//Objective function for the solution:
				
				Evaluator_FO evaluator = new Evaluator_FO();
				double objFO = evaluator.evaluateObjectiveFunctionSolution(solution, instance, GlobalParameters.PRECISION);
				pw.println("----------------Routes----------------");
				pw.println("ObjectiveFunction: "+objFO);
				pw3.println("ObjectiveFunction:"+objFO);	
			
			//Objective function for each route:
				pw.println("----------------Routes----------------");
				pw.println("RouteID - ObjectiveFunction");
				for(Route route : solution.getRoutes()) {
					pw.println(route.getRouteID()+" - "+evaluator.evaluateObjectiveFunctionRoute(route, instance, GlobalParameters.PRECISION));
				}
				
			//Closes the print writer
				
			pw.close();
			pw2.close();
			pw3.close();
		}
		catch(Exception e) {
			System.out.println("Problem printing report for this solution");
		}
		
		
		
	}
	
	/**
	 * This method reads an instance:
	 * @return
	 */
	public InstanceEVRPNLC readInstance_C() {
		
		//Build the current path:
		
		String pathName = GlobalParameters.INSTANCE_FOLDER+"/"+"tc"+A+"c"+B+"s"+C+"c"+D+E+".xml";
		
		// Read the XML file:
		
			//Check if the file is not null:
		
			Document mXMLData = XMLParser.parse(pathName);
			if (mXMLData == null) {
				throw new IllegalStateException("The instance file cannot be read -> " + pathName);
			}
			
			//First element: Instance name
				
				//Retrieve the node:
			
					Element root = mXMLData.getRootElement();
				
				//Get the child:
					
					Element e = root.getChild("info");
					
				//Recover the instance name:
					
					String name = e.getChild("name").getTextTrim();
			
			//Network:
			
				//Recover the child:
					
					e = root.getChild("network");
					
				//Store the precision needed:
					
					int decimals = Integer.parseInt(e.getChild("decimals").getTextTrim());
					
				//Create a list of children nodes (nodes):
					
					List<Element> nodes = e.getChild("nodes").getChildren();
				
				//Initialize the depot id and the nodes size:
				
					int depotID = -1;
					int nbNodes = nodes.size();
		
				//Create arrays to store the coordinates, the ids and the node types:
					
					double[][] coordinatesArray = new double[nbNodes][2];
					int[] originalIDArray = new int[nbNodes];
					NodeType[] typeArray = new NodeType[nbNodes];
					Node[] nodeArray = new Node[nbNodes];
					
				//Create three lists to store information for the CS's and the customers:
					
					List<Integer> csIDList = new ArrayList<>();
					List<Integer> customerIDList = new ArrayList<>();
					List<String> csTypeList = new ArrayList<>();
					
				//Iterate over all nodes:
					
					for (int n = 0; n < nbNodes; n++) {
						
						Element node = nodes.get(n);
						try {
							int id = node.getAttribute("id").getIntValue();
							int type = node.getAttribute("type").getIntValue();
							double x = Double.parseDouble(node.getChild("cx").getTextTrim());
							double y = Double.parseDouble(node.getChild("cy").getTextTrim());
							coordinatesArray[n][0] = x;
							coordinatesArray[n][1] = y;
							originalIDArray[n] = id;
							typeArray[n] = NodeType.convert(type);
							nodeArray[n] = new Node(id, "N" + id, typeArray[n]);
							switch (type) {
							case 0:
								if (depotID != -1) {
									throw new IllegalStateException("An instance can only have one depot");
								}
								depotID = id;
								break;
							case 1:
								customerIDList.add(id);
								break;
							case 2:
								csIDList.add(id);
								String idFunction = node.getChild("custom").getChild("cs_type").getTextTrim();
								csTypeList.add(idFunction);
								break;
							default:
								throw new IllegalStateException("Unknown type of node " + type);
							}
						} catch (DataConversionException ex) {
							ex.printStackTrace();
							throw new IllegalStateException("The node cannot be read" + node.toString());
						}
						
					}
					
					// Modify the created lists (to primitive/array):
					
					int[] csID = Utilities.toPrimitive(csIDList.toArray(new Integer[0]));
					int[] customerID = Utilities.toPrimitive(customerIDList.toArray(new Integer[0]));
					String[] csTypesS = csTypeList.toArray(new String[0]);
					
			// Data on the Fleet and the charging functions:
					
				// Recover the node:
					
					e = root.getChild("fleet").getChild("vehicle_profile");

				// Recover the speed factor and max travel time:
					
					double speedFactor = Double.parseDouble(e.getChild("speed_factor").getTextTrim());
					double tMax = Double.parseDouble(e.getChild("max_travel_time").getTextTrim());
					
				// Recover the node (custom):
					
					e = e.getChild("custom");
					
				// Store the consumption rate and the battery capacity:
					
					double consumptionRate = Double.parseDouble(e.getChild("consumption_rate").getTextTrim());
					double batteryCapacity = Double.parseDouble(e.getChild("battery_capacity").getTextTrim());
				
				// Create arrays to store information of the charging_functions:
					
					List<Element> functions = e.getChild("charging_functions").getChildren();
					int nbCSType = functions.size();
					String[] csTypeName = new String[nbCSType];
					int[] nbBreakPointsArray = new int[nbCSType];
					double[][][] piecewisePoints = new double[nbCSType][][];
					double sMax = 0;
					String type = null;
					HashMap<String, Integer> mapCSTypetoNewID = new HashMap<>();
					
				// Recover the information:
					
					for (int i = 0; i < nbCSType; i++) {
						Element function = functions.get(i);
						type = function.getAttribute("cs_type").getValue();
						csTypeName[i] = type;
						mapCSTypetoNewID.put(type, i);
						List<Element> breakPoints = function.getChildren();
						int nbBreakPoints = breakPoints.size();
						nbBreakPointsArray[i] = nbBreakPoints;
						piecewisePoints[i] = new double[nbBreakPoints][2];
						for (int b = 0; b < nbBreakPoints; b++) {
							Element breakpoint = breakPoints.get(b);
							try {
								piecewisePoints[i][b][1] = Double.parseDouble(breakpoint.getChild("battery_level").getTextTrim());
								piecewisePoints[i][b][0] = Double.parseDouble(breakpoint.getChild("charging_time").getTextTrim());
								if (piecewisePoints[i][b][0] > sMax) {
									sMax = piecewisePoints[i][b][0];
								}
							} catch (NumberFormatException e1) {
								e1.printStackTrace();
								throw new IllegalStateException("The breakpoint cannot be read" + breakpoint.toString());
							}
						}
					}
					
				// Convert the cs_type to the id
					
					int[] csTypes = new int[csTypesS.length];
					for (int k = 0; k < csTypesS.length; k++) {
						csTypes[k] = mapCSTypetoNewID.get(csTypesS[k]);
					}

			// Data on the processing time associated with each customer
					
				// Recover the child:
					
					e = root.getChild("requests");
					
				//Create a iterator and an array to store the processing times:
					
					Iterator<Element> itRequests = e.getChildren().iterator();
					double[] processingTimes = new double[nbNodes];
					
				//Iterate thorough the requests:
					
					while (itRequests.hasNext()) {
						Element request = itRequests.next();
						int nodeID;
						try {
							nodeID = request.getAttribute("node").getIntValue();
							double processingTime = Double.parseDouble(request.getChild("service_time").getTextTrim());
							processingTimes[nodeID] = processingTime;
						} catch (DataConversionException e1) {
							e1.printStackTrace();
							throw new IllegalStateException("The node ID is unkwnon " + request.toString());
						}
					}
			
		// Create the instance object:
					
			InstanceEVRPNLC instance = new InstanceEVRPNLC(name, decimals, nbNodes, customerID.length, csID.length,
							nbCSType, batteryCapacity, consumptionRate, tMax, sMax, speedFactor, nodeArray, typeArray, depotID,
							customerID, csID, processingTimes, csTypeName, csTypes, nbBreakPointsArray, piecewisePoints,
							coordinatesArray);
			
		// Return the instance:
			
			return instance;
					
	}
	
	/**
	 * This method runs a solution
	 * @return
	 */
	public Solution readSolution_C() {
		
		// Build the current path:
		
			String pathName =  GlobalParameters.SOLUTIONS_FOLDER+"/"+"tc"+A+"c"+B+"s"+C+"c"+D+E+".xml";

		// Read the xml file:
				
			//Tries to parse the document:
			
				Document mXMLData = XMLParser.parse(pathName);
				if (mXMLData == null) {
					return null;
				}
			
			// Data on the different nodes
				
				//Recovers the root element:
				
					Element root = mXMLData.getRootElement();
				
				//Recover the main parameters of the solution:
				
					String instance_id = root.getAttributes().get(0).getValue();
					String solver = root.getAttributes().get(1).getValue();
					String optimal = root.getAttributes().get(2).getValue();
					
				//Recovers the root element and creates an iterator for each children:
					
					Iterator<Element> itRoutes = root.getChildren().iterator();
					
				//Creates a new solution object:
				
					Solution sol = new Solution();
					sol.setInstance_id(instance_id);
					sol.setSolver(solver);
					sol.setOptimal(optimal);
					
				//Initializes the main attributes:
					
					Element eRoute, eNode;
					int nodeID;
					double waitingTime, chargingAmount, startingTime;
					List<Element> nodeChildren;
				
				//Iterates thorough each route:
					
					while (itRoutes.hasNext()) {
						RouteArray route = new RouteArray();
						eRoute = itRoutes.next();
						if(eRoute.getName().equals("route")) {
							route.setRouteID(Integer.parseInt(eRoute.getAttributes().get(0).getValue()));
							Iterator<Element>itNodesInRoute = eRoute.getChildren().iterator();//eRoute.getChild("sequence");
							while (itNodesInRoute.hasNext()) {
								eNode = itNodesInRoute.next();
								try {
									nodeID = eNode.getAttribute("id").getIntValue();
									nodeChildren = eNode.getChildren();
									if (!nodeChildren.isEmpty()) {
										waitingTime = Double.parseDouble(eNode.getChild("wait").getTextTrim());
										chargingAmount = Double.parseDouble(eNode.getChild("charge").getTextTrim());
										route.insert(nodeID, waitingTime, chargingAmount);
									} else {
										route.insert(nodeID, 0, 0);
									}
								} catch (DataConversionException | NumberFormatException e) {
									e.printStackTrace();
								}
							}
							startingTime = 0;//Double.parseDouble(eRoute.getChild("start_time").getTextTrim());
							route.setStartTime(startingTime);
							sol.addRoute(route);
						}else {
							String cputime = eRoute.getChild("cputime").getValue();
							Element specs = eRoute.getChild("machine");
							String cpu = specs.getChild("cpu").getValue();
							String cores = specs.getChild("cores").getValue();
							String ram = specs.getChild("ram").getValue();
							String language = specs.getChild("language").getValue();
							String os = specs.getChild("os").getValue();
						
							sol.setCores(cores);
							sol.setCpu(cpu);
							sol.setRam(ram);
							sol.setLanguage(language);
							sol.setOs(os);
							sol.setCputime(cputime);
						}
						
					}
		
			
		//Returns the solution:
				
			return sol;
	}
	
	
	
	/**
	 * This method reads an instance:
	 * @return
	 */
	public InstanceEVRPNLC readInstance(String instanceName) {
		
		//Build the current path:
		
		String pathName = GlobalParameters.INSTANCE_FOLDER+"/"+instanceName+".xml";
		
		// Read the XML file:
		
			//Check if the file is not null:
		
			Document mXMLData = XMLParser.parse(pathName);
			if (mXMLData == null) {
				throw new IllegalStateException("The instance file cannot be read -> " + pathName);
			}
			
			//First element: Instance name
				
				//Retrieve the node:
			
					Element root = mXMLData.getRootElement();
				
				//Get the child:
					
					Element e = root.getChild("info");
					
				//Recover the instance name:
					
					String name = e.getChild("name").getTextTrim();
			
			//Network:
			
				//Recover the child:
					
					e = root.getChild("network");
					
				//Store the precision needed:
					
					int decimals = Integer.parseInt(e.getChild("decimals").getTextTrim());
					
				//Create a list of children nodes (nodes):
					
					List<Element> nodes = e.getChild("nodes").getChildren();
				
				//Initialize the depot id and the nodes size:
				
					int depotID = -1;
					int nbNodes = nodes.size();
		
				//Create arrays to store the coordinates, the ids and the node types:
					
					double[][] coordinatesArray = new double[nbNodes][2];
					int[] originalIDArray = new int[nbNodes];
					NodeType[] typeArray = new NodeType[nbNodes];
					Node[] nodeArray = new Node[nbNodes];
					
				//Create three lists to store information for the CS's and the customers:
					
					List<Integer> csIDList = new ArrayList<>();
					List<Integer> customerIDList = new ArrayList<>();
					List<String> csTypeList = new ArrayList<>();
					
				//Iterate over all nodes:
					
					for (int n = 0; n < nbNodes; n++) {
						
						Element node = nodes.get(n);
						try {
							int id = node.getAttribute("id").getIntValue();
							int type = node.getAttribute("type").getIntValue();
							double x = Double.parseDouble(node.getChild("cx").getTextTrim());
							double y = Double.parseDouble(node.getChild("cy").getTextTrim());
							coordinatesArray[n][0] = x;
							coordinatesArray[n][1] = y;
							originalIDArray[n] = id;
							typeArray[n] = NodeType.convert(type);
							nodeArray[n] = new Node(id, "N" + id, typeArray[n]);
							switch (type) {
							case 0:
								if (depotID != -1) {
									throw new IllegalStateException("An instance can only have one depot");
								}
								depotID = id;
								break;
							case 1:
								customerIDList.add(id);
								break;
							case 2:
								csIDList.add(id);
								String idFunction = node.getChild("custom").getChild("cs_type").getTextTrim();
								csTypeList.add(idFunction);
								break;
							default:
								throw new IllegalStateException("Unknown type of node " + type);
							}
						} catch (DataConversionException ex) {
							ex.printStackTrace();
							throw new IllegalStateException("The node cannot be read" + node.toString());
						}
						
					}
					
					// Modify the created lists (to primitive/array):
					
					int[] csID = Utilities.toPrimitive(csIDList.toArray(new Integer[0]));
					int[] customerID = Utilities.toPrimitive(customerIDList.toArray(new Integer[0]));
					String[] csTypesS = csTypeList.toArray(new String[0]);
					
			// Data on the Fleet and the charging functions:
					
				// Recover the node:
					
					e = root.getChild("fleet").getChild("vehicle_profile");

				// Recover the speed factor and max travel time:
					
					double speedFactor = Double.parseDouble(e.getChild("speed_factor").getTextTrim());
					double tMax = Double.parseDouble(e.getChild("max_travel_time").getTextTrim());
					
				// Recover the node (custom):
					
					e = e.getChild("custom");
					
				// Store the consumption rate and the battery capacity:
					
					double consumptionRate = Double.parseDouble(e.getChild("consumption_rate").getTextTrim());
					double batteryCapacity = Double.parseDouble(e.getChild("battery_capacity").getTextTrim());
				
				// Create arrays to store information of the charging_functions:
					
					List<Element> functions = e.getChild("charging_functions").getChildren();
					int nbCSType = functions.size();
					String[] csTypeName = new String[nbCSType];
					int[] nbBreakPointsArray = new int[nbCSType];
					double[][][] piecewisePoints = new double[nbCSType][][];
					double sMax = 0;
					String type = null;
					HashMap<String, Integer> mapCSTypetoNewID = new HashMap<>();
					
				// Recover the information:
					
					for (int i = 0; i < nbCSType; i++) {
						Element function = functions.get(i);
						type = function.getAttribute("cs_type").getValue();
						csTypeName[i] = type;
						mapCSTypetoNewID.put(type, i);
						List<Element> breakPoints = function.getChildren();
						int nbBreakPoints = breakPoints.size();
						nbBreakPointsArray[i] = nbBreakPoints;
						piecewisePoints[i] = new double[nbBreakPoints][2];
						for (int b = 0; b < nbBreakPoints; b++) {
							Element breakpoint = breakPoints.get(b);
							try {
								piecewisePoints[i][b][1] = Double.parseDouble(breakpoint.getChild("battery_level").getTextTrim());
								piecewisePoints[i][b][0] = Double.parseDouble(breakpoint.getChild("charging_time").getTextTrim());
								if (piecewisePoints[i][b][0] > sMax) {
									sMax = piecewisePoints[i][b][0];
								}
							} catch (NumberFormatException e1) {
								e1.printStackTrace();
								throw new IllegalStateException("The breakpoint cannot be read" + breakpoint.toString());
							}
						}
					}
					
				// Convert the cs_type to the id
					
					int[] csTypes = new int[csTypesS.length];
					for (int k = 0; k < csTypesS.length; k++) {
						csTypes[k] = mapCSTypetoNewID.get(csTypesS[k]);
					}

			// Data on the processing time associated with each customer
					
				// Recover the child:
					
					e = root.getChild("requests");
					
				//Create a iterator and an array to store the processing times:
					
					Iterator<Element> itRequests = e.getChildren().iterator();
					double[] processingTimes = new double[nbNodes];
					
				//Iterate thorough the requests:
					
					while (itRequests.hasNext()) {
						Element request = itRequests.next();
						int nodeID;
						try {
							nodeID = request.getAttribute("node").getIntValue();
							double processingTime = Double.parseDouble(request.getChild("service_time").getTextTrim());
							processingTimes[nodeID] = processingTime;
						} catch (DataConversionException e1) {
							e1.printStackTrace();
							throw new IllegalStateException("The node ID is unkwnon " + request.toString());
						}
					}
			
		// Create the instance object:
					
			InstanceEVRPNLC instance = new InstanceEVRPNLC(name, decimals, nbNodes, customerID.length, csID.length,
							nbCSType, batteryCapacity, consumptionRate, tMax, sMax, speedFactor, nodeArray, typeArray, depotID,
							customerID, csID, processingTimes, csTypeName, csTypes, nbBreakPointsArray, piecewisePoints,
							coordinatesArray);
			
		// Return the instance:
			
			return instance;
					
	}
	
	/**
	 * This method runs a solution
	 * @return
	 */
	public Solution readSolution(String solutionName) {
		
		// Build the current path:
		
			String pathName =  GlobalParameters.SOLUTIONS_FOLDER+"/"+solutionName;
				
		// Read the xml file:
				
			//Tries to parse the document:
			
				Document mXMLData = XMLParser.parse(pathName);
				if (mXMLData == null) {
					return null;
				}
			
			// Data on the different nodes
				
				//Recovers the root element:
				
					Element root = mXMLData.getRootElement();
				
				//Recover the main parameters of the solution:
				
					String instance_id = root.getAttributes().get(0).getValue();
					String solver = root.getAttributes().get(1).getValue();
					String optimal = root.getAttributes().get(2).getValue();
					
				//Recovers the root element and creates an iterator for each children:
					
					Iterator<Element> itRoutes = root.getChildren().iterator();
					
				//Creates a new solution object:
				
					Solution sol = new Solution();
					sol.setInstance_id(instance_id);
					sol.setSolver(solver);
					sol.setOptimal(optimal);
					
				//Initializes the main attributes:
					
					Element eRoute, eNode;
					int nodeID;
					double waitingTime, chargingAmount, startingTime;
					List<Element> nodeChildren;
				
				//Iterates thorough each route:
					
					while (itRoutes.hasNext()) {
						RouteArray route = new RouteArray();
						eRoute = itRoutes.next();
						if(eRoute.getName().equals("route")) {
							route.setRouteID(Integer.parseInt(eRoute.getAttributes().get(0).getValue()));
							Iterator<Element>itNodesInRoute = eRoute.getChildren().iterator();//eRoute.getChild("sequence");
							while (itNodesInRoute.hasNext()) {
								eNode = itNodesInRoute.next();
								try {
									nodeID = eNode.getAttribute("id").getIntValue();
									nodeChildren = eNode.getChildren();
									if (!nodeChildren.isEmpty()) {
										waitingTime = Double.parseDouble(eNode.getChild("wait").getTextTrim());
										chargingAmount = Double.parseDouble(eNode.getChild("charge").getTextTrim());
										route.insert(nodeID, waitingTime, chargingAmount);
									} else {
										route.insert(nodeID, 0, 0);
									}
								} catch (DataConversionException | NumberFormatException e) {
									e.printStackTrace();
								}
							}
							startingTime = 0;//Double.parseDouble(eRoute.getChild("start_time").getTextTrim());
							route.setStartTime(startingTime);
							sol.addRoute(route);
						}else {
							String cputime = eRoute.getChild("cputime").getValue();
							Element specs = eRoute.getChild("machine");
							String cpu = specs.getChild("cpu").getValue();
							String cores = specs.getChild("cores").getValue();
							String ram = specs.getChild("ram").getValue();
							String language = specs.getChild("language").getValue();
							String os = specs.getChild("os").getValue();
						
							sol.setCores(cores);
							sol.setCpu(cpu);
							sol.setRam(ram);
							sol.setLanguage(language);
							sol.setOs(os);
							sol.setCputime(cputime);
						}
						
					}
		
			
		//Returns the solution:
				
			return sol;
	}
	
	
	/**
	 * @return the a
	 */
	public int getA() {
		return A;
	}

	/**
	 * @param a the a to set
	 */
	public void setA(int a) {
		A = a;
	}

	/**
	 * @return the b
	 */
	public int getB() {
		return B;
	}

	/**
	 * @param b the b to set
	 */
	public void setB(int b) {
		B = b;
	}

	/**
	 * @return the c
	 */
	public int getC() {
		return C;
	}

	/**
	 * @param c the c to set
	 */
	public void setC(int c) {
		C = c;
	}

	/**
	 * @return the d
	 */
	public String getD() {
		return D;
	}

	/**
	 * @param d the d to set
	 */
	public void setD(String d) {
		D = d;
	}

	/**
	 * @return the e
	 */
	public int getE() {
		return E;
	}

	/**
	 * @param e the e to set
	 */
	public void setE(int e) {
		E = e;
	}
	
	
}
