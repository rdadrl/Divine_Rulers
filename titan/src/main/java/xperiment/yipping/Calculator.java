package xperiment.yipping;

import utils.vector.Vector3D;

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
	
	public static Vector3D[] deriv(Vector3D[] y, Orbiter self, Orbiter[] others, double dt) {
		Vector3D[] doty = new Vector3D[2];
		doty[0] = y[1].scale(dt);
		doty[1] = totalAcceleration(y[0], self, others).scale(dt);
	    return doty;
	}
	
	public static Vector3D[] predict(Vector3D[] y, Vector3D[] k, double coff) {
		Vector3D[] p = new Vector3D[2];
		for(int i = 0; i < y.length; i++) {
			p[i] = y[i].add(k[i].scale(coff));
		}
		return p;
	}
	
	/*
	 * Calculate the final coefficient based on rk4
	 * @param p0 initial position
	 * @param v0 initial velocity
	 * @param a0 initial acceleration
	 * @param dt delta t
	 * @param self the planet on which calculation is based on
	 * @param others the list of planets 
	 * @return Vector3D[]{ka, kv}
	 */
	public static Vector3D[] rk4(Vector3D p0, Vector3D v0, double dt, Orbiter self, Orbiter[] others) {
		Vector3D[] y = new Vector3D[] {p0, v0};
	
		Vector3D[] k1 = deriv(y, self, others, dt);
		
		Vector3D[] yp = predict(y, k1, 0.5);
		Vector3D[] k2 = deriv(yp, self, others, dt);
		
		yp = predict(y, k2, 0.5);
		Vector3D[] k3 = deriv(yp, self, others, dt);
		
		yp = predict(y, k3, 1);
		Vector3D[] k4 = deriv(yp, self, others, dt);
			
		Vector3D[] coff = new Vector3D[2];
		for(int i = 0; i < k1.length; i++) {
			coff[i] = k1[i].add(k2[i].add(k3[i]).scale(2)).add(k4[i]).scale(1.0/6);
		}
		return coff;
		
	}
	
	/*
	 * simulation with RK4
	 * @param planets planets 
	 * @param kav the dotv and dots, calculated by the formula (k1 + 2k2 + 2k3 + k4)/6
	 * @param dt delta t
	 * @param n number of steps  
	 */
	public static void rk4sim(Planet[] planets, Vector3D[][] kav, double dt, int n) {
		kav.toString();
		for(int j = 0; j < n; j++) {
			for(int i = 0; i < planets.length; i++) {
				
				if(planets[i]!=null) {
					Planet planet = planets[i];
					kav[i] = Calculator.rk4(planet.pos, planet.velocity, dt, planet, planets);
				}
			}
			for(int i = 0; i < planets.length; i++) {
				if(planets[i] != null) {
					Planet planet = planets[i];
					planet.pos = planet.pos.add(kav[i][0]);
					planet.velocity = planet.velocity.add(kav[i][1]);
				}
			}
		}
	}
	
	public static void eulerSim(Planet[] planets, Vector3D[][] kav, double dt, int n) {
		for(int j = 0; j < n; j++) {
			for(int i = 0; i < planets.length; i++) {
				if(planets[i] != null) {
					Planet planet = planets[i];
					kav[i] = Calculator.deriv(new Vector3D[] {planet.pos, planet.velocity}, planet, planets, dt);
				}
			}
			for(int i = 0; i < planets.length; i++) {
				if(planets[i] != null) {
					Planet planet = planets[i];
					planet.pos = planet.pos.add(kav[i][0]);
					planet.velocity = planet.velocity.add(kav[i][1]);
				}
			}
		}
	}
	
	

	public static boolean checkCollision(Planet ball, Planet[] planets) {
		for(int i = 0; i < planets.length; i++) {
			Planet p = planets[i];
			
			if(!p.equals(ball)) {
				double dist = p.getPos().dist(ball.getPos());
				
				if(dist < p.r) {
					System.out.println("hit " + p.name);
					return true;					
				}
			}
		}
		return false;
	}
	
}

//old code

//Vector3D ka1 = a0;
//Vector3D kv1 = v0;
//Vector3D temp = p0.add(kv1.scale(0.5*dt)); //temp holds the intermediate position 
//Vector3D ka2 = totalAcceleration(temp, self, others);
//Vector3D kv2 = v0.add(ka1.scale(0.5*dt));
//
//temp = p0.add(kv2.scale(0.5*dt));
//Vector3D ka3 = totalAcceleration(temp, self, others);
//Vector3D kv3 = v0.add(ka2.scale(0.5*dt));
//
//temp = p0.add(kv3.scale(dt));
//Vector3D ka4 = totalAcceleration(temp, self, others);
//Vector3D kv4 = v0.add(ka3.scale(dt));
//
//Vector3D ka = ka1.add(ka2.scale(2)).add(ka3.scale(2)).add(ka4).scale(1.0/6);
//Vector3D kv = kv1.add(kv2.scale(2)).add(kv3.scale(2)).add(kv4).scale(1.0/6);
//return new Vector3D[]{ka, kv};
