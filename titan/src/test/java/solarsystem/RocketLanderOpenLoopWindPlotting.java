package solarsystem;

import org.junit.Test;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import physics.VerletVelocity;
import utils.Date;
import utils.Vector3D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class RocketLanderOpenLoopWindPlotting {
    public int iteration;

    @Test
    public void landerTest() {
        Date date = new Date(2000, 0, 1, 0, 0, 0);
        RocketLanderOpenLoopVerletWind rocket = new RocketLanderOpenLoopVerletWind( new Vector3D(1000, 170000
                , 0),
                new Vector3D(0, 0, 0),true,5, date);
        ArrayList<RocketLanderOpenLoopVerletWind> obj = new ArrayList<>();
        obj.add(rocket);
        VerletVelocity verletVelocity = new VerletVelocity(obj, date);

        ArrayList xData = new ArrayList();
        ArrayList yData = new ArrayList();

        for (int i=0; i < (2000 * (1d / 0.01)); i++) {
            iteration=i;
            xData.add(rocket.totTime);
            yData.add(rocket.acceleration.getZ());
            if (rocket.landed){
                System.out.println("LANDED!!!");
                System.out.println("time: " + rocket.totTime.toString());
                System.out.println("X:" + rocket.centralPos.getX());
                System.out.println("Y:" + rocket.centralPos.getY());
                System.out.println("Z:" + rocket.centralPos.getZ());
                System.out.println("Vx:" + rocket.centralVel.getX());
                System.out.println("Vy:" + rocket.centralVel.getY());
                System.out.println("Vz:" + rocket.centralVel.getZ());
                System.out.println("Ax:" + rocket.acceleration.getX());
                System.out.println("Ay:" + rocket.acceleration.getY());
                System.out.println("Az:" + rocket.acceleration.getZ());
                System.out.println("Main Thruster:" + rocket.getMainThrusterForce());
                System.out.println("Side Thrusters:" + rocket.getSideThrusterForce());
                System.out.println("Fuel Mass:" + rocket.getFuelMass());
                System.out.println();
                break;
            }
            verletVelocity.updateLocation(10, TimeUnit.MILLISECONDS);
            if (i % 50 == 0) {
                System.out.println("time: " + rocket.totTime.toString());
                System.out.println("X:" + rocket.centralPos.getX());
                System.out.println("Y:" + rocket.centralPos.getY());
                System.out.println("Z:" + rocket.centralPos.getZ());
                System.out.println("Vx:" + rocket.centralVel.getX());
                System.out.println("Vy:" + rocket.centralVel.getY());
                System.out.println("Vz:" + rocket.centralVel.getZ());
                System.out.println("Ax:" + rocket.acceleration.getX());
                System.out.println("Ay:" + rocket.acceleration.getY());
                System.out.println("Az:" + rocket.acceleration.getZ());
                System.out.println("Main Thruster:" + rocket.getMainThrusterForce());
                System.out.println("Side Thrusters:" + rocket.getSideThrusterForce());
                System.out.println("Fuel Mass:" + rocket.getFuelMass());
                System.out.println();
            }
        }

        // Create Chart
        XYChart chart = QuickChart.getChart("Z acceleration", "Time", "Z acceleration", "Az(t)", xData, yData);

        // Show it
        new SwingWrapper(chart).displayChart();

        try{
            BitmapEncoder.saveBitmap(chart, "./ZaccWind", BitmapEncoder.BitmapFormat.PNG);
        }
        catch(IOException e){
            System.out.println("fail");
        }

    }

}