package solarsystem.rocket.launch;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import solarsystem.Planet;
import solarsystem.SolarSystem;
import solarsystem.rocket.SpaceCraft;
import utils.Date;
import utils.vector.Vector3D;

import java.util.ArrayList;

import static utils.MathUtil.G;

public class RocketLaunchGEO extends SpaceCraft {
    private double[] mOBhv=new double[5];

    private double[] ispParts; // all specific impulses of the rocket
    private double Isp;// current specific impulse
    private double[] mass_propellant;
    private double[] mass_rocketParts;
    private double[] maxFt;
    private double dt;

    private final double GEO = 3.58*Math.pow(10,7);
    private final double VELGEO = 3076;
    private final double M; // 5.972*Math.pow(10,24);
    private final double R; // 6371000;
    private final double g0; //9.81

    private double h;
    private double theta;
    private double beta;
    private double v;

    private double totalTime;

    private boolean shutdown;

    public RocketLaunchGEO(Vector3D centralPos,
                           Vector3D centralVel, Date date, Planet fromPlanet){
        super();
        this.h=0; //the height
        this.theta=0; //the angle with the surface body
        this.beta=Math.pow(10,-6)*2.7786; //the angle of the vehicle with the vertical
        this.v=10; //velocity
        this.dt=0.01;
        this.current_date = date;
        this.fromPlanet = fromPlanet;
        this.centralPos = centralPos;
        this.centralVel = centralVel;
        this.totalTime = 0;
        this.shutdown = false;
        M=fromPlanet.getMass();
        R=fromPlanet.getRadius();
        g0=G*M/Math.pow(R,2);//9.81;
        //Rocket Specs Based on Falcon Heavy
        double Merlin1D=914000;
        double firstPhaseFt=Merlin1D*27;
        double secondPhaseFt=1200000;
        maxFt= new double[]{firstPhaseFt,secondPhaseFt};
        double firstPhaseIsp=312;
        double secondPhaseIsp=348;
        ispParts = new double[]{firstPhaseIsp,secondPhaseIsp};
        double firstPhasePropellant=411000*3;
        double secondPhasePropellant=97000;
        mass_propellant = new double[]{firstPhasePropellant, secondPhasePropellant};
        mass_rocketParts = new double[]{22200*3,4000,20300};
        totalMass();
        acceleration = new Vector3D();
    }

    public void totalMass(){
        mass=0;
        for(int i=0;i<mass_propellant.length;i++){
            mass+=mass_propellant[i];
        }
        for(int j = 0;j<mass_rocketParts.length;j++){
            mass+=mass_rocketParts[j];
        }
    }


    public double getTime(){
        return totalTime;
    }

    public double getTheta(){
        return theta;
    }

    public double getBeta(){
        return beta;
    }

    public double getH(){
        return h;
    }

    public double getV(){
        return v;
    }

    public void setAcceleration() {
        mOBhv = rk4();
        mass = mOBhv[0];
        h = mOBhv[3];
        theta = mOBhv[1];
        beta = mOBhv[2];
        v = mOBhv[4];
        totalTime+=dt;
        /* To get the acceleration decomposed :
        double v_doubledots=Ft/mass-g0*R/(R+mOBhv[3])*Math.cos(mOBhv[2]);
        double y_doubledots=v_doubledots*Math.cos(mOBhv[2]);
        double x_doubledots=v_doubledots*Math.sin(mOBhv[2]);
         */
    }

