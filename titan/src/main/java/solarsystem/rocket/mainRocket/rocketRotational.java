package solarsystem.rocket.mainRocket;

import physics.ODEsolvable;
import solarsystem.CelestialObject;
import utils.Date;
import utils.vector.Vector3D;

import java.util.ArrayList;

/**
 *
 *
 */
public class rocketRotational implements ODEsolvable {
    @Override
    public void setAcceleration(ArrayList<? extends CelestialObject> objectsInSpace, Date date) {

    }

    @Override
    public Vector3D getAcceleration() {
        return null;
    }

    @Override
    public Vector3D getCentralPos() {
        return null;
    }

    @Override
    public void setCentralPos(Vector3D centralPos) {

    }

    @Override
    public Vector3D getCentralVel() {
        return null;
    }

    @Override
    public void setCentralVel(Vector3D centralVel) {

    }

    @Override
    public void initializeCartesianCoordinates(Date date) {

    }
}
