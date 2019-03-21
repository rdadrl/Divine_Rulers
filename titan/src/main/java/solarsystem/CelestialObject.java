package solarsystem;

import utils.Date;
import utils.Vector3D;

import java.util.ArrayList;

/**
 *
 *
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
    void initializeCartesianCoordinates(Date date);



}
