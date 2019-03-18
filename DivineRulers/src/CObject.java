
public class CObject {
	public double x, y, z, weight, radius;
	Vector v;
 public CObject(double _x, double _y, double _z, double _weight, double _radius) {
	 x = _x;
	 y = _y;
	 z = _z;
	 v = new Vector(x, y, z);
	 weight = _weight;
	 radius = _radius;
 }
}
