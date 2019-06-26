package physics;


import solarsystem.rocket.Projectile;
import utils.Date;
import utils.vector.Vector3D;

import java.util.ArrayList;


public class RungeKutta4_for_launch {
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




    public RungeKutta4_for_launch(){};

    public RungeKutta4_for_launch(ArrayList<? extends ODEsolvable> bodies,
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

    /*
        derivative function specific to launching
        @param y, the start value, contains five different values:  m o b h v
        @param dt, the time step
     */
    public static double[] deriv(double[] y, double dt) {
        double[] doty = new double[5];
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

    /*
        predict future values using the slope and the coefficient
        @param y, the start value
        @param k, the increment
        @param coff, the coefficent or the middle time step
     */
    public static double[] predict(double[] y, double[]k, double coff) {
        double[] p = new double[5];
        for (int i = 0; i < y.length; i++) {
            p[i] = y[i] + k[i]*coff;
        }
        return p;
    }

    /*
        the inner method for computing the increment
     */
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

    /*
    main method to call
     */
    public static double[] rk4(double m, double O, double B, double h, double v, double dt) {
        double[] coff = _rk4(m, O, B, h, v, dt);
        m = m + coff[0];
        O = O + coff[1];
        B = B + coff[2];
        h = h + coff[3];
        v = v + coff[4];
        return new double[] {m, O, B, h, v};
    }

}
