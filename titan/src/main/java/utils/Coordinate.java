package utils;

import java.util.Arrays;

/**
 *
 *
 */
public class Coordinate extends Vector{
    private double[] arr;

    public Coordinate() {
        arr = new double[]{super.getX(), super.getY(), super.getZ()};
    }

    public Coordinate(double x, double y, double z) {
        super(x, y, z);
        arr = new double[]{super.getX(), super.getY(), super.getZ()};
    }

    public Coordinate(Coordinate coordinate){
        this(coordinate.getX(), coordinate.getY(), coordinate.getZ());
    }

    public Coordinate(Vector vector){
        this(vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * 2D coordinate constructor
     * @param x x coordinate
     * @param y y coordinate
     */
    public Coordinate(double x, double y) {
        super(x, y, 0);
        arr = new double[]{super.getX(), super.getY()};
    }

    public double[] getArray(){
        return arr;
    }

    /*
     * add two coordinates to each other
     */
    public Coordinate add(Coordinate other) {
        return new Coordinate(super.add(other));
    }
    /*
     * substract two coordinates
     */
    public Coordinate substract(Coordinate other) {
        return new Coordinate(super.substract(other));
    }
    /*
     * scale the vector with a constant factor
     */
    @Override
    public Coordinate scale(double c) {
        return new Coordinate(super.scale(c));
    }

    @Override
    public void setX(double x){
        super.setX(x);
        arr[0] = x;
    }
    @Override
    public void setY(double y){
        super.setY(y);
        arr[1] = y;
    }
    @Override
    public void setZ(double z){
        super.setZ(z);
        arr[2] = z;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + super.getX() +
                ", y=" + super.getY() +
                ", z=" + super.getZ() +
                ", arr=" + Arrays.toString(arr) +
                '}';
    }
}
