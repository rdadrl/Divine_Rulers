package xperiment.yipping;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JSpinner;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import utils.vector.Vector3D;

public class Controller {
	private Vector3D initialV;
	public Controller(View view) {
		Canvas cvs = view.cvs;
		ControlPanel cp = view.cp;
		JSpinner s1 = view.cp.sSpeed1;
		JSpinner s2 = view.cp.sSpeed2;
		Planet[] planets = view.cvs.planets;
		
		initialV = new Vector3D(Constant.escapeV *1e3, Constant.escapeV*1e3, 0);
//		double u = Constant.G*planets[1].getMass();
//		double v = Math.sqrt(u/planets[1].r);
//		initialV = new Vector3D(v, 0, 0);
		Vector3D[][] dfs = new Vector3D[planets.length][2];

		ActionListener lst = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					switch (Constant.mode) {
						case "euler":
							Calculator.eulerSim(planets, dfs, Constant.dt,Constant.n);
						case "RK4":
							Calculator.rk4sim(planets, dfs, Constant.dt, Constant.n);					
					}
					
					if(cvs.ball != null)
						cvs.collided = Calculator.checkCollision(cvs.ball, planets);
					
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
				
				Planet ball = new Planet(earth.getPos().add(new Vector3D(6.371e6,6.371e6,6.371e6)), initialV, 60000, 100, "cannon");
				cvs.ball = ball;
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
