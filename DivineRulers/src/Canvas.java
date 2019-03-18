import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Canvas extends JPanel {
	private int _WIDTH = 1200;
	private int _HEIGHT = 900;

	private int centerX = _WIDTH / 2;
	private int centerY = _HEIGHT / 2;
	CelestialObjects[] objs;
	SolarSystem sys;

	public Canvas() {

		sys = new SolarSystem();
		sys.createSolarSystem();

		objs = sys.getObjects();
		setPreferredSize(new Dimension(_WIDTH, _HEIGHT));

		ActionListener lst = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				repaint();

			}

		};

		Timer t = new Timer(100, lst);
		t.start();

	}

	public void paintComponent(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLUE);

		g2.fillRect(0, 0, getWidth(), getHeight());

//		paintCelestialObject(g, sys.getSun(), Color.yellow);
		paintCelestialObjects(g, objs, Color.red);

//		for(int i = 0; i < objs.length; i++) {
//			double au = objs[i].getDistanceAU();
//			double dist = au * Constant.AUPIXELRATIO;
//			double angle = Math.random() * Math.PI * 2;
//			double x = Math.cos(angle);
//			double y = Math.sin(angle);
//			
//		}

//		paintCObject(g, a, Color.cyan);
//		paintCObject(g, b, Color.darkGray);

	}

	public void paintCelestialObject(Graphics g, CelestialObjects obj, Color color) {
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(obj.color);

	}

	public void paintCelestialObjects(Graphics g, CelestialObjects[] objs, Color color) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);

//		for(int i = 0; i < objs.length; i++) {
//			CelestialObjects a = objs[i];
//			double norm = a.pos.norm();
//			for(int j = 0; j < objs.length; j++) {
//				if(i != j) {
//					//
//				}
//			}
//		}
		CelestialObjects a = sys.getSaturn();
		CelestialObjects sun = sys.getSun();
		double x = 0;
		double y = 0;
		double times = 29 * 365 * 24 * 60 * 60;
		double norm = a.pos.norm();
		double F = Calculator.force(sun, a);
		System.out.println(F);
		double fx = F * a.getPosition().x / norm;
		double fy = F * a.getPosition().y / norm;
		double fz = F * a.getPosition().z / norm;
		double ax = fx / a.getMass() * times;
		double ay = fy / a.getMass() * times;
		double az = fz / a.getMass() * times;
		System.out.println(fx + " " + fy + " " + fz);
		System.out.println(ax + " " + ay + " " + az);
		Vector pos = a.pos.add(new Vector(ax, ay, az));
		a.setPosition(pos);
//		double angle = Math.random() * Math.PI * 2;
//		double l = obj.getDistanceAU() * Constant.AUPIXELRATIO;
//		double x = l * Math.cos(angle) + centerX;
//		double y = l * Math.sin(angle) + centerY;
		x = pos.x * Constant.epsilon + centerX;
		y = pos.y * Constant.epsilon + centerY;
		System.out.println(x + " " + y);

		Ellipse2D _obj = new Ellipse2D.Double(x, y, 20, 20);
		g2.drawString(a.getName(), (int) x, (int) y);
		g2.draw(_obj);

		Ellipse2D _sun = new Ellipse2D.Double(centerX, centerY, 20, 20);
		g2.drawString("Sun", centerX, centerY);
		g2.draw(_sun);
	}

	public double[] transform(Vector v) {
		return new double[] { Constant.epsilon * v.x + centerX, Constant.epsilon * v.y + centerY };
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
