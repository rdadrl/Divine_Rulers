package solarsystem;

import org.junit.Assert;
import org.junit.Test;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XYChart;
import physics.ODEsolver;
import physics.RungeKutta4;
import physics.VerletVelocity;
import utils.Date;
import utils.Vector3D;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 *
 *
 */
public class RocketTempLanderClosedLoopTest_advancedWind_rot {
    private String folderName = "advanced_wind_trialMaxRot_noPID/";
    private String pathName;
    boolean CHART = false;
    boolean PRINT = true;
    final double PRINT_INTERVAL = 100;
    final int testCase = 3;
    ODEsolver ODEsolver = new VerletVelocity();

    @Test
    public void checkParametersNoWind() {
        CHART = false;
        PRINT = false;
        for (int i = 1; i <= 5; i++) {
            RocketLanderClosedLoop_advancedWind_good rocket = landTestWindSpecificSpeed(0, i);
            Assert.assertTrue(rocket.getCentralPos().getY() < 0.1);
            Assert.assertTrue(rocket.getCentralPos().getX() < 0.1);
            Assert.assertTrue(rocket.getCentralPos().getZ() < 0.02);
            Assert.assertTrue(rocket.getCentralVel().getY() < 0.1);
            Assert.assertTrue(rocket.getCentralVel().getX() < 0.1);
            Assert.assertTrue(rocket.getCentralVel().getZ() < 0.01);
        }
    }


    @Test
    public void landerTestNoWind() {
        if(CHART) createFolderDate();
        landTestWindSpecificSpeed(0, testCase);
    }

    @Test
    public void landerTestWind() {
        if(CHART) createFolderDate();
        landTestWindSpecificSpeed(-10, testCase);
    }

    @Test
    public void landerTestWindMultiple() {
        if(CHART) createFolderDate();
        for(int i = -10; i <= 10; i++) {
            //if(i%2 != 0) continue;
            landTestWindSpecificSpeed(i, testCase);
        }
    }

    private RocketLanderClosedLoop_advancedWind_good landTestWindSpecificSpeed(double windSpeed, int testCase) {
        switch (testCase){
            case 1:
                return landerSpecs1(windSpeed);
            case 2:
                return landerSpecs2(windSpeed);
            case 3:
                return landerSpecs3(windSpeed);
            case 4:
                return landerSpecs4(windSpeed);
            case 5:
                return landerSpecs5(windSpeed);
            default:
                return landerSpecs1(windSpeed);
        }
    }

    private RocketLanderClosedLoop_advancedWind_good landerSpecs1(double windSpeed) {
        Date date = new Date(2000,0,1,0,0,0);
        RocketLanderClosedLoop_advancedWind_good rocket = new RocketLanderClosedLoop_advancedWind_good(100, new Vector3D(900, 150000
                ,0.04),
                new Vector3D(0,-600,0.01),date, true, windSpeed);
        runODE(rocket, date);
        return rocket;
    }

    private RocketLanderClosedLoop_advancedWind_good landerSpecs2(double windSpeed) {
        Date date = new Date(2000,0,1,0,0,0);
        RocketLanderClosedLoop_advancedWind_good rocket = new RocketLanderClosedLoop_advancedWind_good(100, new Vector3D(-2000, 220000
                ,0),
                new Vector3D(30,-600,0),date, true, windSpeed);
        runODE(rocket, date);
        return rocket;

    }

    private RocketLanderClosedLoop_advancedWind_good landerSpecs3(double windSpeed) {
        Date date = new Date(2000,0,1,0,0,0);
        RocketLanderClosedLoop_advancedWind_good rocket = new RocketLanderClosedLoop_advancedWind_good(100, new Vector3D(-3000, 150000
                ,0.04),
                new Vector3D(40,-50,0.01),date, true, windSpeed);
        runODE(rocket, date);
        return rocket;
    }

    private RocketLanderClosedLoop_advancedWind_good landerSpecs4(double windSpeed) {
        Date date = new Date(2000,0,1,0,0,0);
        RocketLanderClosedLoop_advancedWind_good rocket = new RocketLanderClosedLoop_advancedWind_good(100,
                new Vector3D(1000, 170000,0), new Vector3D(-30,-700,0),date, true, windSpeed);
        runODE(rocket, date);
        return rocket;
    }

