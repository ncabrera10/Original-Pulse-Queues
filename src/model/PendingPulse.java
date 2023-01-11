package model;

/**
 * This class represents the pending pulses
 */
public class PendingPulse {

	/**
	 * Node id on which the pulse is actually on.
	 */
	private int NodeID;

	/**
	 * Pending pulse weights
	 */
	
	private int[] pendingWeights;
	
	/**
	 * False if the pulse is already treated.
	 */
	private boolean notTreated;
	
	/**
	 * Sort criteria
	 */
	private int sortCriteria;
	
	public PendingPulse(int id, int[] partialWeights) {
		NodeID = id;
		pendingWeights = new int[2];
		pendingWeights[0] = partialWeights[0];
		pendingWeights[1] = partialWeights[1];
		notTreated = true;
		sortCriteria = 0;
	}
	
	
	public int getNodeID() {
		return NodeID;
	}

	public void setNodeID(int nodeID) {
		NodeID = nodeID;
	}

	public int getDist() {
		return pendingWeights[1];
	}

	public void setDist(int dist) {
		this.pendingWeights[1] = dist;
	}

	public int getTime() {
		return pendingWeights[0];
	}

	public void setTime(int time) {
		this.pendingWeights[0] = time;
	}

	public boolean getNotTreated() {
		return notTreated;
	}

	public void setNotTreated(boolean notTreated) {
		this.notTreated = notTreated;
	}
	
	public void setSortCriteria(int sortCriteria) {
		this.sortCriteria = sortCriteria;
	}
	public int getSortCriteria() {
		return sortCriteria;
	}

}
