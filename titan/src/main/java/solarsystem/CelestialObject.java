package solarsystem;

import utils.Date;
import utils.Vector3D;

import java.util.ArrayList;

/**
 * An interface for any celestial object which have to move through space. Any object such as a
 * planet, rocket or cannonball upon which the forces of the universe apply need to inherit this
 * class.
 */
public interface CelestialObject {
    String getName();
    Vector3D getForces();
    void setForces(ArrayList<? extends CelestialObject> objectInSpace);
    Vector3D getCentralPos();
    void setCentralPos(Vector3D centralPos, Date date);
    Vector3D getCentralVel();
    void setCentralVel(Vector3D centralVel, Date date);
    double getMass();
    double getRadius();
    void initializeCartesianCoordinates(Date date);



}
