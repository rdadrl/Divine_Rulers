package physics;

import solarsystem.CelestialObject;
import utils.Date;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 *
 *
 */
public interface ODEsolver {
    void updateLocation(long time, TimeUnit unit);
    void initialize(ArrayList<? extends CelestialObject> bodies,
                    Date date);
}
