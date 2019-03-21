package utils;

import solarsystem.ObjectInSpace;

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

    public static Vector3D gravitationalForces(ObjectInSpace refOb, ArrayList<?
            extends ObjectInSpace> objectsInSpace) {

        Vector3D gForces = new Vector3D(); // reset the forces
        for (ObjectInSpace o : objectsInSpace) {
            if (o == refOb) {
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
}