    private RocketLanderClosedLoop_advancedWind_good landerSpecs5(double windSpeed) {
        Date date = new Date(2000,0,1,0,0,0);
        RocketLanderClosedLoop_advancedWind_good rocket = new RocketLanderClosedLoop_advancedWind_good(100,
        new Vector3D(1000, 170000,0), new Vector3D(-30,-700,0),date, true, windSpeed);
        runODE(rocket, date);
        return rocket;

    }
    private void runODE(RocketLanderClosedLoop_advancedWind_good rocket, Date date) {
        ArrayList<RocketLanderClosedLoop_advancedWind_good> obj = new ArrayList<>();
        obj.add(rocket);
        ODEsolver.initialize(obj, date);
        ArrayList<Double> y_pos, x_pos, y_vel, x_vel, y_acc, x_acc, y_jerk, x_jerk, t, t_pos, t_vel, t_acc, x_acc_abs, Ft_list, Fl_list, x_jerk_avg, x_acc_avg, t_10;

        y_pos = new ArrayList<>();
        x_pos = new ArrayList<>();
        y_vel = new ArrayList<>();
        x_vel = new ArrayList<>();
        y_acc = new ArrayList<>();
        x_acc = new ArrayList<>();
        x_acc_abs = new ArrayList<>();
        y_jerk = new ArrayList<>();
        x_jerk = new ArrayList<>();
        t = new ArrayList<>();
        t_pos = new ArrayList<>();
        t_vel = new ArrayList<>();
        t_acc = new ArrayList<>();
        Ft_list = new ArrayList<>();
        Fl_list = new ArrayList<>();
        x_jerk_avg = new ArrayList<>();
        t_10 = new ArrayList<>();
        x_acc_avg = new ArrayList<>();


        for(int i = 0; i < (2000*(1d/0.01)); i++) {
            if(rocket.landed||!rocket.fuelLeft()) break;
            ODEsolver.updateLocation(10, TimeUnit.MILLISECONDS);
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
                Ft_list.add(rocket.getMainThrusterForce());
                Fl_list.add(rocket.getSideThrusterForce());
                //x_jerk_avg.add(rocket.getAverageJerkX());
                t_10.add(rocket.getTotalTime());
                //x_acc_avg.add(rocket.getAverageAccX());

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
            double Hl = rocket.phase2X_time;

//            createChart(t, x_jerk_avg, rocket.meanWindSpeed + "_x_jerk_avg_z", -0.01, 0.01, Hl);
//            createChart(t, x_jerk_avg, rocket.meanWindSpeed + "_x_jerk_avg", 0, 0, Hl);
//            createChart(t, x_acc_avg, rocket.meanWindSpeed + "_x_acc_avg", 0, 0, Hl);

//            createChart(t, x_jerk_avg, rocket.meanWindSpeed + "_x_jerk_avg_z_max", -0.2, 0.2, Hl);
            createChart(t, x_pos, rocket.meanWindSpeed + "_x_pos_z", -20, 20, Hl);
//            createChart(t, x_vel, rocket.meanWindSpeed + "_x_vel_z", -0.5, 0.5, Hl);
//            createChart(t, x_acc, rocket.meanWindSpeed + "_x_acc_z", -0.05, 0.05, Hl);
//            createChart(t, x_jerk, rocket.meanWindSpeed + "_x_jerk_z", -0.01, 0.01, Hl);
            createChart(t, x_pos, rocket.meanWindSpeed + "_x_pos", 0, 0, Hl);
            createChart(t, x_vel, rocket.meanWindSpeed + "_x_vel", 0, 0, Hl);
//            createChart(t, x_acc, rocket.meanWindSpeed + "_x_acc", 0, 0, Hl);
//            createChart(t, x_jerk, rocket.meanWindSpeed + "_x_jerk", 0, 0, Hl);
            createChart(t, y_pos, rocket.meanWindSpeed + "_y_pos", 0, 0, Hl);
            createChart(t, y_vel, rocket.meanWindSpeed + "_y_vel", 0, 0, Hl);
//            createChart(t, y_acc, rocket.meanWindSpeed + "_y_acc", 0, 0, Hl);
//            createChart(t, y_jerk, rocket.meanWindSpeed + "_y_jerk",0, 0, Hl);
            createChart(t, t_pos, rocket.meanWindSpeed + "_t_pos",0, 0, Hl);
//            createChart(t, t_vel, rocket.meanWindSpeed + "_t_vel",0, 0, Hl);
//            createChart(t, t_acc, rocket.meanWindSpeed + "_t_acc",0, 0, Hl);
            createChart(t, Ft_list, rocket.meanWindSpeed + "_Ft",0, 0, Hl);
//            createChart(t, Fl_list, rocket.meanWindSpeed + "_Fl",0, 0, Hl);
        }

    }

    private void createChart(ArrayList<Double> xDataAL, ArrayList<Double> yDataAL, String name) {
        double[] xData = xDataAL.stream().mapToDouble(d -> d).toArray();
        double[] yData = yDataAL.stream().mapToDouble(d -> d).toArray();


        if(yData.length >0) {
            XYChart chart = QuickChart.getChart(name, "X", "Y", "y(x)", xData, yData);
            try {
                BitmapEncoder.saveBitmapWithDPI(chart, (pathName + name), BitmapEncoder.BitmapFormat.PNG, 300);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createChart(ArrayList<Double> xDataAL, ArrayList<Double> yDataAL, String name, double yMin, double yMax, double Hl_t) {
        double[] xData = xDataAL.stream().mapToDouble(d -> d).toArray();
        double[] yData = yDataAL.stream().mapToDouble(d -> d).toArray();


        if(yData.length >0) {
            XYChart chart = QuickChart.getChart(name, "X", "Y", "y(x)", xData, yData);
            if(yMin != 0 || yMax !=0){
                chart.getStyler().setYAxisMax(yMax);
                chart.getStyler().setYAxisMin(yMin);
                chart.addSeries("Hl", new double[]{Hl_t, Hl_t}, new double[]{yMin, yMax});
            }else{
                chart.addSeries("Hl", new double[]{Hl_t, Hl_t}, new double[]{Collections.min(yDataAL), Collections.max(yDataAL)});
            }

              try {
                BitmapEncoder.saveBitmapWithDPI(chart, (pathName + name), BitmapEncoder.BitmapFormat.PNG, 300);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createFolderDate() {
        java.util.Date currentDate = new java.util.Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_HH_mm");
        pathName = "./src/test/resources/" + folderName + formatter.format(currentDate) + "_T" + testCase+"/";
        new File(pathName).mkdir();
    }
}