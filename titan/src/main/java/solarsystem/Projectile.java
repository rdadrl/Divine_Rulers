package solarsystem;

import utils.Constant;

/**
 *
 *
 */
public interface Projectile extends CelestialObject{
    double minDistanceAll = Constant.BEST_DISTANCE_RANGE_CANNONBALL;
    double minDistanceAllCurrentDT = Double.MAX_VALUE;
    void checkColisions();
}
