package gui;

import utils.Vector3D;

public class Calculator {
	
	public static double force(Orbiter a, Orbiter b) {
		
		double dist = a.getPos().dist(b.getPos());
		double f = Constant.G * a.getMass() * b.getMass() / Math.pow(dist, 2);
		 return f;
	}
	
	
	public static Vector3D acceFrom(Vector3D pos, Orbiter other) {
		Vector3D vectorTo = other.getPos().substract(pos);
		double r = vectorTo.length();
		double r3 = r*r*r;
		Vector3D acce = vectorTo.scale(Constant.G * other.getMass()/r3);
		return acce;
	}
	
	public static Vector3D totalAcceleration(Vector3D pos, Orbiter self, Orbiter[] others) {
		Vector3D acceSum = new Vector3D();
		for(int i = 0; i < others.length; i++) {
			Orbiter other = others[i];
			if(!self.equals(other) && other != null) {
				acceSum = acceSum.add(acceFrom(pos, other));
			}
		}
		return acceSum;
	}
	
	public static Vector3D[] rk4(Vector3D p0, Vector3D v0, Vector3D a0, double dt, Orbiter self, Orbiter[] others) {
		Vector3D ka1 = a0;
		Vector3D kv1 = v0;
		
		Vector3D temp = p0.add(kv1.scale(0.5*dt));
		Vector3D ka2 = totalAcceleration(temp, self, others);
		Vector3D kv2 = v0.add(ka1.scale(0.5*dt));
		
		temp = p0.add(kv2.scale(0.5*dt));
		Vector3D ka3 = totalAcceleration(temp, self, others);
		Vector3D kv3 = v0.add(ka2.scale(0.5*dt));
		
		temp = p0.add(kv3.scale(dt));
		Vector3D ka4 = totalAcceleration(temp, self, others);
		Vector3D kv4 = v0.add(ka3.scale(dt));
		
		Vector3D ka = ka1.add(ka2.scale(2)).add(ka3.scale(2)).add(ka4).scale(1.0/6);
		Vector3D kv = kv1.add(kv2.scale(2)).add(kv3.scale(2)).add(kv4).scale(1.0/6);
		return new Vector3D[]{ka, kv};
	}
	
	
	public static void main(String[] args) {
	}
}
