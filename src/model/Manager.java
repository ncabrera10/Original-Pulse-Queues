package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;

import javax.xml.parsers.*;
import org.xml.sax.*;

import model.PulseAlgorithm;


/**
 * Class to manage the different algorithms
 */
public class Manager {


	public Manager() throws IOException, InterruptedException{

	}


	/**
	 * Runs the original Pulse
	 * @param pForwardDirection true if the direction will be forwards. False if backward
	 */
	public PulseAlgorithm runPulse(boolean pForwardDirection, int numLabels,int instance) throws IOException, InterruptedException{
		PulseAlgorithm pulso = new PulseAlgorithm();
		pulso.originalPulse(pForwardDirection, numLabels,instance);
		return pulso;
	}


	/**
	 * Runs the Pulse with some acceleration strategies
	 * 	@param pForwardDirection true if the direction will be forwards. False if backward
	 */
	public PulseAlgorithm runPulseAccelerated (boolean pForwardDirection, int numLabels, double tightness, int depth,int instance) throws IOException, InterruptedException{
		
		PulseAlgorithm pulso = new PulseAlgorithm();
		pulso.pulseAccelerationStrategies(pForwardDirection, numLabels, tightness, depth,instance);
		return pulso;
	}



}
