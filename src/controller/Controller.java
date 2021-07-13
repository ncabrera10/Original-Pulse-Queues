package controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import java.io.File;

import model.Manager;
import model.PulseAlgorithm;
import view.ManagerView;


/**
 * This class controlls both the view and the models. 
 * From this class the program in runned and controled. 
 */
public class Controller {

	/**
	 * View Object
	 */
	private ManagerView view;


	/**
	 * Model Object
	 */
	private model.Manager model;

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
		Scanner sc = new Scanner(System.in);
		boolean fin = false;
		//Controller controller = new Controller();
		int option = -1;
		boolean numeroEncontrado = false;
		boolean esSatisfactorio;

		long startTime;
		long endTime;
		long duration;

		while(!fin)
		{
			view.printMenu();
			// Para tener que reiniciar el programa si no se da una opcion valida
			while (!numeroEncontrado){
				try {
					option = sc.nextInt();
					numeroEncontrado = true;
				} catch (InputMismatchException e) {
					System.out.println("Esa no es una opcion valida");
					view.printMenu();
					sc = new Scanner(System.in);
				}
			} numeroEncontrado = false;

			try { // Este try se usa para no tener que reiniciar el programa en caso de que 
				// ocurra un error pequenio al ejecutar como ingresar mal la fecha  

				switch(option)
				{

				//Case when the user presses 1
				case 1:
					view.printMessage("Choose a direction for the original pulse:");
					view.printMessage("1. Run the pulse on the forward direction");
					view.printMessage("2. Run the pulse on the backward direction");
					int busqueda = Integer.parseInt(sc.next());
					view.printMessage("Choose the number of labels (Q):");
					int numLabels = Integer.parseInt(sc.next());
					view.printMessage("Choose the instance:");
					int instance = Integer.parseInt(sc.next());
					
					PulseAlgorithm originalPulse = null;
					if(busqueda == 1){
						originalPulse = model.runPulse(true,numLabels,instance);
					}else if(busqueda == 2){
						originalPulse = model.runPulse(false,numLabels,instance);
					}else{}
					view.printResults(originalPulse);
					break;
					//Case when the user presses 
				case 2:
					view.printMessage("Choose a direction for the pulse with acceleration strategies:");
					view.printMessage("1. Run the pulse on the forward direction");
					view.printMessage("2. Run the pulse on the backward direction");
					int busqueda2 = Integer.parseInt(sc.next());
					view.printMessage("Choose the number of labels (Q):");
					int numLabels2 = Integer.parseInt(sc.next());
					view.printMessage("Tightness factor (k) for Constraint:");
					view.printMessage("Note: If k = 0, the default value for the TimeC of the txt file Config.txt is used");
					view.printMessage("Note2: TimeC = k*(MaxTimeSource - MinTimeSource)+MinTimeSource ");
					double tightness = Double.parseDouble(sc.next());
					view.printMessage("Pulse queueing -  Maximum Depth:");
					int depth= Integer.parseInt(sc.next());
					view.printMessage("Choose the instance:");
					int instance2 = Integer.parseInt(sc.next());
					
					
					
					PulseAlgorithm acceleratedPulse = null;
					if(busqueda2 == 1){
						acceleratedPulse = model.runPulseAccelerated(true, numLabels2, tightness, depth,instance2);
					}else if(busqueda2 == 2){
						acceleratedPulse = model.runPulseAccelerated(false,numLabels2, tightness,depth,instance2);
					}else{}
					view.printResults(acceleratedPulse);
					break;


				case 11:
					fin=true;
					sc.close();
					break;
				}
			} catch(Exception e) { // Si ocurrio un error al ejecutar algun metodo
				e.printStackTrace(); System.out.println("Something happen. We recommend you to start over");}
		}
	}

}

