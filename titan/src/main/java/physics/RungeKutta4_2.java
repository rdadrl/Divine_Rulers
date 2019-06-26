package physics;


import solarsystem.rocket.Projectile;
import utils.Date;
import utils.vector.Vector3D;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 *
 *
 */
public class RungeKutta4_2 {
    private Date currentDate;
    private ArrayList<? extends ODEsolvable> bodies;
    private ArrayList<Projectile> projectiles;
    private ArrayList<solarsystem.Planet> planets;
    private Vector3D[][] dfs;

    public static double maxFt;
    public static double gasRelativeSpeed;
    public static double g0;
    public static double R;
    public static double F;
    public static double g;




    public RungeKutta4_2(){};

    public RungeKutta4_2(ArrayList<? extends ODEsolvable> bodies,
                         Date date) {
        this.currentDate = date;
        this.bodies = bodies;
        projectiles = new ArrayList<>();
        planets = new ArrayList<>();
        for (ODEsolvable body: bodies) {
            body.initializeCartesianCoordinates(date);
            if (body instanceof Projectile) projectiles.add((Projectile) body);
            if (body instanceof solarsystem.Planet) planets.add((solarsystem.Planet) body);
            System.out.println(body);
        }
        dfs = new Vector3D[bodies.size()][2];
    }

    public void initialize(ArrayList<? extends ODEsolvable> bodies, Date date) {
        this.currentDate = date;
        this.bodies = bodies;
        projectiles = new ArrayList<>();
        planets = new ArrayList<>();
        for (ODEsolvable body: bodies) {
            body.initializeCartesianCoordinates(date);
            if (body instanceof Projectile) projectiles.add((Projectile) body);
            if (body instanceof solarsystem.Planet) planets.add((solarsystem.Planet) body);
            System.out.println(body);
        }
        dfs = new Vector3D[bodies.size()][2];
    }

//    public Vector3D[] deriv(Vector3D[] y, Vector3D acceleration, double dt) {
//        Vector3D[] doty = new Vector3D[2];
//        doty[0] = y[1].scale(dt);
//        doty[1] = acceleration.scale(dt);
//        return doty;
//    }
    public static double[] deriv(double[] y, double dt) {
        double[] doty = new double[5];
//        m o b h v
        doty[0] = -maxFt/(gasRelativeSpeed*g0);
        doty[1]=(y[4]*Math.sin(y[2]))/(R+y[3]);
        doty[2]=g0*Math.pow(R/(R+y[3]),2)*Math.sin(y[2])/y[4]-y[1];
        doty[3]=y[4]*Math.cos(y[2]);
        doty[4]=F/y[0]-g*Math.cos(y[2]);
        for(int i = 0; i < doty.length; i++) {
            doty[i] = doty[i] * dt;
        }
        return doty;
    }


//    public static Vector3D[] predict(Vector3D[] y, Vector3D[] k, double coff) {
//        Vector3D[] p = new Vector3D[2];
//        for(int i = 0; i < y.length; i++) {
//            p[i] = y[i].add(k[i].scale(coff));
//        }
//        return p;
//    }
    public static double[] predict(double[] y, double[]k, double coff) {
        double[] p = new double[5];
        for (int i = 0; i < y.length; i++) {
            p[i] = y[i] + k[i]*coff;
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
//    public Vector3D[] rk4(Vector3D p0, Vector3D v0, double dt, ODEsolvable self) {
//        Vector3D[] y = new Vector3D[] {p0, v0};
//        self.setAcceleration(planets, currentDate);
//        Vector3D acceleration = self.getAcceleration();
//
//        Vector3D[] k1 = deriv(y, acceleration, dt);
//
//        Vector3D[] yp = predict(y, k1, 0.5);
//        Vector3D[] k2 = deriv(yp, acceleration, dt);
//
//        yp = predict(y, k2, 0.5);
//        Vector3D[] k3 = deriv(yp, acceleration, dt);
//
//        yp = predict(y, k3, 1);
//        Vector3D[] k4 = deriv(yp, acceleration, dt);
//
//        Vector3D[] coff = new Vector3D[2];
//        for(int i = 0; i < k1.length; i++) {
//            coff[i] = k1[i].add(k2[i].add(k3[i]).scale(2)).add(k4[i]).scale(1.0/6);
//        }
//        return coff;
//
//    }

    public static double[] _rk4(double m, double O, double B, double h, double v, double dt) {
        double[]y = new double[]{m, O, B, h, v};
        double[] k1 = deriv(y, dt);

        double[] yp = predict(y, k1, 0.5);
        double[] k2 = deriv(yp, dt);

        yp = predict(y, k2, 0.5);
        double[] k3 = deriv(yp, dt);

        yp = predict(y, k3, 1);
        double[] k4 = deriv(yp, dt);

        double[] coff = new double[5];
        for (int i = 0; i < k1.length; i++) {
            coff[i] = 1.0/6*(k1[i] + 2*(k2[i] + k3[i]) + k4[i]);
        }

        return coff;
    }

    public static double[] rk4(double m, double O, double B, double h, double v, double dt) {
        double[] coff = _rk4(m, O, B, h, v, dt);
        m = m + coff[0];
        O = O + coff[1];
        B = B + coff[2];
        h = h + coff[3];
        v = v + coff[4];
        return new double[] {m, O, B, h, v};
    }




    /*
     * simulation with RK4
     * @param planets planets
     * @param kav the dotv and dots, calculated by the formula (k1 + 2k2 + 2k3 + k4)/6
     * @param dt delta t
     * @param n number of steps
     */
//    public void rk4sim(ArrayList<? extends ODEsolvable> bodies, Vector3D[][] kav, double dt) {
//        for(int i = 0; i < bodies.size(); i++) {
//            if(bodies.get(i)!=null) {
//                ODEsolvable planet = bodies.get(i);
//               // System.out.println(planet.toString());
//                kav[i] = rk4(planet.getCentralPos(), planet.getCentralVel(), dt, planet);
//            }
//        }
//
//        for(int i = 0; i < bodies.size(); i++) {
//            if(bodies.get(i) != null) {
//                ODEsolvable planet = bodies.get(i);
//                planet.setCentralPos(planet.getCentralPos().add(kav[i][0]));
//                planet.setCentralVel(planet.getCentralVel().add(kav[i][1]));
//            }
//        }
//    }

//    @Override
//    public void updateLocation(long time, TimeUnit unit) {
//
//        time = TimeUnit.MILLISECONDS.convert(time, unit); //convert to seconds
//        currentDate.add(Calendar.MILLISECOND, (int)time);
//        double dt = time /1000D;
//        rk4sim(bodies, dfs, dt);
//    }
}
