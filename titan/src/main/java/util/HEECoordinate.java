package util;

/**
 * A Heliocentric Earth ecliptic (HEE) coordinate
 * This system has its X axis towards the Earth and its Z axis perpendicular to
 * the plane of the Earth's orbit around the Sun (positive North). This system
 * is fixed with respect to the Earth-Sun line.
 *
 */
public class HEECoordinate extends Coordinate{
    public HEECoordinate() {
    }

    public HEECoordinate(double x, double y, double z) {
        super(x, y, z);
    }

    public HEECoordinate(double x, double y) {
        super(x, y);
    }
    public HEECoordinate(Coordinate vector){
        this(vector.getX(), vector.getY(), vector.getZ());
    }

    /*
     * add two coordinates to each other
     */
    public HEECoordinate add(HEECoordinate other) {
        return new HEECoordinate(super.add(other));
    }
    /*
     * substract two coordinates
     */
    public HEECoordinate substract(HEECoordinate other) {
        return new HEECoordinate(super.substract(other));
    }
    /*
     * scale the vector with a constant factor
     */
    @Override
    public HEECoordinate scale(double c) {
        return new HEECoordinate(super.scale(c));
    }
}
