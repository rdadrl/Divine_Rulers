package solarsystem;

import utils.Vector;
import utils.Vector3D;
import utils.Vector3D;

import java.util.ArrayList;

/**
 *
 *
 */
public interface ObjectInSpace {
    Vector3D getForces();
    void setForces(ArrayList<ObjectInSpace> objectInSpace);
    Vector3D getHEEpos();
    void setHEEpos(Vector3D HEEpos);
    Vector3D getHEEvel();
    void setHEEvel(Vector3D HEEvel);
    double getMass();


}
