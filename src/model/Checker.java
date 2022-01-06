package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dataStructures.Fleet;
import dataStructures.Graph;

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
	
	/**
	 * 
	 */
	private String instancePath;
	
	public Checker(int se,int a,int b,int c,String d, int e) {
		
		//Initialize main parameters of the instance:
		
			A = a;
			B = b;
			C = c;
			D = d;
			E = e;
			if(se == 1) {
				instancePath = "Montoya_2017";
			}
		
		//Read the instance:
		
			Graph G = this.readGraph();
			Fleet F = this.readFleet();
		
		//Reads the solution:
			
	}
	
	public Fleet readFleet() {
		
		Fleet F = null;
		
		//
		try {
			File currentFile = new File("./Instances/"+instancePath+"/"+"tc"+A+"c"+B+"s"+C+"c"+D+E+".xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(currentFile);
			doc.getDocumentElement().normalize();
					
		//Read fleet information:
			
			// Creates a new fleet:
					
				F = new Fleet();
					
			//Recover the main node:
				
				Node node_fleet = doc.getElementsByTagName("fleet").item(0);

			//Recover all the child nodes:
				
				NodeList node_fleet_ch = node_fleet.getChildNodes();
				
			//Iterates through them:
				
				for (int i = 0; i < node_fleet_ch.getLength(); i++) {
					Node node = node_fleet_ch.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element) node;
				
						NodeList node_custom_ch = element.getElementsByTagName("custom").item(0).getChildNodes();
						
						int consum_rate = Integer.parseInt(node_custom_ch.item(1).getChildNodes().item(0).getNodeValue());
						int battery_cap = Integer.parseInt(node_custom_ch.item(3).getChildNodes().item(0).getNodeValue());
						Hashtable<String,ArrayList<Double>> ht_batteryLevels = new Hashtable<String,ArrayList<Double>>();
						Hashtable<String,ArrayList<Double>> ht_chargingTimes = new Hashtable<String,ArrayList<Double>>();
						
						NodeList node_functions_ch = node_custom_ch.item(5).getChildNodes();
						for (int k = 0; k < node_functions_ch.getLength(); k++) {
							Node nodef = node_functions_ch.item(k);
							if (nodef.getNodeType() == Node.ELEMENT_NODE) {
								NodeList node_charging_ch = nodef.getChildNodes();
								String function_type = "";
								ArrayList<Double> batteryLevels = new ArrayList<Double>();
								ArrayList<Double> chargingTimes = new ArrayList<Double>();
								
								for (int j = 0; j < node_charging_ch.getLength(); j++) {
									Node node2 = node_charging_ch.item(j);
									
									if (node2.getNodeType() == Node.ATTRIBUTE_NODE) {
										function_type = node2.getNodeValue();
									}
									if (node2.getNodeType() == Node.ELEMENT_NODE) {
										Element element2 = (Element) node2;
										batteryLevels.add(Double.parseDouble(getValue("battery_level",element2)));
										chargingTimes.add(Double.parseDouble(getValue("charging_time",element2)));
										
									}
								}
								ht_batteryLevels.put(function_type, batteryLevels);
								ht_chargingTimes.put(function_type, chargingTimes);
							}
						}

					
						
						F.addVehicle(Integer.parseInt(node.getAttributes().item(0).getNodeValue()),Integer.parseInt(getValue("departure_node", element)),Integer.parseInt(getValue("arrival_node", element)),Integer.parseInt(getValue("max_travel_time", element)),Integer.parseInt(getValue("speed_factor", element)),consum_rate,battery_cap,ht_batteryLevels,ht_chargingTimes);
						
						
					}
					
				}
		
		
			

		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("There was a problem reading the instance");
		}
		
		return(F);
	}
	
	/**
	 * This method reads the graph (nodes)
	 * @return
	 */
	public Graph readGraph() {
		
		Graph G = null;
		
		try {
			File currentFile = new File("./Instances/"+instancePath+"/"+"tc"+A+"c"+B+"s"+C+"c"+D+E+".xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(currentFile);
			doc.getDocumentElement().normalize();
			
			//Read network information:
			
				//Creates a new graph:
			
					G = new Graph();
				
				//Recover the main node:
			
					Node node_network = doc.getElementsByTagName("network").item(0);
					
				//Recover all the child nodes:
					
					NodeList node_network_ch = node_network.getChildNodes().item(1).getChildNodes();
					
				//Add each child node to the original graph:
					
					for (int i = 0; i < node_network_ch.getLength(); i++) {
						Node node = node_network_ch.item(i);
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							Element element = (Element) node;
							if(Integer.parseInt(node.getAttributes().item(1).getNodeValue()) == 2) {
								NodeList node_custom_ch = node.getChildNodes().item(5).getChildNodes();
								String func_type = node_custom_ch.item(1).getChildNodes().item(0).getNodeValue();
								G.addNode(Integer.parseInt(node.getAttributes().item(0).getNodeValue()),Integer.parseInt(node.getAttributes().item(1).getNodeValue()),Double.parseDouble(getValue("cx", element)),Double.parseDouble(getValue("cy", element)),func_type);
							
							}else {
								G.addNode(Integer.parseInt(node.getAttributes().item(0).getNodeValue()),Integer.parseInt(node.getAttributes().item(1).getNodeValue()),Double.parseDouble(getValue("cx", element)),Double.parseDouble(getValue("cy", element)),"-1");
								
							}
							
						}
						
					}
					
				//Type of distance and # of decimals.
					
					String typeOfDis = node_network.getChildNodes().item(3).getNodeName();
					int numOfDec = Integer.parseInt(node_network.getChildNodes().item(5).getChildNodes().item(0).getNodeValue());
					G.setParamAdd(typeOfDis,numOfDec);
					
			//Adds requests information:
					
					//Recover the main node:
						
						Node node_requests = doc.getElementsByTagName("requests").item(0);

					//Recover all the child nodes:
						
						NodeList node_requests_ch = node_requests.getChildNodes();
						
					//Iterates through them:
						
						for (int i = 0; i < node_requests_ch.getLength(); i++) {
							Node node = node_requests_ch.item(i);
							if (node.getNodeType() == Node.ELEMENT_NODE) {
								Element element = (Element) node;
								int id_act = Integer.parseInt(node.getAttributes().item(0).getNodeValue());
								double serv_act = Double.parseDouble(getValue("service_time", element));
								G.getNodes().get(id_act).setServ_time(serv_act);
							}
							
						}
		}
		catch(Exception e) {
			System.out.println("Problems reading the graph");
		}
		
		return G;
		
	}
	
	/**
	 * Get the value of a given tag
	 * @param tag
	 * @param element
	 * @return
	 */
	private static String getValue(String tag, Element element) {
		
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		
		Node node = (Node) nodes.item(0);
		
		return node.getNodeValue();
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
