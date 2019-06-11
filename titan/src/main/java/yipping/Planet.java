package yipping;


import utils.vector.Vector3D;

public class Planet implements Orbiter {
	public Vector3D pos;
	public Vector3D velocity;
	public double mass;
	public String name;
	public Vector3D acce;
	public double r;
	/*
	 * @param pos position unit m
	 * @param velocity unit m/s
	 * @param mass unit kg
	 * @param r radius in m
	 * @name planet name
	 */
	public Planet(Vector3D pos, Vector3D velocity, double mass, double r, String name) {
		this.name = name;
		this.pos = pos;
		this.velocity = velocity;
		this.mass = mass;
		this.acce = new Vector3D(0,0,0);
		this.r = r;
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
	
	public void setPos(Vector3D pos) {
		this.pos = pos;
	}
	
	public void setVel(Vector3D vel) {
		this.velocity = vel;
	}
	
}