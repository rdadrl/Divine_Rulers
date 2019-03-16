package Util;

public class Vector {
	public static double epsilon = 1e-10;
	private double x;
	private double y;
	private double z;
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Vector(Vector other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}
	/*
	 * add two vectors to each other
	 */
	public Vector add(Vector other) {
		return new Vector(x + other.x, y + other.y, z + other.z);
	}
	/*
	 * substract two vectors
	 */
	public Vector substract(Vector other) {
		return new Vector(x - other.x, y - other.y, z - other.z);
	}
	/*
	 * scale the vector with a constant factor 
	 */
	public Vector scale(double c) {
		return new Vector(c * x, c * y, c * z);
	}
	/*
	 * calculate the dot product of two vectors
	 */
	public double dot(Vector other) {
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
	public Vector unit() {
		return scale(1/norm());
	}
	
	/*
	 * calculate distance between two vectors
	 */
	public double dist(Vector other) {
		Vector v = substract(other);
		return v.length();
	}
	public static double dist(Vector u, Vector w) {
		return u.dist(w);
	}
	/*
	 * check if two vectors are orthogonal
	 */
	public boolean isOrthogonal(Vector other) {
		return Math.abs(dot(other)) < epsilon;
	}
	public static boolean isOrthogonal(Vector u, Vector w) {
		return u.isOrthogonal(w);
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(x);
		sb.append(", "); 
		sb.append(y);
		sb.append(", ");
		sb.append(z);
		sb.append("]");
		return sb.toString();
	}
	
	public static void main(String[] args) {
		Vector v = new Vector(-5 + Math.random() * 10, -5 + Math.random() * 10, -5 + Math.random() * 10);
		Vector w = new Vector(Math.random() * 10, Math.random() * 10, Math.random() * 10);
		System.out.println("v: " + v);
		System.out.println("w: " + w);
		System.out.println("v + w: " + v.add(w));
		System.out.println("v - w: " + v.substract(w));
		System.out.println("norm(v): " + v.norm());
		System.out.println("dot(v, w): " + v.dot(w));
		System.out.println("dist(v, w): " + v.dist(w));
		
	}

}
