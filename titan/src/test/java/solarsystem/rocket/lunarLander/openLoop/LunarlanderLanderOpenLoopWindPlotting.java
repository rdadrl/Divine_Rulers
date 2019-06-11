package solarsystem.rocket.lunarLander.openLoop;

import org.junit.Test;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import physics.VerletVelocity;
import utils.Date;
import utils.vector.Vector3D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class LunarlanderLanderOpenLoopWindPlotting {
    public int iteration;

    @Test
    public void name() {
    }

    @Test
    public void landerTest() {
        Date date = new Date(2000, 0, 1, 0, 0, 0);
        LunarlanderLanderOpenLoopVerletWind rocket = new LunarlanderLanderOpenLoopVerletWind( new Vector3D(1000, 170000
                , 0),
                new Vector3D(0, 0, 0),true,5, date);
        ArrayList<LunarlanderLanderOpenLoopVerletWind> obj = new ArrayList<>();
        obj.add(rocket);
        VerletVelocity verletVelocity = new VerletVelocity(obj, date);

        ArrayList xData = new ArrayList();
        ArrayList yData = new ArrayList();

        for (int i=0; i < (2000 * (1d / 0.01)); i++) {
            iteration=i;
            xData.add(rocket.getCentralPos().getX());
            yData.add(rocket.getCentralPos().getY());
            if (rocket.getLanded()){
                System.out.println("LANDED!!!");
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
                System.out.println("Main Thruster:" + rocket.getMainThrusterForce());
                System.out.println("Side Thrusters:" + rocket.getSideThrusterForce());
                System.out.println("Fuel Mass:" + rocket.getFuelMass());
                System.out.println();
                break;
            }
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
                System.out.println("Main Thruster:" + rocket.getMainThrusterForce());
                System.out.println("Side Thrusters:" + rocket.getSideThrusterForce());
                System.out.println("Fuel Mass:" + rocket.getFuelMass());
                System.out.println();
            }
        }

        // Create Chart
        XYChart chart = QuickChart.getChart("XY", "X", "Y", "XY(t)", xData, yData);

        // Show it
        new SwingWrapper(chart).displayChart();

        try{
            BitmapEncoder.saveBitmap(chart, "./XY", BitmapEncoder.BitmapFormat.PNG);
        }
        catch(IOException e){
            System.out.println("fail");
        }

    }

}