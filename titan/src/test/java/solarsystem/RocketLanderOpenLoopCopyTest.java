package solarsystem;

import org.junit.Test;
import physics.VerletVelocity;
import utils.Date;
import utils.Vector3D;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class RocketLanderOpenLoopCopyTest {
    @Test
    public void landerTest() {
        Date date = new Date(2000, 0, 1, 0, 0, 0);
        RocketLanderOpenLoopCopy rocket = new RocketLanderOpenLoopCopy( new Vector3D(1000, 200000
                , 0),
                new Vector3D(0, 0, 0), date);
        ArrayList<RocketLanderOpenLoopCopy> obj = new ArrayList<>();
        obj.add(rocket);
        VerletVelocity verletVelocity = new VerletVelocity(obj, date);

        for (int i = 0; i < (2000 * (1d / 0.01)); i++) {
            verletVelocity.updateLocation(10, TimeUnit.MILLISECONDS);
        if(i%100==0) {
            System.out.println("X:" + rocket.centralPos.getX());
            System.out.println("Y:" + rocket.centralPos.getY());
            System.out.println("Z:" + rocket.centralPos.getZ());
            System.out.println("Vx:" + rocket.centralVel.getX());
            System.out.println("Vy:" + rocket.centralVel.getY());
            System.out.println("Vz:" + rocket.centralVel.getZ());
            System.out.println("Ax:" + rocket.acceleration.getX());
            System.out.println("Ay:" + rocket.acceleration.getY());
            System.out.println("Az:" + rocket.acceleration.getZ());
            System.out.println();
        }
        }

    }
}