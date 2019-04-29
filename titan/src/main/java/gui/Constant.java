package gui;


public class Constant {
	public static final int AUPIXELRATIO = 30;
	public static final double epsilon = 1e-10;
	public static final double G = 6.67e-11;
	public static final double au = 1.496e11; //m
	public static final double inv_au = 1/au;
	public static final double scale = inv_au * AUPIXELRATIO;
	public static final double escapeV = 11.186;
	public static final int CANVASWIDTH = 1000;
	public static final int CANVASHEIGHT = 900;
	public static final int CANVASCENTERX = CANVASWIDTH/2;
	public static final int CANVACENTERY = CANVASHEIGHT/2;
	protected static final int FRAME = 20; //animation fps rate 
	public static final int ONEDAY = 24 * 60 * 60; //one day in seconds
	public static final int tf = 60 * 60 * 24; //final interpolation time: 24h in seconds
	public static final double dt = 60 * 60; //step size: 1h
	public static final int n = (int)(Constant.tf/Constant.dt); //number of steps
	public static int cnt = 0;
}
