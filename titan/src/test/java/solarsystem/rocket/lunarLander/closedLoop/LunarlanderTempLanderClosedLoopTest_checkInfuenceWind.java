package solarsystem.rocket.lunarLander.closedLoop;

import org.junit.Test;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XYChart;
import physics.VerletVelocity;
import utils.Date;
import utils.vector.Vector3D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 *
 *
 */
public class LunarlanderTempLanderClosedLoopTest_checkInfuenceWind {
    final boolean CHART = false;
    final boolean PRINT = true;
    final double PRINT_INTERVAL = 100;
    @Test
    public void landerTestNoWind() {
        landTestWindSpecificSpeed(0, 1);
    }

    @Test
    public void landerTestWind() {
        landTestWindSpecificSpeed(10, 1);
    }

    @Test
    public void landerTestWindMultiple() {
        for(int i = -10; i <= 10; i++) {
            if(i%2 != 0) continue;
            landTestWindSpecificSpeed(i, 2);
        }
    }


    private void landTestWindSpecificSpeed(double windSpeed, int testCase) {
        switch (testCase){
            case 1:
                landerSpecs1(windSpeed);
                break;
            default:
                landerSpecs1(windSpeed);
        }
    }

    private void landerSpecs1(double windSpeed) {
        Date date = new Date(2000,0,1,0,0,0);
        LunarlanderLanderClosedLoop_checkInfluenceWind rocket = new LunarlanderLanderClosedLoop_checkInfluenceWind(100, new Vector3D(0, 150000
                ,0.0),
                new Vector3D(0,-600,0.0),date, true, windSpeed);
        runVerlet(rocket, date);
    }


    private void runVerlet(LunarlanderLanderClosedLoop_checkInfluenceWind rocket, Date date) {
        ArrayList<LunarlanderLanderClosedLoop_checkInfluenceWind> obj = new ArrayList<>();
        obj.add(rocket);
        VerletVelocity verletVelocity = new VerletVelocity(obj, date);
        ArrayList<Double> y_pos, x_pos, y_vel, x_vel, y_acc, x_acc, y_jerk, x_jerk, t, t_pos, t_vel, t_acc;
        if(CHART){
            y_pos = new ArrayList<>();
            x_pos = new ArrayList<>();
            y_vel = new ArrayList<>();
            x_vel = new ArrayList<>();
            y_acc = new ArrayList<>();
            x_acc = new ArrayList<>();
            y_jerk = new ArrayList<>();
            x_jerk = new ArrayList<>();
            t = new ArrayList<>();
            t_pos = new ArrayList<>();
            t_vel = new ArrayList<>();
            t_acc = new ArrayList<>();
        }




        for(int i = 0; i < (2000*(1d/0.01)); i++) {
            if(rocket.getLanded()||!rocket.fuelLeft()) break;
            verletVelocity.updateLocation(10, TimeUnit.MILLISECONDS);
            if(CHART) {
                t.add(rocket.getTotalTime());
                x_pos.add(rocket.getCentralPos().getX());
                y_pos.add(rocket.getCentralPos().getY());
                x_vel.add(rocket.getCentralVel().getX());
                y_vel.add(rocket.getCentralVel().getY());
                x_acc.add(rocket.getAccelerationSmooth().getX());
                y_acc.add(rocket.getAccelerationSmooth().getY());
                x_jerk.add(rocket.getCentralJerk().getX());
                y_jerk.add(rocket.getCentralJerk().getY());
                t_pos.add(rocket.getCentralPos().getZ());
                t_vel.add(rocket.getCentralVel().getZ());
                t_acc.add(rocket.getAccelerationSmooth().getZ());
            }
            if(PRINT) {
                if(i%PRINT_INTERVAL == 0) {
                    rocket.printStatus();
                    //if(i < 1000) System.out.println(rocket.totTime + ", " + rocket.getCentralJerk().getX());
                }
            }


        }
        rocket.printStatus();
        if(CHART){
            ArrayList<Double> t_xpos = new ArrayList<>();
            ArrayList<Double> x_xpos = new ArrayList<>();
            for(int i = 0; i < x_pos.size(); i++) {
                if(Math.abs(x_pos.get(i)) < 300){
                    t_xpos.add(t.get(i));
                    x_xpos.add(x_pos.get(i));
                }
            }

            ArrayList<Double> t_xvel = new ArrayList<>();
            ArrayList<Double> x_xvel = new ArrayList<>();
            for(int i = 0; i < x_pos.size(); i++) {
                if(Math.abs(x_vel.get(i)) < 0.05){
                    t_xvel.add(t.get(i));
                    x_xvel.add(x_vel.get(i));
                }
            }

            ArrayList<Double> t_xacc = new ArrayList<>();
            ArrayList<Double> x_xacc = new ArrayList<>();
            for(int i = 0; i < x_pos.size(); i++) {
                if(Math.abs(x_acc.get(i)) < 5){
                    t_xacc.add(t.get(i));
                    x_xacc.add(x_acc.get(i));
                }
            }

            ArrayList<Double> t_xjerk = new ArrayList<>();
            ArrayList<Double> x_xjerk = new ArrayList<>();
            for(int i = 0; i < x_pos.size(); i++) {
                if(Math.abs(x_jerk.get(i)) < 0.5){
                    t_xjerk.add(t.get(i));
                    x_xjerk.add(x_jerk.get(i));
                }
            }


//            createChart(t_xpos, x_xpos, rocket.getMeanWindSpeed() + "_x_dist");
//            createChart(t_xvel, x_xvel, rocket.getMeanWindSpeed() + "_x_vel");
//            createChart(t_xacc, x_xacc, rocket.getMeanWindSpeed() + "_x_acc");
//            createChart(t_xjerk, x_xjerk, rocket.getMeanWindSpeed() + "_x_jerk");
            createChart(t, x_pos, rocket.getMeanWindSpeed() + "_x_pos");
            createChart(t, x_vel, rocket.getMeanWindSpeed() + "_x_vel");
            createChart(t, x_acc, rocket.getMeanWindSpeed() + "_x_acc");
            createChart(t, x_jerk, rocket.getMeanWindSpeed() + "_x_jerk");
            createChart(t, y_pos, rocket.getMeanWindSpeed() + "_y_dist");
            createChart(t, y_vel, rocket.getMeanWindSpeed() + "_y_vel");
            createChart(t, y_acc, rocket.getMeanWindSpeed() + "_y_acc");
            createChart(t, y_jerk, rocket.getMeanWindSpeed() + "_y_jerk");
            createChart(t, t_pos, rocket.getMeanWindSpeed() + "_t_dist");
            createChart(t, t_vel, rocket.getMeanWindSpeed() + "_t_vel");
            createChart(t, t_acc, rocket.getMeanWindSpeed() + "_t_acc");

        }

    }

    private void createChart(ArrayList<Double> xDataAL, ArrayList<Double> yDataAL, String name) {
        double[] xData = xDataAL.stream().mapToDouble(d -> d).toArray();
        double[] yData = yDataAL.stream().mapToDouble(d -> d).toArray();
        XYChart chart = QuickChart.getChart(name, "X", "Y", "y(x)", xData, yData);
        try {
            BitmapEncoder.saveBitmapWithDPI(chart, ("./src/test/resources/simple_wind_works_PID/" + name), BitmapEncoder.BitmapFormat.PNG, 300);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}