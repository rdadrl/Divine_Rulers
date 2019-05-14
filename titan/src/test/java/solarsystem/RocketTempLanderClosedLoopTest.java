package solarsystem;

import org.junit.Test;
import physics.VerletVelocity;
import utils.Date;
import utils.Vector3D;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 *
 *
 */
public class RocketTempLanderClosedLoopTest {
    @Test
    public void landerTestNoWind() {
        Date date = new Date(2000,0,1,0,0,0);
        RocketLanderClosedLoop rocket = new RocketLanderClosedLoop(100, new Vector3D(900, 200000
                ,0),
        new Vector3D(-30,-700,0),date);
        ArrayList<RocketLanderClosedLoop> obj = new ArrayList<>();
        obj.add(rocket);
        VerletVelocity verletVelocity = new VerletVelocity(obj, date);

        for(int i = 0; i < (2000*(1d/0.01)); i++) {
            verletVelocity.updateLocation(10, TimeUnit.MILLISECONDS);
        }
    }

    @Test
    public void landerTestWind() {
        Date date = new Date(2000,0,1,0,0,0);
        RocketLanderClosedLoop rocket = new RocketLanderClosedLoop(100, new Vector3D(800, 170000
                ,0),
                new Vector3D(0,-700,0),date, true, 2);
        ArrayList<RocketLanderClosedLoop> obj = new ArrayList<>();
        obj.add(rocket);
        VerletVelocity verletVelocity = new VerletVelocity(obj, date);

        for(int i = 0; i < (2000*(1d/0.01)); i++) {
            verletVelocity.updateLocation(10, TimeUnit.MILLISECONDS);
        }
    }

    @Test
    public void landerTestWindMultiple() {
        for(int i = -10; i <= 10; i++) {
            landTestWindSpecificSpeed(i);
        }
    }


    private void landTestWindSpecificSpeed(double windSpeed) {
        Date date = new Date(2000,0,1,0,0,0);
        RocketLanderClosedLoop rocket = new RocketLanderClosedLoop(100, new Vector3D(900, 170000
                ,0),
                new Vector3D(-30,-500,0),date, true, windSpeed);
        ArrayList<RocketLanderClosedLoop> obj = new ArrayList<>();
        obj.add(rocket);
        VerletVelocity verletVelocity = new VerletVelocity(obj, date);

        for(int i = 0; i < (2000*(1d/0.01)); i++) {
            if(rocket.landed) break;
            verletVelocity.updateLocation(10, TimeUnit.MILLISECONDS);
        }
    }
}