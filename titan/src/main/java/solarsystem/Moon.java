package solarsystem;

import utils.MathUtil;
import utils.Date;
import utils.Vector3D;

import java.util.ArrayList;

/**
 * A trial to see if we could implement the moon as well
 */
public abstract class Moon implements CelestialObject {
    private String name;
    private double mass;
    private double radius;
    private Vector3D HEEpos; // Coordinate central body reference frame
    private Vector3D HEEvel; // Velocity central body reference frame
    private Vector3D forces;
    private Planet fromPlanet;
    private Planet toPlanet;
    private Date date;
    private double inclination;
    private double velocity;
    private Vector3D startVelVec;

    @Override
    public String getName() {
        return "Moon";
    }

    @Override
    public Vector3D getForces() {
        return forces;
    }

    @Override
    public void setForces(ArrayList<? extends CelestialObject> objectInSpace) {
        forces = MathUtil.gravitationalForces(this, objectInSpace);
    }

    @Override
    public Vector3D getHEEpos() {
        return HEEpos;
    }

    @Override
    public void setHEEpos(Vector3D HEEpos) {
        this.HEEpos = HEEpos;

    }

    @Override
    public Vector3D getHEEvel() {
        return HEEvel;
    }

    @Override
    public void setHEEvel(Vector3D HEEvel) {
        this.HEEvel = HEEvel;

    }

    @Override
    public double getMass() {
        return 7.349E22;
    }

    @Override
    public double getRadius() {
        return 1738.0;
    }

    @Override
    public void initializeCartesianCoordinates(Date date) {
        Date dateValid = new Date(2000, 0, 1, 12, 0);
        if(!date.equals(dateValid)) {
            System.err.println("Cannot use moon for other date");
            System.exit(0);
        }
        double X =-1.790843814584827E-01;double Y = 9.654035606449805E-01;double Z =
                    2.383722177694409E-04;
        double VX=-1.683595459103093E-02; double VY=-3.580960731546292E-03; double VZ=
                    -6.540546076886734E-06;
        HEEpos = new Vector3D(X,Y,Z);
        HEEpos = HEEpos.scale(MathUtil.AU);
        HEEvel = new Vector3D(VX,VY,VZ);
        HEEvel = HEEvel.scale(MathUtil.AU/(60*60*24));
    }
}

