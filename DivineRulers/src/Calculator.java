import java.awt.Color;

public class Calculator {
	
	public static double force(CelestialObjects a, CelestialObjects b) {
		
		double dist = a.pos.dist(b.pos);
		double f = Constant.G * a.getMass() * b.getMass() / Math.pow(dist, 2);
		 return f;
	}
	
	
	public static double acce(CObject obj, double f) {
		double a = f/obj.weight;
		return a;
	}
	public static void main(String[] args) {
		CelestialObjects sun = new CelestialObjects("Sun", 1.989e30, 695.8e3);
        sun.setColor(Color.yellow);
        sun.setPosition(0, 0, 0);

        CelestialObjects saturn = new CelestialObjects("Saturn", 568.34e24, 58232.0);
	        saturn.setOrbitingParent(sun);
	        saturn.setEccentricity(0.0565);
	        saturn.setPeriod(10759.22);
	        saturn.setSemiMajor(9.53707032);
	        saturn.setDistance(9.139);
	        saturn.setPosition(351254886446.44037, -1345585527058.3767, -570922577597.4592);
	        double f = Calculator.force(sun, saturn);
	        System.out.println(f);
	}
}
