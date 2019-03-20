package solarsystem;

import utils.Constant;
import utils.Vector3D;

public class Planet implements Orbiter{
	Vector3D pos;
	Vector3D velocity;
	double mass;
	String name;
//	double ax;
//	double ay;
//	double az;
	Vector3D acce;
	public Planet(Vector3D pos, Vector3D velocity, double mass, String name) {
		this.name = name;
		this.pos = pos;
		this.velocity = velocity;
		this.mass = mass;
	}
	public Vector3D getVelocity() {
		return velocity;
	}
	public Vector3D getPos() {
		return pos;
	}
	public double getMass() {
		return mass;
	}
	public String getName() {
		return name;
	}
	
	public void interact(Orbiter other) {
		double dt = 1 * 24 * 3600;
		Vector3D posB = other.getPos();
		Vector3D deltaS = posB.substract(this.pos);
		double r = deltaS.length();
//		double inv_r3 = 1.0 / (r * r * r);
//		
	    double r3 = r * r * r;

	    
	    acce = deltaS.scale(Constant.G * other.getMass() / r3);
	    update(dt);
	
	}

	public void update(double dt) {
	    velocity = velocity.add(acce.scale(dt));
	    pos = pos.add(velocity.scale(dt));
//	    System.out.println(pos);
	}

}
