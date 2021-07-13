/**
 * This is a class that holds all the relevant data for an instance.
 * 
 * Ref.: Lozano, L. and Medaglia, A. L. (2013). 
 * On an exact method for the constrained shortest path problem. Computers & Operations Research. 40 (1):378-384.
 * DOI: http://dx.doi.org/10.1016/j.cor.2012.07.008 
 * 
 * 
 * @author L. Lozano & D. Duque
 * @affiliation Universidad de los Andes - Centro para la Optimizaci�n y Probabilidad Aplicada (COPA)
 * @url http://copa.uniandes.edu.co/
 * 
 */
package dataStructures;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

import model.PendingPulse;


/**
 * This class holds all information (data) relevant for an instance. 
 */
public class DataHandler {


	/**
	 * Number of arcs
	 */
	public int NumArcs;
	/**
	 * Number of nodes
	 */
	public static int NumNodes;
	/**
	 * Destination node
	 */
	public int LastNode;
	/**
	 * Source node
	 */
	public int Source;
	/**
	 * All the arcs in the network stored in a vector where Arcs[i][0]= Tail for arc i and Arcs[i][1]= Head for arc i 
	 */
	public static int[][] Arcs;
	/**
	 * The distance attribute for any arc i
	 */
	public static int[] Distance;
	/**
	 * The time attribute for any arc i
	 */
	public static int[] Time;
	/**
	 * Data structure for storing the graph
	 */
	private PulseGraph Gd;

	private int networkId;

	private String acro;

	public static int numLabels;

	public static Random r = new Random(0);

	/**
	 * Pulse Queue
	 */
	public static ArrayList<PendingPulse> pendingQueue;




	/**
	 * Read data from an instance
	 * @param numNodes
	 * @param numArcs
	 * @param sourceNode
	 * @param lastNode
	 * @param netId
	 */
	public DataHandler(int numNodes, int numArcs, int sourceNode, int lastNode, int netId, String acronym) {

		//Retrieves the information of the instance
		NumArcs = numArcs;
		NumNodes = numNodes;
		LastNode = lastNode;
		Source = sourceNode;
		networkId = netId;
		acro = acronym;

		//Creates the list of arcs. A list of distances and a list of times   --- Serian independientes del sentido de la red ! 
		Arcs = new int[numArcs][2];
		Distance = new int[numArcs];
		Time = new int[numArcs];

		//Creates the graph
		Gd = new PulseGraph(NumNodes);
		pendingQueue = new ArrayList<PendingPulse>();
	}


	/**
	 * This procedure creates the nodes for the graph
	 */
	public void upLoadNodes(){
		// All nodes are VertexPulse except the final node
		for (int i = 0; i < NumNodes; i++) {
			if(i!=(LastNode-1)){
				Gd.addVertex(new VertexPulse(i) ); //Primero lo creo, y luego lo meto. El id corresponde al n�mero del nodo
			}
		}
		// The final node is a FinalVertexPulse 
		FinalVertexPulse vv = new FinalVertexPulse(LastNode-1);
		Gd.addFinalVertex(vv);
	}

	/**
	 * This procedure returns a graph
	 * @return the graph
	 */
	public PulseGraph getGd()
	{
		return Gd;
	}

