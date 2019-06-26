package physics;


import solarsystem.CelestialObject;
import utils.Date;
import utils.vector.Vector3D;
import java.util.ArrayList;

/**
 * An interface to distuingish all the components that can be solved by an ODE solver
 */
public interface ODEsolvable {
        /**
         * gets the acceleration of the celestial object in space  in space.
         * @param objectsInSpace arraylist of all the objects that apply a force to
         * @return acceleration
         */
        void setAcceleration(ArrayList<? extends CelestialObject> objectsInSpace, Date date);

        /**
         * @return acceleration
         */
        Vector3D getAcceleration();

        /**
         * @return get coordinates based upon HEE coordinates
         */
        Vector3D getCentralPos();

        void setCentralPos(Vector3D centralPos);

        Vector3D getCentralVel();

        void setCentralVel(Vector3D centralVel);

        void initializeCartesianCoordinates(Date date);


}
