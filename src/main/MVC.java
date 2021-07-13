package main;

import java.io.IOException;

import controller.Controller;
/**
 * Main Class
 * Starts the program
 */
public class MVC {
	/**
	 * MAIN METHODS
	 * THIS METHOS RUNS FIRST
	 * DO NOT MODIFY IT
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		Controller controler = new Controller();
		controler.run();
	}

}