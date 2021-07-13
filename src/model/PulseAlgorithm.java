package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import model.PendingPulse;
import dataStructures.DataHandler;
import dataStructures.DukqstraDist;
import dataStructures.DukqstraTime;
import dataStructures.FinalVertexPulse;
import dataStructures.PulseGraph;
import threads.ShortestPathTask;
import dataStructures.VertexPulse;



/**
 * This class stores the different types of pulses algorithms
 */
public class PulseAlgorithm {

	/**
	 * The name of the file (network)
	 */
	public String fileName;

	/**
	 * The network where the Original pulse will be running
	 */
	public PulseGraph network;

	/**
	 * Name of the network
	 */
	public String networkName;


	/**
	 * Initial Primal Bound
	 */
	public int InitialPrimalBound;


	/**
	 * Computational Time
	 */
	public double computationalTime;


	/**
	 * True if the pulse will run in the forward direction
	 * False if the pulse will run in the backward direction
	 */
	public boolean forwardDirection;


	public double tightness;
	
	
	public int destiny;

	public int instanc;

	/**
	 * Initialize the attributes of the pulse
	 */
	public PulseAlgorithm(){

		fileName = "";
		network = null;
		networkName = "";
		InitialPrimalBound = 0;
		computationalTime = 0;
		forwardDirection = false;
	}



	/**
	 * This method runs the original Pulse
	 */
	public void originalPulse(boolean pForwardDirection, int numLabels, int instance) throws IOException, InterruptedException {

		/*******************************************************************
		 ***************INITIALIZATION AND PRE PROCESSING*******************
		 *******************************************************************
		 */

		forwardDirection = pForwardDirection; //Direction of the pulse 
		fileName = "./instances/Config"+instance+".txt";  
		instanc = instance;
		
		String [] information = new String [6]; //6 lines of information in Config.txt
		information = readData();

		/**
		 * Creates a DataHandler and a Pulse Graph:
		 * 1) DataHandler: Keeps the most important information related to the graph
		 * DataHandler(numNodes, numArcs, source, lastNode, type, acronym)
		 * It also creates a graph given the information
		 */
		DataHandler dataA = dataHandlerAndPulseGraph(information);
		destiny = dataA.getLastNode();

		/**
		 * Sets the time constraint
		 * The value is given by the txt File
		 */
		network.SetConstraint(Double.parseDouble(information[3]));

		/**
		 * Runs the shortest path 
		 */
		SP(dataA,network);

		/**
		 * Initialization of Primal Bound, Destination, Best time
		 */
		InitialPrimalBound =network.getVertexByID(dataA.Source-1).getMaxDist();
		network.setPrimalBound(InitialPrimalBound);
		network.TimeStar = network.getVertexByID(dataA.Source-1).getMinTime();

		//Size of Q
		dataA.numLabels = numLabels;

		//Initial weights
		int[] initialWeights = new int[2];
		initialWeights[0] = 0;
		initialWeights[1] = 0;

		/*******************************************************************
		 ************************PULSE PROCEDURE******************************
		 *******************************************************************
		 */
		//Starts the clock
		double ITime = System.nanoTime(); 

		network.getVertexByID(dataA.Source-1).pulse(initialWeights);
		//Ends the clock
		double FTime = (System.nanoTime());


		/*******************************************************************
		 ************************ RESULTS ******************************
		 *******************************************************************
		 */
		networkName = information[0];
		computationalTime = (FTime-ITime)/1000000000;
	}



	/**
	 * This method runs the pulse with Acceleration Strategies
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void pulseAccelerationStrategies(boolean pForwardDirection, int numLabels, double tightness, int depth, int instance) throws IOException, InterruptedException{


		/*******************************************************************
		 ***************INITIALIZATION AND PRE PROCESSING*******************
		 *******************************************************************
		 */

		forwardDirection = pForwardDirection; //Direction of the pulse 
		fileName = "./instances/Config"+instance+".txt";   
		instanc = instance;
		
		String [] information = new String [6]; //6 lines of information in Config.txt
		information = readData();


		/**
		 * Creates a DataHandler and a Pulse Graph:
		 * 1) DataHandler: Keeps the most important information related to the graph
		 * DataHandler(numNodes, numArcs, source, lastNode, type, acronym)
		 * It also creates a graph given the information
		 */
		DataHandler dataA = dataHandlerAndPulseGraph(information);
		
		if(forwardDirection ) {
			destiny = dataA.getLastNode();
		}
		else {
			destiny = dataA.Source;
		}

		/**
		 * Runs the shortest path 
		 */
		SP(dataA,network);


		/**
		 * Method to determine the tightness factor 
		 */
		if(tightness == 0){
			/**
			 * Sets the time constraint
			 * The value is given by the txt File
			 */
			network.SetConstraint(Double.parseDouble(information[3]));

		}else{

			//Choose the tightness factor (This is optional)
			double k  = tightness;
			//k*(MaxTimeSource - MinTimeSource)+MinTimeSource 
			double timeC = k*(network.getVertexByID(dataA.Source-1).maxTime-network.getVertexByID(dataA.Source-1).minTime) + network.getVertexByID(dataA.Source-1).minTime;		
			network.SetConstraint(timeC);
			System.out.println(timeC);
			System.out.println(network.getVertexByID(dataA.Source-1).minTime + " - "+network.getVertexByID(dataA.Source-1).maxTime);
		}


		//Pulse algorithm parameters:

		//First primal bound

		InitialPrimalBound =network.getVertexByID(dataA.Source-1).getMaxDist();
		network.setDestiny(dataA.LastNode-1);
		network.setPrimalBound(InitialPrimalBound);
		network.TimeStar = network.getVertexByID(dataA.Source-1).getMinTime();

