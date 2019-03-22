package gui;

import gui.Orbiter;
import utils.Constant;

public class Calculator {
	
	public static double force(Orbiter a, Orbiter b) {
		
		double dist = a.getPos().dist(b.getPos());
		double f = Constant.G * a.getMass() * b.getMass() / Math.pow(dist, 2);
		 return f;
	}
	
	
	
	
	public static void main(String[] args) {
	}
}
