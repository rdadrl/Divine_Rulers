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
	
	public void interact(Orbiter[] others) {
		//the final time is one hour later
		double tf = 360;
		//number of time step 
		double step = 20;
		//dt is chosen as fixed as an average of deltatime/deltastep  
		double dt = tf/step;
		for(int i = 0; i < others.length; i++) {
			Orbiter other = others[i];
			if(!this.equals(other) && other!=null) {			
			//rk4
				//initial acceleration 
				Vector3D acce = this.totalAcceleration(others);
				this.acce = acce;
				if(this.name == "earth" && Constant.cnt < 100) {
					System.out.println("pos " + this.name + ": " + this.pos);
					System.out.println("acce " + this.name + ": " + this.acce.length() + this.acce);
					System.out.println("velocity " + this.name + ": " + this.velocity.length() + this.velocity);
				}
				Vector3D pos = this.pos;
				Vector3D ka1 = acce;
				Vector3D kv1 = this.velocity;
				
				this.pos = pos.add(kv1.scale(0.5*dt));
				Vector3D ka2 = this.totalAcceleration(others);
				Vector3D kv2 = this.velocity.add(ka1.scale(0.5*dt));
				
				this.pos = pos.add(kv2.scale(0.5*dt));
				Vector3D ka3 = this.totalAcceleration(others);
				Vector3D kv3 = this.velocity.add(ka2.scale(0.5*dt));
				
				this.pos = pos.add(kv3.scale(1.0*dt));
				Vector3D ka4 = this.totalAcceleration(others);
				Vector3D kv4 = this.velocity.add(ka3.scale(1.0 * dt));
				
				this.pos = pos;
				Vector3D ka = ka1.add(ka2.scale(2)).add(ka3.scale(2)).add(ka4).scale(1.0/6); 
				Vector3D kv = kv1.add(kv2.scale(2)).add(kv3.scale(2)).add(kv4).scale(1.0/6);
				
				this.acce = ka;
				this.velocity = this.velocity.add(ka.scale(dt));
//					this.pos = this.pos.add(kv.scale(dt));
				this.pos = this.pos.add(kv.scale(dt));
				Constant.cnt++;
			}
		}
	}
				
					
				//euler method
//				Vector3D posB = other.getPos();
//				Vector3D deltaS = posB.substract(this.pos);
//				double r = deltaS.length();
//				double r3 = r * r * r;
//				acce = acce.add(deltaS.scale(Constant.G * other.getMass() / r3));
			
//		double inv_r3 = 1.0 / (r * r * r);
	
//	}
	
	public Vector3D acceFrom(Orbiter other) {
		Vector3D vectorTo = other.getPos().substract(this.pos);
		double r = vectorTo.length();
		double r3 = r*r*r;
		Vector3D acce = vectorTo.scale(Constant.G * other.getMass()/r3);
		return acce;
	}
	public Vector3D totalAcceleration(Orbiter[] others) {
		Vector3D acceSum = new Vector3D();
		for(int i = 0; i < others.length; i++) {
			Orbiter other = others[i];
			if(!this.equals(other) && other != null) {
				acceSum = acceSum.add(acceFrom(other));
			}
		}
		return acceSum;
	}
	public HashMap<String, Vector3D> derivative(Vector3D vel, Vector3D acc, double dt, Orbiter[] others) {
		   
		        Vector3D deltapos = vel.scale(dt);
		        Vector3D deltavel = acc.scale(dt);
		        this.pos = this.pos.add(deltapos);
		        
		        Vector3D acceleration = this.totalAcceleration(others);
		        Vector3D velocity = this.velocity.add(deltavel);
		        this.pos = this.pos.substract(deltapos);
		        HashMap<String, Vector3D> map = new HashMap<>();
		        map.put("vel", velocity);
		        map.put("acc", acceleration);
		        return map;
		    
	}

	public void update() {
//		pos = pos.add(velocity.scale(dt));
//	    velocity = velocity.add(acce.scale(dt));
//	    System.out.println(pos);
	}

}
