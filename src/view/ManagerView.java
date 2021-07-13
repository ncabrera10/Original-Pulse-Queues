package view;

import java.io.File;

import dataStructures.PulseGraph;
import model.PulseAlgorithm;

/**
 * This class manages the view of the program
 */
public class ManagerView 
{
	/**
	 * Constant to manage the number of data shown  in console
	 */
	public static final int N = 20;

	
	/**
	 * Manager of the view
	 */
	public ManagerView() {

	}

	
	/**
	 * Console's Menu
	 */
	public void printMenu() {
		System.out.println("-------------------------Pulse Algorithm-----------------------------");
		System.out.println("1. Run the original pulse - Version Cabrera et. al. (2020)");
		System.out.println("2. Run the pulse with acceleration strategies - Version Cabrera et. al. (2020)");
		
		System.out.println("11. Exit");
		System.out.println("Choose the algorithm you want to use, then press enter: (Ej., 1):");

	}

	/**
	 * Method to print in console a message
	 * @param message to print
	 */
	public void printMessage(String mensaje) {
		System.out.println(mensaje);
	}
	
	
	/**
	 * Method to print Pulse Algorithm Results. 
	 * @param  pulse runned.
	 */
	public void printResults(PulseAlgorithm pulse){
		
		System.out.println("-----------Main results------------");		
		System.out.println("Instance: "+pulse.instanc);
		System.out.println("Network: "+ pulse.networkName);
		System.out.println("Destiny: "+pulse.destiny);
		System.out.println("Time limit: "+pulse.network.TimeC);
		System.out.println("Time star: "+pulse.network.TimeStar);
		System.out.println("Initial Primal Bound: "+ pulse.InitialPrimalBound);
		System.out.println("Final Primal Bound: "+pulse.network.PrimalBound);
		System.out.println("Computational time: "+ pulse.computationalTime);
		
		if(pulse.forwardDirection){
			System.out.println("Direction: Forward");
		}else{
			System.out.println("Direction: Backward");
		}
	
		System.out.println("------------------------------------");
		
	}


}
