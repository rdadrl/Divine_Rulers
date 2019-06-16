package solarsystem;

import org.junit.Test;
import physics.VerletVelocity;
import solarsystem.rocket.launch.RocketLauncher;
import solarsystem.rocket.lunarLander.openLoop.LunarlanderLanderOpenLoopVerlet;
import utils.Date;
import utils.vector.Vector3D;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class RocketLauncherTest {
    public int iteration;

    @Test
    public void launchTest() {
        Date date = new Date(2000, 0, 1, 0, 0, 0);
        RocketLauncher rocket = new RocketLauncher( new Vector3D(0, 0
                , 0.959931),
                new Vector3D(0, 0, 0), date);
        ArrayList<RocketLauncher> obj = new ArrayList<>();
        obj.add(rocket);
        VerletVelocity verletVelocity = new VerletVelocity(obj, date);

        for (int i=0; i < (2000 * (1d / 0.01)); i++) {
            iteration=i;
            verletVelocity.updateLocation(10, TimeUnit.MILLISECONDS);
            if (i % 50 == 0) {
                System.out.println("time: " + rocket.getTotalTime());
                System.out.println("X:" + rocket.getCentralPos().getX());
                System.out.println("Y:" + rocket.getCentralPos().getY());
                System.out.println("Z:" + rocket.getCentralPos().getZ());
                System.out.println("Vx:" + rocket.getCentralVel().getX());
                System.out.println("Vy:" + rocket.getCentralVel().getY());
                System.out.println("Vz:" + rocket.getCentralVel().getZ());
                System.out.println("Ax:" + rocket.getAcceleration().getX());
                System.out.println("Ay:" + rocket.getAcceleration().getY());
                System.out.println("Az:" + rocket.getAcceleration().getZ());
                System.out.println();
            }
        }

    }
}
