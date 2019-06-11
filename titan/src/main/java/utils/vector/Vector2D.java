package utils.vector;

import java.awt.geom.Point2D;

public class Vector2D implements Vector<Point2D>{
	private double x;
	private double y;
	public static double epsilon = 1e-10;
	public Vector2D(double _x, double _y) {
		x = _x;
		y = _y;
	}
	public Vector2D(Vector3D v) {
		this(v.getX(), v.getY());
	}
	public Vector2D add(Vector<Point2D> _other) {
		Vector2D other = (Vector2D)(_other);
		return new Vector2D(this.x + other.x, this.y + other.y);
	}
	
	/*
	 * substract two vectors
	 */
	public Vector2D substract(Vector<Point2D> _other) {
		Vector2D other = (Vector2D)(_other);
		return new Vector2D(x - other.x, y - other.y);
	}
	/*
	 * scale the vector with a constant factor 
	 */
	public Vector2D scale(double c) {
		return new Vector2D(c * x, c * y);
	}
	public double dot(Vector<Point2D> _other) {
		Vector2D other = (Vector2D)(_other);
		return this.x * other.x + this.y; 
	}
	public double norm() {
		return Math.sqrt(dot(this));
	}
	public double length() {
		return norm();
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
	
	public double dist(Vector<Point2D> _other) {
		Vector2D other = (Vector2D)_other;
		return this.substract(other).length();
	}

	public Vector2D unit() {
		return scale(1/norm());
	}

	public boolean isOrthogonal(Vector<Point2D> _other) {
		Vector2D other = (Vector2D)_other;
		return Math.abs(dot(other)) < epsilon;
	}
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [x=" + this.getX() + ", y=" + this.getY() + "]";
	}
	public static void main(String[] args) {
		Vector2D v = new Vector2D(1,2);
		Vector2D v2 = v.add(new Vector2D(2,3));
		System.out.println(v2);
	}
}