	/**
	 * This procedure reads data from a data file in DIMACS format
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public void ReadDimacsF() throws NumberFormatException, IOException {
		File file2 = null;
		file2 = new File("./networks/"+acro);
		//file2 = new File("C:/Users/nick0/OneDrive/Documentos/Universidad/BP/Networks/"+acro);
		
		BufferedReader bufRdr2 = new BufferedReader(new FileReader(file2));
		String line2 = null;
		int row2 = 0;
		while ((line2 = bufRdr2.readLine()) != null && row2 < NumArcs) {
			String[] Actual = line2.split(" ");
			Arcs[row2][0] = Integer.parseInt(Actual[0])-1;
			Arcs[row2][1] =  Integer.parseInt(Actual[1])-1;
			Distance[row2] = Integer.parseInt(Actual[2]);
			Time[row2] = Integer.parseInt(Actual[3]);
			row2++;
		}
	}

	/**
	 * This procedure reads data from a data file in DIMACS format
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public void ReadDimacsB() throws NumberFormatException, IOException {
		File file2 = null;
		file2 = new File("./networks/"+acro);
		//file2 = new File("C:/Users/nick0/OneDrive/Documentos/Universidad/BP/Networks/"+acro);
				
		BufferedReader bufRdr2 = new BufferedReader(new FileReader(file2));
		String line2 = null;
		int row2 = 0;
		while ((line2 = bufRdr2.readLine()) != null && row2 < NumArcs) {
			String[] Actual = line2.split(" ");
			Arcs[row2][1] = Integer.parseInt(Actual[0])-1;
			Arcs[row2][0] =  Integer.parseInt(Actual[1])-1;
			Distance[row2] = Integer.parseInt(Actual[2]);
			Time[row2] = Integer.parseInt(Actual[3]);
			row2++;
		}
	}


	public static int binarySearch(PendingPulse p, ArrayList<PendingPulse> labels) {
		double cScore = p.getSortCriteria();
		boolean cond = true;
		int l = 0; //izq
		int r = labels.size()-1; //der
		int m = (int) ((l + r) / 2); //medio
		double mVal = 0;
		//		System.out.println("Inicia :"+labels.get(l).getSortCriteria()+"\t"+labels.get(m).getSortCriteria()+"\t"+labels.get(r).getSortCriteria());
		if(labels.size() == 1){
			return 0;
		}else{
			mVal = labels.get(m).getSortCriteria();
		}
		while (cond) {
			//			 System.out.println("murio");
			if (r - l > 1) {
				if (cScore > mVal) {
					r = m;
					m = (int) ((l + r) / 2);
				} else if (cScore < mVal) {
					l = m;
					m = (int) ((l + r) / 2);
				} else if (p.getNodeID()>labels.get(m).getNodeID()){
					r = m;
					m = (int) ((l + r) / 2);
				} else if (p.getNodeID()<labels.get(m).getNodeID()){
					l = m;
					m = (int) ((l + r) / 2);
				}  else if (p.getTime()>labels.get(m).getTime()){
					r = m;
					m = (int) ((l + r) / 2);
				} else if (p.getTime()<labels.get(m).getTime()){
					l = m;
					m = (int) ((l + r) / 2);
				} else {
					return m;
				}
				mVal = labels.get(m).getSortCriteria();
			} else {
				cond = false;

				///if (p.getNodeID()==labels.get(r).getNodeID() && p.getSortCriteria() == labels.get(r).getSortCriteria() && p.getTime() == labels.get(r).getTime()){
				//return r;
				//}else if (p.getNodeID()==labels.get(l).getNodeID() && p.getSortCriteria() == labels.get(l).getSortCriteria()&& p.getTime() == labels.get(l).getTime()){
				//return l;
				//}

				if (p.equals(labels.get(r))){
					return r;
				}else if (p.equals(labels.get(l))){
					return l;
				}

			}
		}
		return -1;

	}


	public static void addPendingPulse_DOrder(PendingPulse p, ArrayList<PendingPulse>labels){

		double cScore = p.getSortCriteria();
		boolean cond = true;
		int l = 0; //Por izquierda
		int r = labels.size(); //Por derecha
		int m = (int) ((l + r) / 2); //La mitad
		double mVal = 0;
		if(labels.size() == 0) {
			labels.add(p);
			return;
		}
		else if(labels.size()  == 1) {
			mVal = labels.get(m).getSortCriteria();
			if(cScore == mVal) {
				if(p.getNodeID() == labels.get(m).getNodeID()) {
					labels.add(p.getTime()>labels.get(m).getTime()?0:1,p);
				}
				else {
					labels.add(p.getNodeID()>labels.get(m).getNodeID()?0:1,p);
				}
			}else {
				labels.add(cScore>mVal?0:1,p);
			}
		}
		else {
			mVal = labels.get(m).getSortCriteria();
		}
		while(cond) {
			if (r - l > 1) {
				if (cScore > mVal) {
					r = m;
					m = (int) ((l + r) / 2);
				} else if (cScore < mVal) {
					l = m;
					m = (int) ((l + r) / 2);
				} else if (p.getNodeID()>labels.get(m).getNodeID()){
					r = m;
					m = (int) ((l + r) / 2);
				} else if (p.getNodeID()<labels.get(m).getNodeID()){
					l = m;
					m = (int) ((l + r) / 2);
				}  else if (p.getTime()>labels.get(m).getTime()){
					r = m;
					m = (int) ((l + r) / 2);
				} else if (p.getTime()<labels.get(m).getTime()){
					l = m;
					m = (int) ((l + r) / 2);
				}  
				else {
					labels.add(m, p);
					return;
				}
				mVal = labels.get(m).getSortCriteria();
			} else {
				cond = false;
				if(l == m ){
					if (cScore == mVal){
						if(p.getNodeID()==labels.get(m).getNodeID()){
							labels.add(p.getTime()>labels.get(m).getTime()?l:l+1,p);
						}else{
							labels.add(p.getNodeID()>labels.get(m).getNodeID()?l:l+1,p);
						}						
					}else{
						labels.add(cScore>mVal?l:l+1,p);
					}
				}else if (r == m){
					if (cScore == mVal){
						if(p.getNodeID()==labels.get(m).getNodeID()){
							labels.add(p.getTime()>labels.get(m).getTime()?r:Math.min(r+1, labels.size()),p);
						}else{
							labels.add(p.getNodeID()>labels.get(m).getNodeID()?r:Math.min(r+1, labels.size()),p);
						}
					}else{
						labels.add(cScore>mVal?r:Math.min(r+1, labels.size()),p);
					}
				}else
				{
					System.err.println("LABEL, addLabel ");
				}
				return;
			}


		}

	}


	/**
	 * @return the lastNode
	 */
	public int getLastNode() {
		return LastNode;
	}


	/**
	 * @param lastNode the lastNode to set
	 */
	public void setLastNode(int lastNode) {
		LastNode = lastNode;
	}


	
}