		//Depth limit and num labels
		dataA.numLabels = numLabels;
		/**
		 * Acceleration strategy: Network Depth
		 */
		network.depth = depth;

		//Initial weights

		int[] initialWeights = new int[2];
		initialWeights[0] = 0;
		initialWeights[1] = 0;


		/*******************************************************************
		 ************************PULSE PROCEDURE******************************
		 *******************************************************************
		 */

		//Starts the clock
		double ITime = System.nanoTime(); 

		//Check if we already have found the optimal solution
		if(network.getVertexByID(dataA.Source-1).getMaxTime() <= network.TimeC) {

			//Set the primal bound and the time star
			network.setPrimalBound(network.getVertexByID(dataA.Source-1).getMinDist());
			network.TimeStar = network.getVertexByID(dataA.Source-1).getMaxTime();

		}

		else {
			//Send the first pulse
			network.getVertexByID(dataA.Source-1).pulseWithQueues(initialWeights,0);

			//Store the number of pulses that are in the queue
			int pendingPulses = dataA.pendingQueue.size();

			//While we have pending pulses we must explore them
			while(pendingPulses > 0) {

				//Recovers the last pulse (and removes it):
				PendingPulse p = dataA.pendingQueue.remove(pendingPulses-1);
				p.setNotTreated(false);

				//The pendingPulse weights:
				initialWeights[0] = p.getTime();
				initialWeights[1] = p.getDist();

				//Resumes the pulse
				if(network.getVertexByID(p.getNodeID()).getMinDist() + initialWeights[1] < network.PrimalBound) {
					network.getVertexByID(p.getNodeID()).pulseWithQueues(initialWeights,0);
				}

				//Updates the global queue size (How many are left)
				pendingPulses = dataA.pendingQueue.size();
			}

		}

		//Ends the clock
		double FTime = (System.nanoTime());

		/*******************************************************************
		 ************************ RESULTS ******************************
		 *******************************************************************
		 */

		networkName = information[0];
		computationalTime = (FTime-ITime)/1000000000;

	}



	/**
	 * This method reads the data from a .txt file
	 * It read the file after the name of fileName
	 * Initializes: #Arcs, #Nodes, TimeConstraint, StartNode, EndNode
	 * @return the important information for the pulse
	 */
	private String[] readData() throws IOException{

		/*******************************************************************
		 ************************DATA READING*******************************
		 *******************************************************************
		 */

		File testFile = new File(fileName); //Gets the information from Config.txt

		BufferedReader bufRedr = new BufferedReader(new FileReader(testFile)); //BR to read the file

		String actLine = null; //Active line

		String [] information = new String [6]; //6 lines of information in Config.txt

		int rowA = 0;
		int colA = 0;

		while((actLine = bufRedr.readLine()) != null && rowA < 6){	 //Reads the file Config.txt
			String [] info = actLine.split(":");
			information[rowA] = info[1];   //Stores the important information
			rowA++;
		}


		return information;

	}


	/**
	 * Creates a DataHandler and a Pulse Graph:
	 * 1) DataHandler: Keeps the most important information related to the graph
	 * DataHandler(numNodes, numArcs, source, lastNode, type, acronym)
	 * It also creates a graph given the information
	 * @param information about the graph: #Arcs, #Nodes, TimeConstraint, StartNode, EndNode (Config.txt)
	 */
	public DataHandler dataHandlerAndPulseGraph(String[] information) throws NumberFormatException, IOException{

		DataHandler dataA = null;

		if(!forwardDirection) {

			//Backward direction: 
			dataA = new DataHandler(Integer.parseInt(information[2]),Integer.parseInt(information[1]),Integer.parseInt(information[5]),Integer.parseInt(information[4]),1,information[0]);	
			dataA.ReadDimacsB();
			network = createGraph(dataA);	

		}else if(forwardDirection) {

			//Forward direction: 
			dataA = new DataHandler(Integer.parseInt(information[2]),Integer.parseInt(information[1]),Integer.parseInt(information[4]),Integer.parseInt(information[5]),1,information[0]);
			dataA.ReadDimacsF();
			network = createGraph(dataA);	
		}
		return dataA;
	}


	/**
	 * This method creates a network
	 * @param data
	 * @return the final graph
	 */
	private static PulseGraph createGraph(DataHandler data) {
		int numNodes = data.NumNodes;
		PulseGraph Gd = new PulseGraph(numNodes);
		for (int i = 0; i < numNodes; i++) {
			if(i!=(data.LastNode-1)){
				Gd.addVertex(new VertexPulse(i) );
			}
		}
		FinalVertexPulse vv = new FinalVertexPulse(data.LastNode-1);
		Gd.addFinalVertex(vv);
		for(int i = 0; i <data.NumArcs; i ++){
			Gd.addWeightedEdge( Gd.getVertexByID(data.Arcs[i][0]), Gd.getVertexByID(data.Arcs[i][1]),data.Distance[i],data.Time[i], i);			
		}
		return Gd;
	}

	/**
	 * This method runs a shortest path for cost and times
	 * @param data
	 * @param network
	 * @throws InterruptedException
	 */
	private static void SP(DataHandler data, PulseGraph network) throws InterruptedException {
		// Create two threads and run parallel SP for the initialization		
		Thread tTime = new Thread();
		Thread tDist = new Thread();
		// Reverse the network and run SP for distance and time 
		DukqstraDist spDist = new DukqstraDist(network, data.LastNode-1);
		DukqstraTime spTime = new DukqstraTime(network, data.LastNode-1);
		tDist = new Thread(new ShortestPathTask(1, spDist, null));
		tTime = new Thread(new ShortestPathTask(0, null,  spTime));
		tDist.start();
		tTime.start();
		tDist.join();
		tTime.join();
	}



	


}
