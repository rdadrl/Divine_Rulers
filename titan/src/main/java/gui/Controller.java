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
	
	public Controller(View view) {
		Canvas cvs = view.cvs;
		ControlPanel cp = view.cp;
		JSpinner s1 = view.cp.sSpeed1;
		JSpinner s2 = view.cp.sSpeed2;
		Planet[] planets = view.cvs.planets;

		Vector3D initialV = new Vector3D(Constant.escapeV *1e3, Constant.escapeV*1e3, 0);
		Vector3D[][] kav = new Vector3D[planets.length][2]; 
		ActionListener lst = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
					Calculator.rk4sim(planets, kav, Constant.dt, Constant.n);
					cvs.passing += Constant.tf;
					cvs.repaint();
				}
				
//				if(cvs.ball!=null) {
//					cvs.ball.interact(planets);
//					cvs.ball.update();					
//				}
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
				Timer t = new Timer(Constant.FRAME, lst);
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
			
				t.start();
			}
			
		});
		
	}
}
