package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JPanel;

import utils.Vector2D;
import utils.Vector3D;

public class Canvas extends JPanel {
	
	private int _WIDTH = Constant.CANVASWIDTH;
	private int _HEIGHT = Constant.CANVASHEIGHT;
	private int centerX = Constant.CANVASCENTERX;
	private int centerY = Constant.CANVACENTERY;
	double arrowX;
	double arrowY;
	Planet earth;
	Planet sun;
	Planet mars;
	Planet saturn;
	Planet moon;
	Planet jupiter;
	Planet[] planets;
	Planet ball;
	Calendar date;
	int cnt = 0;
	long passing = 0;
	long days = 0;
	boolean running = false;
	public Canvas() throws IOException {

		setPreferredSize(new Dimension(Constant.CANVASWIDTH, Constant.CANVASHEIGHT));
		
		date = Calendar.getInstance();
		date.set(Calendar.YEAR, 2019);
		date.set(Calendar.MONTH, 3);
		date.set(Calendar.DAY_OF_MONTH, 14);
		
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
		moon = new Planet(new Vector3D(-1.469961550052274E+11, 2.139416217572848E+10, -2.665896077882964E+07), 
				new Vector3D(-5.628284545172739E+03, -2.920876030457305E+04, 6.182609716986498E01), 
				7.349e22, "moon");
		jupiter = new Planet(new Vector3D(-2.452456365019889E+3, -7.584121180836378E+11, 8.637457754131973E+09),
				new Vector3D(1.228561476070495E+04, -3.406289621808893E+03, -2.607238800858853E2), 
				1898.13e24, "jupiter");
		
				 
		
		planets = new Planet[]{sun, earth, 
				mars, saturn, moon, jupiter, 
				ball
				};
	
	
	}
	
	public String formatDate(Calendar d) {
		return pad(d.get(Calendar.DATE)) + "/" + pad(d.get(Calendar.MONTH)) + "/" + d.get(Calendar.YEAR); 
	}
	
	public String pad(int n) {
		if(n < 10)
			return "0" + n;
		return ""+ n;
	}
	
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.blue);
		g2.fillRect(0, 0, _WIDTH, _HEIGHT);
		g2.setColor(Color.white);
		
		
		long days = passing/Constant.ONEDAY;
//		System.out.println(date);
		long dt = date.getTimeInMillis()+ passing * 1000;
		Calendar d = Calendar.getInstance();
		d.setTimeInMillis(dt);
		
		g2.drawString(formatDate(d), 20, 20);
		g2.drawString("days: " + days, 20, 40);
//		if(ball != null) {
//			Vector2D v = ball.getPos().toScreen();
////			System.out.println(ball.getPos());
//			g2.setColor(Color.DARK_GRAY);
//			 Ellipse2D.Double obj = new Ellipse2D.Double(v.getX(), v.getY(), 10, 10);
//			 g2.fill(obj);
//		}
		for(int i = 0; i < planets.length; i++) {
			if(planets[i] == null)
				break;
			 Vector2D v = (planets[i].getPos()).toScreen();
			 Ellipse2D.Double obj = new Ellipse2D.Double(v.getX(), v.getY(), 10, 10);
			 g2.setColor(Color.yellow);
			 g2.fill(obj);
			 g2.setColor(Color.white);
			 g2.drawString(planets[i].getName(), (int)v.getX(), (int)v.getY());
			 if(planets[i].equals(earth) && !running) {
				 Point from = new Point((int)v.getX()+5, (int)v.getY() + 5);
				 Point to = new Point((int)(from.getX() + arrowX), (int)from.getY());
				 Point to1 = new Point((int)from.x, (int)(from.y-arrowY));
				 Point comb = new Point((int)(from.getX()+arrowX), (int)(from.getY()-arrowY));
				 g2.setColor(Color.CYAN);
				 drawArrowLine(g, (int)from.x, (int)from.y, (int)comb.x, (int)comb.y, 10, 4);
				 g2.setColor(Color.RED);
				 drawArrowLine(g, (int)from.x, (int)from.y, (int)to.x, (int)to.y,10, 4);
				 drawArrowLine(g, (int)from.x, (int)from.y, (int)to1.x, (int)to1.y,10, 4);
			 }
		}
	}

	
	
	private void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2, int d, int h) {
	    int dx = x2 - x1, dy = y2 - y1;
	    double D = Math.sqrt(dx*dx + dy*dy);
	    double xm = D - d, xn = xm, ym = h, yn = -h, x;
	    double sin = dy / D, cos = dx / D;

	    x = xm*cos - ym*sin + x1;
	    ym = xm*sin + ym*cos + y1;
	    xm = x;

	    x = xn*cos - yn*sin + x1;
	    yn = xn*sin + yn*cos + y1;
	    xn = x;

	    int[] xpoints = {x2, (int) xm, (int) xn};
	    int[] ypoints = {y2, (int) ym, (int) yn};

	    g.drawLine(x1, y1, x2, y2);
	    g.fillPolygon(xpoints, ypoints, 3);
	}

	


	public double[] transform(Vector3D v) {
		return new double[] { Constant.epsilon * v.getX() + centerX, Constant.epsilon * v.getY() + centerY };
	}

	

}
