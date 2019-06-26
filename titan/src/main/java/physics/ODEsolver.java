package physics;

import solarsystem.CelestialObject;
import utils.Date;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * An interface for the ODE solver
 */
public interface ODEsolver {
    void updateLocation(long time, TimeUnit unit);
    void initialize(ArrayList<? extends ODEsolvable> bodies,
                    Date date);
}
