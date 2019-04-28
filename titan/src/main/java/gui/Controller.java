package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JSpinner;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import utils.Vector3D;

public class Controller {
	
	int day = 0;
	public Controller(View view) {
		Canvas cvs = view.cvs;
		ControlPanel cp = view.cp;
		JSpinner s1 = view.cp.sSpeed1;
		JSpinner s2 = view.cp.sSpeed2;
		Planet[] planets = view.cvs.planets;
		utils.Date startDate = cvs.date;
		
		int dayToGameTime = 24 * 60 * 60/ (360 * 100);
		int fr = Constant.INTERVAL;
		double tf = 60 * 60 * 24; //24h
		//number of time step
		double dt = 60 * 60; //step size: 1h
		int n = (int)(tf/dt); //number of steps
		
		Vector3D initialV = new Vector3D(Constant.escapeV *1e3, Constant.escapeV*1e3, 0);
		Vector3D[][] kav = new Vector3D[planets.length][2]; 
		ActionListener lst = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for(int j = 0; j < n; j++) {
					for(int i = 0; i < planets.length; i++) {
						
						if(planets[i]!=null) {
							Planet planet = planets[i];
							Vector3D acc = Calculator.totalAcceleration(planet.pos, planet, planets);
							Vector3D[] ret = Calculator.rk4(planet.pos, planet.velocity, acc, dt, planet, planets);
							kav[i][0] = ret[0];
							kav[i][1] = ret[1];
						}
					}
					for(int i = 0; i < planets.length; i++) {
						if(planets[i] != null) {
							Planet planet = planets[i];
							planet.pos = planet.pos.add(kav[i][1].scale(dt));
							planet.velocity = planet.velocity.add(kav[i][0].scale(dt));
						}
					}
				}
//				if(cvs.ball!=null) {
//					cvs.ball.interact(planets);
//					cvs.ball.update();					
//				}
				/*
				 * update
				 */
//				for(int i = 0; i < planets.length; i++) {
//					if(planets[i]!=null)
//						planets[i].update();
//				}
				cvs.cnt++;
				cvs.day = (int)(cvs.cnt * dayToGameTime);
//				if(day != _day) {
//					cvs.date.add(Calendar.DATE, 1);
//					day = _day;
//				}
				cvs.repaint();
			}
		};

		
		view.cp.sSpeed1.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				double val = (Double)s1.getValue();
				cvs.arrowX = val;
				initialV.setX(val *1e3);
				cvs.repaint();
			}
		});
		
		view.cp.sSpeed2.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				double val = (Double)s2.getValue();
				cvs.arrowY = -val;
				initialV.setY(val*1e3);
				cvs.repaint();
			}
		});
		cp.sSpeed3.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				double val = (Double)s2.getValue();
				initialV.setZ(val*1e3);
			}
			
		});
		
		
		view.cp.bLaunch.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Timer t = new Timer(Constant.INTERVAL, lst);
				cvs.running = true;
				Planet earth = cvs.earth;
				
				Planet ball = new Planet(earth.getPos().add(new Vector3D(6.371e6,6.371e6,0)), initialV, 1000, "cannon");
				for(int i = 0; i <cvs.planets.length;i++) {
					if(cvs.planets[i] == null) {
						cvs.planets[i] = ball;
					}
				}
				System.out.println(ball.getVelocity());
				System.out.println(cvs.earth.getVelocity());
//				System.out.println(cvs.ball.getPos());
//				System.out.println(cvs.ball.getDepartureVelocity());
//				System.out.println(cvs.earth.getPos());
//				System.out.println(cvs.earth.getDepartureVelocity());
				t.start();
			}
			
		});
		
	}
}
