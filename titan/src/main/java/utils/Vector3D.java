package utils;

public class Vector3D implements Vector<Point3D>{
	public static double epsilon = 1e-10;
	private double x;
	private double y;
	private double z;

    public Vector3D() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

	public Vector3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Vector3D(Vector3D other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}
	/*
	 * add two vectors to each other
	 */
	public Vector3D add(Vector<Point3D> _other) {
		Vector3D other = (Vector3D)_other;
		return new Vector3D(x + other.x, y + other.y, z + other.z);
	}
	/*
	 * substract two vectors
	 */
	public Vector3D substract(Vector<Point3D> _other) {
		Vector3D other = (Vector3D)_other;
		return new Vector3D(x - other.x, y - other.y, z - other.z);
	}
	/*
	 * scale the vector with a constant factor 
	 */
	public Vector3D scale(double c) {
		return new Vector3D(c * x, c * y, c * z);
	}

	/*
	 * calculate the dot product of two vectors
	 */
	public double dot(Vector<Point3D> _other) {
		Vector3D other = (Vector3D)_other;
		return x * other.x + y * other.y + z * other.z;
	}
	/*
	 * calculate the norm of the vector
	 */
	public double norm() {
		return Math.sqrt(dot(this));
	}
	/*
	 * calculate the length of the vector, same as norm
	 */
	public double length() {
		return norm();
	}
	/*
	 * calculate the unit vector
	 * @return the unit vector to return
	 */
	public Vector3D unit() {
		return scale(1/norm());
	}
	
	/*
	 * calculate distance between two vectors
	 */
	public double dist(Vector<Point3D> _other) {
		Vector3D other = (Vector3D)_other;
		Vector3D v = substract(other);
		return v.length();
	}
	public static double dist(Vector3D u, Vector3D w) {
		return u.dist(w);
	}
	/*
	 * check if two vectors are orthogonal
	 */
	public boolean isOrthogonal(Vector<Point3D>_other) {
		Vector3D other = (Vector3D)_other;
		return Math.abs(dot(other)) < epsilon;
	}
	/*
	public Vector rotateXDeg(double theta) {
		return rotateXRad(Math.toRadians(theta));
	}


	public Vector rotateXRad(double theta) {
		double cosTheta = Math.cos(theta);
		double sinTheta = Math.sin(theta);
		return new Vector(
				this.dot(new Vector3D(1, 0, 0)),
				this.dot(new Vector3D(0, cosTheta, -1*sinTheta)),
				this.dot(new Vector3D(0, sinTheta, cosTheta))
		);
	}*/


	public static boolean isOrthogonal(Vector3D u, Vector3D w) {
		return u.isOrthogonal(w);
	}

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "Vector3D{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    public static void main(String[] args) {
		Vector3D v = new Vector3D(-5 + Math.random() * 10, -5 + Math.random() * 10, -5 + Math.random() * 10);
		Vector3D w = new Vector3D(Math.random() * 10, Math.random() * 10, Math.random() * 10);
		System.out.println("v: " + v);
		System.out.println("w: " + w);
		System.out.println("v + w: " + v.add(w));
		System.out.println("v - w: " + v.substract(w));
		System.out.println("norm(v): " + v.norm());
		System.out.println("dot(v, w): " + v.dot(w));
		System.out.println("dist(v, w): " + v.dist(w));
		
//		Vector3D z = new Vector3D(-1.471633868509571E+08, 2.104852097662997E+07, -2.126817645682022E+02);
		Vector3D z = new Vector3D(-4.054425385519177E+05, -2.557910226152136E+06, 5.277937690315753E+01);
		System.out.println(29.78);
		System.out.println(z.length()/24/3600);
		
	}


}
