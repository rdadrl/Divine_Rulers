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
		double df = 3600 * 24; 
		for(int i = 0; i < others.length; i++) {
			Orbiter other = others[i];
			if(!this.equals(other) && other!=null) {			
				//rk4
				//60s
				//iterate 60 for a final value after exact an hour
				double dt = df/20;
				for(int j = 0; j < 20; j++) {
//					if(Constant.cnt < 60) {
						System.out.println("pos " + this.name + ": " + this.pos);
						System.out.println("acce " + this.name + ": " + this.acce.length());
						System.out.println("velocity " + this.name + ": " + this.velocity.length());
						
//					}
					Vector3D acce = this.totalAcceleration(others);
					Vector3D ka1 = acce;
					Vector3D kv1 = this.velocity;
					Vector3D pos = this.pos;
					this.pos = pos.add(kv1.scale(1.0/2*dt));
					Vector3D ka2 = this.totalAcceleration(others);
					Vector3D kv2 = this.velocity.add(ka1.scale(1.0/2*dt));
					this.pos = pos.add(kv2.scale(1.0/2*dt));
					Vector3D ka3 = this.totalAcceleration(others);
					Vector3D kv3 = this.velocity.add(ka2.scale(1.0/2 * dt));
					this.pos = pos.add(kv3.scale(dt));
					Vector3D ka4 = this.totalAcceleration(others);
					Vector3D kv4 = this.velocity.add(ka3.scale(1.0 * dt));
					Vector3D ka = ka1.add(ka2.scale(2)).add(ka3.scale(2)).add(ka4).scale(1.0/6); 
					Vector3D kv = kv1.add(kv2.scale(2)).add(kv3.scale(2)).add(kv4).scale(1.0/6);
					this.acce = ka;
					this.velocity = kv;
					this.pos = pos;
					this.pos = this.pos.add(this.velocity.scale(dt));
					Constant.cnt++;
					
				}
				
//				HashMap<String, Vector3D> da = derivative(this.velocity, this.totalAcceleration(others), 0, others);
//			    HashMap<String, Vector3D> db = derivative(da.get("vel"), da.get("acc"), 0.5, others);
//			    HashMap<String, Vector3D> dc = derivative(db.get("vel"), db.get("acc"), 0.5, others);
//			    HashMap<String, Vector3D> dd = derivative(dc.get("vel"), dc.get("acc"), 1, others);
//			    Vector3D dav = da.get("vel");
//			    Vector3D dbv = db.get("vel");
//			    Vector3D dcv = dc.get("vel");
//			    Vector3D ddv = dd.get("vel");
//			    Vector3D vel = new Vector3D(1.0/6 * (dav.getX() + 2*(dbv.getX() + dcv.getX()) + ddv.getX()),
//			    		1.0/6 * (dav.getY() + 2*(dbv.getY() + dcv.getY()) + ddv.getY()),
//	    				1.0/6 * (dav.getZ() + 2*(dbv.getZ() + dcv.getZ()) + ddv.getZ())
//	    		);
//			    Vector3D daa = da.get("acc");
//			    Vector3D dba = db.get("acc");
//			    Vector3D dca = dc.get("acc");
//			    Vector3D dda = dd.get("acc");
//			    Vector3D acc = new Vector3D(1.0/6 * (daa.getX() + 2*(dba.getX() + dca.getX()) + dda.getX()),
//			    		1.0/6 * (daa.getY() + 2*(dba.getY() + dca.getY()) + dda.getY()),
//	    				1.0/6 * (daa.getZ() + 2*(dba.getZ() + dca.getZ()) + dda.getZ())
//	    		);
//			    this.pos = this.pos.add(vel);
//			    this.velocity = this.velocity.add(acc);
//			    System.out.println(this.pos);
//			    System.out.println(this.velocity);

				
				//euler method
//				Vector3D posB = other.getPos();
//				Vector3D deltaS = posB.substract(this.pos);
//				double r = deltaS.length();
//				double r3 = r * r * r;
//				acce = acce.add(deltaS.scale(Constant.G * other.getMass() / r3));
			}
//		double inv_r3 = 1.0 / (r * r * r);
		}
	}
	
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
