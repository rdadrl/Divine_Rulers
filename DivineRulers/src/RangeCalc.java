class RangeCalc {
	RangeCalc(double speed, double angleInDegrees) {
		double g, angleInRads, range; // declare three variables
		g = 9.8; // SI units
		angleInRads = angleInDegrees * Math.PI / 180;
		range = 2 * speed * speed *
		Math.sin(angleInRads) * Math.cos(angleInRads) / g;
		System.out.println("Range = " + range + " meters");
}
	public static void main(String[] arg) {
		new RangeCalc(20,45); // launch at 20 m/s, 45 degrees
	}
}
