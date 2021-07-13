package sensitivity;

import java.io.IOException;

import model.Manager;
import model.PulseAlgorithm;
import view.ManagerView;

public class MainSensitivity {

	public static void main(String[] args) throws IOException, InterruptedException {
	
		int ini = 140; 	//Initial instance id
		int fin = 140;	//Final instance id
		int depth = 2;
		int numLabels = 3;
		double tightness = 0.1;
		
		//Create the manager's
		
		Manager model = new Manager();
		ManagerView view = new ManagerView();
		
		//Iterate:
		
		for(int i=ini;i<=fin;i++) {
			//Forward:
			PulseAlgorithm acceleratedPulse = model.runPulseAccelerated(true, numLabels, tightness, depth,i);
			view.printResults(acceleratedPulse);

			//Backward:
			PulseAlgorithm acceleratedPulse2 = model.runPulseAccelerated(false, numLabels, tightness, depth,i);
			view.printResults(acceleratedPulse2);
		}
					
				
	
	}
	
}
