package xperiment;

import physics.RungeKutta4;
import physics.VerletVelocity;
import solarsystem.Planet;
import solarsystem.SolarSystem;
import utils.Date;
import utils.vector.Vector3D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ODEcompare {
    /*
    VERLET RESULTS
    Date{2012-7-14 0:0:0}
    Central Position: Vector3D{x=5.325241156329363E10, y=-1.41452184412034E11, z=1.1030699004221822E8}
    Central Velocity: Vector3D{x=27058.705656931637, y=11222.209149849, z=-0.2091756563449268}


    RK4 RESULTS
    Date{2012-7-14 0:0:0}
    Central Position: Vector3D{x=-1.6015285302666357E11, y=-2.6840744664931254E9, z=1.06603035325175E8}
    Central Velocity: Vector3D{x=-176.6153888712509, y=-29276.718785193432, z=1.0974051027277367}

    HORIZONS RESULT
    {
			"date": "14/07/2012",
			"central_pos": {"x": 5.645300900150418E+07, "y": -1.411991692780540E+08, "z": 4.150859886042774E+03},
			"central_vel": {"x": 2.717528707965805E+01, "y": 1.094708331113107E+01, "z": -3.730668094528156E-04}
		},
     */

    public static SolarSystem solarSystem;

    //RK4 vars
    public static final long dt = 6;

    //best overall date 17/06/2002
    public static final Date STARTING_DATE = new Date(2002, 06, 17, 0, 0, 0);

    //Horizons Actual Results in meters
    public static final Vector3D H_CENTRAL_POS = new Vector3D(5.645300900150418E+07 * 1000, -1.411991692780540E+08 * 1000, 4.150859886042774E+03 * 1000);
    public static final Vector3D H_CENTRAL_VEL = new Vector3D(2.717528707965805E+01 * 1000, 1.094708331113107E+01 * 1000, -3.730668094528156E-04 * 1000);

    //Simulation Vars
    public static final int ITERATION_TIME = 365 * 10 * 24;
    public static final TimeUnit UPDATE_UNIT = TimeUnit.HOURS;
    public static final boolean USE_ABSOLUTE_ERROR = true;
    public static void main (String[] args) throws IOException {

        //Runge Kutta >
        solarSystem = new SolarSystem();
        Date date = new Date(STARTING_DATE);
        solarSystem.setPositionsPlanetsAtDateKepler(date);

        ArrayList<Planet> planets = solarSystem.getPlanets().getAll();
        Planet plan = solarSystem.getPlanets().getEarth();
        RungeKutta4 rk4 = new RungeKutta4(planets, date);
        for(int i = 0; i < ITERATION_TIME; i++){
            rk4.updateLocation(1, UPDATE_UNIT);
        }

        Vector3D rk4CentralPos = plan.getCentralPos();
        Vector3D rk4CentralVel = plan.getCentralVel();

        //Verlet Velocity >
        solarSystem = new SolarSystem();
        date = new Date(STARTING_DATE);
        solarSystem.setPositionsPlanetsAtDateKepler(date);

        planets = solarSystem.getPlanets().getAll();
        plan = solarSystem.getPlanets().getEarth();
        VerletVelocity vv = new VerletVelocity(planets, date);
        for(int i = 0; i < ITERATION_TIME; i++){
            vv.updateLocation(1, UPDATE_UNIT);
        }

        Vector3D vvCentralPos = plan.getCentralPos();
        Vector3D vvCentralVel = plan.getCentralVel();

        //Vector3D vvCentralPos = new Vector3D(5.325241156329363E10, -1.41452184412034E11, 1.1030699004221822E8);
        //Vector3D vvCentralVel = new Vector3D(27058.705656931637, 11222.209149849, -0.2091756563449268);

        //Vector3D rk4CentralPos = new Vector3D(-1.6015285302666357E11, -2.6840744664931254E9, 1.06603035325175E8);
        //Vector3D rk4CentralVel = new Vector3D(-176.6153888712509, -29276.718785193432, 1.0974051027277367);

        if (!USE_ABSOLUTE_ERROR) {
            System.out.println("Verlet Position Relative Error: " + calculateRelativeErrorVector(vvCentralPos, H_CENTRAL_POS).toString());
            System.out.println("Verlet Velocity Relative Error: " + calculateRelativeErrorVector(vvCentralVel, H_CENTRAL_VEL).toString());
            System.out.println("RK4 Position Relative Error: " + calculateRelativeErrorVector(rk4CentralPos, H_CENTRAL_POS).toString());
            System.out.println("RK4 Velocity Relative Error: " + calculateRelativeErrorVector(rk4CentralVel, H_CENTRAL_VEL).toString());
        }
        else {
            System.out.println("Verlet Position Absolute Error: " + calculateAbsoluteErrorVector(vvCentralPos, H_CENTRAL_POS).norm());
            System.out.println("Verlet Velocity Absolute Error: " + calculateAbsoluteErrorVector(vvCentralVel, H_CENTRAL_VEL).norm());
            System.out.println("RK4 Position Absolute Error: " + calculateAbsoluteErrorVector(rk4CentralPos, H_CENTRAL_POS).norm());
            System.out.println("RK4 Velocity Absolute Error: " + calculateAbsoluteErrorVector(rk4CentralVel, H_CENTRAL_VEL).norm());
        }
        System.out.println(date);

    }
    public static Vector3D calculateRelativeErrorVector (Vector3D a, Vector3D b) {
        double xRE = Math.abs((a.getX() - b.getX()) / b.getX());
        double yRE = Math.abs((a.getY() - b.getY()) / b.getY());
        double zRE = Math.abs((a.getZ() - b.getZ()) / b.getZ());


        return new Vector3D(xRE, yRE, zRE);
    }
    public static Vector3D calculateAbsoluteErrorVector (Vector3D a, Vector3D b) {
        double xRE = Math.abs(a.getX() - b.getX());
        double yRE = Math.abs(a.getY() - b.getY());
        double zRE = Math.abs(a.getZ() - b.getZ());


        return new Vector3D(xRE, yRE, zRE);
    }

}
