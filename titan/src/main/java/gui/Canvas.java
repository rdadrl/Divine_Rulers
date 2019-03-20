package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.util.Calendar;

import javax.swing.JPanel;
import javax.swing.Timer;

import solarsystem.CelestialObjects;
import solarsystem.Planet;
import solarsystem.Planets;
import solarsystem.SolarSystem;
import utils.Constant;
import utils.Date;
import utils.Vector2D;
import utils.Vector3D;

public class Canvas extends JPanel {
	private int _WIDTH = 1200;
	private int _HEIGHT = 900;

	private int centerX = _WIDTH / 2;
	private int centerY = _HEIGHT / 2;
	
	Planet earth;
	Planet sun;
	Planet mars;
	Planet saturn;
	Planet[] planets;
	Date date;
	int cnt = 0;
	public Canvas() throws IOException {

		setPreferredSize(new Dimension(_WIDTH, _HEIGHT));
		
		
		date = new Date(2019, 3, 14); 
		
		earth = new Planet(
				new Vector3D(-1.471633868509571E+11, 2.104852097662997E+10, -2.126817645682022E+05),//pos m
				new Vector3D(-4.692621973980529E+03, -2.960544243231639E+04, 6.108724178606195E-01),//velocity m/s
				5.97219e24, 
				"earth");//mass
		
		sun = new Planet(new Vector3D(0, 0, 0), new Vector3D(0, 0, 0), 1.989e30, "sun");
		
		mars = new Planet(new Vector3D(4.118375362597144E+10, 2.272780613990308E+11, 3.751746077609673E+09),
				new Vector3D(-2.292571145415813E+04, 6.379431960955054E+03, 6.962003264745111E+02), 6.4171e24, "mars");
		saturn = new Planet(new Vector3D(3.478584940740049E+11, -1.463678221158519E+12, 1.159733711534047E+10), 
				new Vector3D(8.879609027903770E+03, 2.200312023010623E+03, -3.910344211873641E02),
				5.6834e26, "saturn");
		
		planets = new Planet[]{sun, earth, mars, saturn};
		
		ActionListener lst = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for(int i = 0; i < planets.length; i++) {
					planets[i].interact(planets);
				}
				for(int i = 0; i < planets.length; i++) {
					planets[i].update();
				}
				
//				System.out.println(earth.getPos());
				repaint();
			}

		};

		Timer t = new Timer(50, lst);
		t.start();

	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.drawString(date.getDateString(), 20, 20);
		date.add(Calendar.DATE, 1);
		g2.drawString("days: " + ++cnt, 20, 40);
		
		for(int i = 0; i < planets.length; i++) {
			 Vector2D v = new Vector2D(planets[i].getPos().scale(Constant.scale)).add(new Vector2D(centerX, centerY));
			 Ellipse2D.Double obj = new Ellipse2D.Double(v.getX(), v.getY(), 20, 20);
			 g2.draw(obj);
			 g2.drawString(planets[i].getName(), (int)v.getX(), (int)v.getY());
		}
	}


	public double[] transform(Vector3D v) {
		return new double[] { Constant.epsilon * v.getX() + centerX, Constant.epsilon * v.getY() + centerY };
	}

	

}