    public double[] deriv(double[] y) {
        double[] doty = new double[5];
//        doty[0] = m, dot[1]=o, dot[2]=b, dot[3]=h and dot[4]=v
        double r = R+y[3];
        g = g0*Math.pow(R/(R+y[3]),2);
        double vhor = y[4]*Math.sin(y[2]);
        double vver = y[4]*Math.cos(y[2]);
        //mass after all first stage fuel has been consumed
        double m1= mass_rocketParts[0]+mass_rocketParts[1]+mass_rocketParts[2]+mass_propellant[1];
        //mass after all second stage fuel has been consumed
        double m2=  mass_rocketParts[1]+mass_rocketParts[2];
        //Using First Stage Thrust
        if (y[0] >= m1 && totalTime <= 175){
            Ft = maxFt[0];
            Isp = ispParts[0];
            doty[0] = -(Ft/(Isp * g0));
        }
        //No thrust powered on
        else if(y[0] <= m1 && totalTime <= 175){
            doty[0] = 0;
            Ft =0;
            //First stage dropped
            y[0] = mass_rocketParts[1]+mass_rocketParts[2]+mass_propellant[1];
        }
        //Using second stage thrust
        else if (y[0] >=  m2 && totalTime >= 175){
            Ft = maxFt[1];
            Isp = ispParts[1];
            doty[0] = -(Ft/(Isp * g0));
        }
        //No thrust powered on
        else if (y[0] <= m2 && totalTime >= 175){
            doty[0] = 0;
            Ft = 0;
            //Second stage dropped
            y[0] = mass_rocketParts[2];
        }
        doty[1] =  vhor/r;
        doty[2] = g*Math.sin(y[2])/y[4]- doty[1];
        doty[3] = vver;
        doty[4] = Ft/y[0]-g*Math.cos(y[2]);
        return doty;
    }

    public double[] predict(double[] y, double[]k, double coff) {
        double[] yp = new double[5];
        for (int i = 0; i < y.length; i++) {
            yp[i] = y[i] + k[i]*coff*dt;
        }
        return yp;
    }

    public double[] _rk4() {
        double[]y = new double[]{mass, theta, beta, h, v};
        double[] k1 = deriv(y);

/*      uncomment to use Runge Kutta
        double[] yp = predict(y, k1, 0.5);
        double[] k2 = deriv(yp);
        yp = predict(y, k2, 0.5);
        double[] k3 = deriv(yp);
        yp = predict(y, k3, 1);
        double[] k4 = deriv(yp);
 */

        double[] dy = new double[5];
        for (int i = 0; i < k1.length; i++) {
            //uncomment to use Runge Kutta
            // dy[i] = dt/6*(k1[i] + 2*(k2[i] + k3[i]) + k4[i]);
            dy[i] = dt*k1[i];
        }

        return dy;
    }

    public double[] rk4() {
        double[] dy = _rk4();
        mass = mass + dy[0];
        theta = theta + dy[1];
        beta = beta + dy[2];
        h = h + dy[3];
        v = v + dy[4];
        return new double[] {mass, theta, beta, h, v};
    }

    public double getIspG(){
        return Isp*g0;
    }

    @Override
    public void initializeCartesianCoordinates(Date date) {

    }

    @Override
    public void setCentralPos(Vector3D newCentralPos) {
        this.centralPos=newCentralPos;
    }

    //Run to see the result of fuel consumption, angular change, velocity and altitude (GEO reached)
    public static void main(String[] args){
        try{
            SolarSystem solarSystem = new SolarSystem(); //Solar System Instance
            Date date = new Date(2000, 0, 1, 0, 0, 0);
            Planet earth = solarSystem.getPlanets().getEarth();
            RocketLaunchGEO rocket=new RocketLaunchGEO(new Vector3D(0,0,0), new Vector3D(0,0,0),date,earth);
            ArrayList xData = new ArrayList();
            ArrayList yData = new ArrayList();
            ArrayList y2Data = new ArrayList();
            ArrayList y3Data = new ArrayList();
            ArrayList y4Data = new ArrayList();
            for (int i=0; i < (15000/ 0.01); i++) {
                rocket.setAcceleration();
                if(i%10==0){
                    xData.add(rocket.getTime());
                    yData.add(rocket.getH());
                    y2Data.add(rocket.getBeta());
                    y3Data.add(rocket.getMass());
                    y4Data.add(rocket.getV());
                }
                if(i==50000){
                    System.out.println("Hello");
                }
            }

            // Create Chart
            XYChart chart = QuickChart.getChart("h(t)", "t", "h", "h(t)", xData, yData);
            XYChart chart2 = QuickChart.getChart("B(t)", "t", "B", "B(t)", xData, y2Data);
            XYChart chart3 = QuickChart.getChart("m(t)", "t", "m", "m(t)", xData, y3Data);
            XYChart chart4 = QuickChart.getChart("v(t)", "t", "v", "v(t)", xData, y4Data);

            // Show it
            new SwingWrapper(chart).displayChart();
            new SwingWrapper(chart2).displayChart();
            new SwingWrapper(chart3).displayChart();
            new SwingWrapper(chart4).displayChart();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
