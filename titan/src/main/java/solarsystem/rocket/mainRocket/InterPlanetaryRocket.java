package solarsystem.rocket.mainRocket;

import physics.ODEsolvable;
import solarsystem.CelestialObject;
import solarsystem.Planet;
import solarsystem.rocket.SpaceCraft;
import utils.Date;
import utils.vector.Vector3D;

import java.util.ArrayList;

/**
 *
 *
 */
public class InterPlanetaryRocket extends SpaceCraft implements ODEsolvable {
    private boolean ImpulseManouvre;
    private Vector3D speedSetPoint;
    private Vector3D startingPos;
    private Vector3D destinationPos;
    private Date arrivalDate;
    private double timeOfFlight; //seconds


    public InterPlanetaryRocket(double mass, Planet fromPlanet, Planet toPlanet, Date current_date, Vector3D destinationPos, Date arrivalDate) {
        this.mass = mass;
        this.fromPlanet = fromPlanet;
        this.toPlanet = toPlanet;
        this.current_date = current_date;
        this.destinationPos = destinationPos;
        this.arrivalDate = new Date(arrivalDate);
        timeOfFlight = (arrivalDate.getTimeInMillis() - current_date.getTimeInMillis())/1000D;
    }

    public InterPlanetaryRocket(double mass, Planet fromPlanet,
                                Planet toPlanet, Date date) {
        this.mass = mass;
        this.fromPlanet = fromPlanet;
        this.toPlanet = toPlanet;
        this.current_date = date;
    }

    @Override
    public void setAcceleration(ArrayList<? extends CelestialObject> objectsInSpace, Date date) {
        this.current_date = date;
        Vector3D GravitationalForces = gravitationalForces(this, objectsInSpace);
        acceleration = GravitationalForces.scale(1D/mass);
    }

    @Override
    public Vector3D getAcceleration() {
        return super.getAcceleration();
    }

    @Override
    public Vector3D getCentralPos() {
        return centralPos;
    }

    @Override
    public Vector3D getCentralVel() {
        return centralVel;
    }

    @Override
    public void setCentralVel(Vector3D centralVel) {
        this.centralVel = centralVel;
    }

    @Override
    public void setCentralPos(Vector3D newCentralPos) {
        this.centralPos = newCentralPos;
    }

    @Override
    public void initializeCartesianCoordinates(Date date) {}

    private void calculateLambertTrajectory() {

    }






}
