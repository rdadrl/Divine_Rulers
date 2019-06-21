package solarsystem.rocket.mainRocket;

import physics.ODEsolvable;
import physics.ODEsolver;
import solarsystem.CelestialObject;
import solarsystem.Planet;
import solarsystem.rocket.Projectile;
import solarsystem.rocket.SpaceCraft;
import utils.Date;
import utils.vector.Vector3D;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 *
 *
 */
public class InterPlanetaryRocket extends SpaceCraft implements ODEsolvable {
    private boolean ImpulseManouvre;
    private Vector3D speedSetPoint;

    public InterPlanetaryRocket(double mass, Planet fromPlanet,
                                Planet toPlanet, Date date, boolean initializeBasedOnPlanet) {
        this.mass = mass;
        this.fromPlanet = fromPlanet;
        this.toPlanet = toPlanet;
        this.old_date = date;
    }

    @Override
    public void setAcceleration(ArrayList<? extends CelestialObject> objectsInSpace, Date date) {
        this.old_date = date;
        Vector3D GravitationalForces = gravitationalForces(this, objectsInSpace);
        acceleration = GravitationalForces.scale(1D/mass);
    }

    @Override
    public Vector3D getAcceleration() {
        return super.getAcceleration();
    }

    @Override
    public Vector3D getCentralPos() {
        return super.getCentralPos();
    }

    @Override
    public Vector3D getCentralVel() {
        return super.getCentralVel();
    }

    @Override
    public void setCentralVel(Vector3D centralVel) {
        super.setCentralVel(centralVel);
    }

    @Override
    public void initializeCartesianCoordinates(Date date) {}


}
