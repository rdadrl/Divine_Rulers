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
    Vector3D getHEEpos();
    void setHEEpos(Vector3D HEEpos);
    Vector3D getHEEvel();
    void setHEEvel(Vector3D HEEvel);
    double getMass();
    double getRadius();
    void initializeCartesianCoordinates(Date date);



}
