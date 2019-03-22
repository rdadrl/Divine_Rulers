package physics;

import javafx.geometry.Point3D;
import solarsystem.CannonBall;
import solarsystem.CelestialObject;
import solarsystem.Rocket;
import utils.Vector;
import utils.Vector3D;

import java.util.ArrayList;

/**
 *
 *
 */
public class MathUtil {
    public static final double AU = 149597870700.0;    // AU in m
    public static final double GAU = 1.993E-44;          // AU^3/kg*s^2
    public static final double G =  6.6742867E-11;         // m/kg*s^2


    public static double sinDegree(double degree){
        return(Math.sin(Math.toRadians(degree)));
    }

    public static double cosDegree(double degree){
        return(Math.cos(Math.toRadians(degree)));
    }

    public static Vector3D gravitationalForces(CelestialObject refOb, ArrayList<?
            extends CelestialObject> objectsInSpace) {

        Vector3D gForces = new Vector3D(); // reset the forces
        for (CelestialObject o : objectsInSpace) {
            if (o == refOb || o instanceof CannonBall || o instanceof Rocket) {
                continue;
            }
            Vector3D r = o.getHEEpos().substract(refOb.getHEEpos());
            double dist = r.length();
            // F(m1<-m2) = (G * m1 * m2) / r^2
            double netForce = (MathUtil.G * o.getMass() * refOb.getMass()) /
                    Math.pow(dist, 2);
            Vector3D thisForce = r.unit().scale(netForce);
            thisForce.length();
            gForces = gForces.add(thisForce);
        }
        return gForces;
    }

    //TODO: rewritht
    public static boolean FindLineSphereIntersections(Vector3D linePoint0, Vector3D linePoint1,
                                                        Vector3D circleCenter, double circleRadius)
    {

        double cx = circleCenter.getX();
        double cy = circleCenter.getY();
        double cz = circleCenter.getZ();

        double px = linePoint0.getX();
        double py = linePoint0.getY();
        double pz = linePoint0.getZ();

        double vx = linePoint1.getX() - px;
        double vy = linePoint1.getY() - py;
        double vz = linePoint1.getZ() - pz;

        double A = vx * vx + vy * vy + vz * vz;
        double B = 2.0 * (px * vx + py * vy + pz * vz - vx * cx - vy * cy - vz * cz);
        double C = px * px - 2 * px * cx + cx * cx + py * py - 2 * py * cy + cy * cy +
                pz * pz - 2 * pz * cz + cz * cz - circleRadius * circleRadius;

        // discriminant
        double D = B * B - 4 * A * C;

        double t1 = (-B -Math.sqrt(D)) / (2.0 * A);

        Vector3D solution1 = new Vector3D(linePoint0.getX() * (1 - t1) + t1 * linePoint1.getX(),
                linePoint0.getY() * (1 - t1) + t1 * linePoint1.getY(),
                linePoint0.getZ() * (1 - t1) + t1 * linePoint1.getZ());

        double t2 = (-B + Math.sqrt(D)) / (2.0 * A);
        Vector3D solution2 = new Vector3D(linePoint0.getX() * (1 - t2) + t2 * linePoint1.getX(),
                linePoint0.getY() * (1 - t2) + t2 * linePoint1.getY(),
                linePoint0.getZ() * (1 - t2) + t2 * linePoint1.getZ());

        if (D < 0 || t1 > 1 || t2 >1)
        {
            return false;
        }
        else if (D == 0)
        {
            return true;
        }
        else
        {
            return true;
        }
    }
}
