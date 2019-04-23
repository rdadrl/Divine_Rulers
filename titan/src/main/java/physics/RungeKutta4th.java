package physics;


public class RungeKutta4th {
	/*
	 * @param f function to approximate
	 * @param t0 initial time
	 * @param w0 initial value y(t0)
	 * @param h time step
	 */
	public static double eval(Fvt f, double t0, double w0, double h) {
		double k1 = h * f.solve(t0, w0);
		double k2 = h * f.solve(t0 + 1.0/2*h, w0 + 1.0/2 * k1);
		double k3 = h * f.solve(t0 + 1.0/2*h, w0 + 1.0/2 * k2);
		double k4 = h * f.solve(t0 + h, w0 + k3);
		double k = 1.0/6.0*(k1 + 2.0*k2 + 2.0*k3 + k4);
		return w0 + k;
	}
	
	public static double solve(Fvt f, double w0, double t0, double tf, double h) {
		
		int iter = (int) ((tf-t0)/h);
		
		for(int i = 0; i < iter; i++) {
			w0 = eval(f, t0, w0, h);
			t0 = t0 + h;
			System.out.println("t = " + t0 + ", y = " + w0);
		}
		return w0;
	}
	public static void main(String[] args) {
		double h = 0.2;
		double t0 = 0;
		double w0 = 0;
		double tf = 1;
		
		Fvt f = new Fvt<Double>() {
			public double solve(double t0, double y0) {
				return Math.exp(-t0) - Math.pow(y0, 2);
			}
		};
		
		RungeKutta4th.solve(f, w0, t0, tf, h);
	}
}

