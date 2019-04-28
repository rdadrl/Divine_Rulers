package gui;

import java.util.HashMap;

import utils.Vector3D;

public class Planet implements Orbiter {
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
		this.acce = new Vector3D(0,0,0);
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
	
	
	
	public void interact(Orbiter[] others) {
		//the final time is one hour later
		
		
		
		
		
//		if(this.name == "earth" && Constant.cnt < 100) {
//			System.out.println("pos " + this.name + ": " + this.pos);
//			System.out.println("acce " + this.name + ": " + this.acce.length() + this.acce);
//			System.out.println("velocity " + this.name + ": " + this.velocity.length() + this.velocity);
//		}
//		Vector3D pos = this.pos;
//		Vector3D ka1 = acce;
//		Vector3D kv1 = this.velocity;
//		
//		this.pos = pos.add(kv1.scale(0.5*dt));
//		Vector3D ka2 = this.totalAcceleration(others);
//		Vector3D kv2 = this.velocity.add(ka1.scale(0.5*dt));
//		
//		this.pos = pos.add(kv2.scale(0.5*dt));
//		Vector3D ka3 = this.totalAcceleration(others);
//		Vector3D kv3 = this.velocity.add(ka2.scale(0.5*dt));
//		
//		this.pos = pos.add(kv3.scale(1.0*dt));
//		Vector3D ka4 = this.totalAcceleration(others);
//		Vector3D kv4 = this.velocity.add(ka3.scale(1.0 * dt));
//		
//		this.pos = pos;
//		Vector3D ka = ka1.add(ka2.scale(2)).add(ka3.scale(2)).add(ka4).scale(1.0/6); 
//		Vector3D kv = kv1.add(kv2.scale(2)).add(kv3.scale(2)).add(kv4).scale(1.0/6);
//		
//		this.acce = ka;
//		this.velocity = this.velocity.add(ka.scale(dt));
////			this.pos = this.pos.add(kv.scale(dt));
//		this.pos = this.pos.add(kv.scale(dt));
//		Constant.cnt++;
		
		
	}
				
					
				//euler method
//				Vector3D posB = other.getPos();
//				Vector3D deltaS = posB.substract(this.pos);
//				double r = deltaS.length();
//				double r3 = r * r * r;
//				acce = acce.add(deltaS.scale(Constant.G * other.getMass() / r3));
			
//		double inv_r3 = 1.0 / (r * r * r);
	
//	}
	

	public void update() {
//		pos = pos.add(velocity.scale(dt));
//	    velocity = velocity.add(acce.scale(dt));
//	    System.out.println(pos);
	}

}
