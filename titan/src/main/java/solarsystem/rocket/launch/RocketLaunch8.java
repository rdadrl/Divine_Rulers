package solarsystem.rocket.launch;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import solarsystem.Planet;
import solarsystem.SolarSystem;
import solarsystem.rocket.SpaceCraft;
import utils.Date;
import utils.vector.Vector3D;

import java.io.IOException;
import java.util.ArrayList;

import static utils.MathUtil.G;

public class RocketLaunch8 extends SpaceCraft {
    private double[] mOBhv=new double[5];

    private double[] ispParts;
    private double Isp;
    private double[] mass_propellant;
    private double[] mass_rocketParts;
    private double m0;
    private double mf;
    private double[] maxFt;
    private int stage;
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

    public RocketLaunch8(Vector3D centralPos,
                         Vector3D centralVel, Date date, Planet fromPlanet){
        super();
        this.h=0;
        this.theta=0;
        this.beta=Math.pow(10,-6)*2.7786;
        this.v=10;
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
        stage=0;
        //Rocket Specs Based on Falcon 9 (Not the latest revised)
        //1st stage is Ft made by 9 Merlin 1C
        double Merlin1D=914000;
        double firstPhaseFt=Merlin1D*27;
        //2nd stage is Ft made by one Merlin 1C
        double secondPhaseFt=1200000;
        maxFt= new double[]{firstPhaseFt,secondPhaseFt};
        double firstPhaseIsp=312;
        double secondPhaseIsp=348;
        ispParts = new double[]{firstPhaseIsp,secondPhaseIsp};
        //LOX 1.141Kg/L and RP-1 is 0.81Kg/L
        //First stage: LOX : 146000L=166586Kg, RP-1 : 94000L=76140Kg, Total : 242725Kg
        //Second stage: LOX : 27600L=31491.6Kg, RP-1 : 17400L=14094Kg, Total : 45585.6Kg
        double firstPhasePropellant=411000*3;
        double secondPhasePropellant=97000;
        mass_propellant = new double[]{firstPhasePropellant, secondPhasePropellant};
        double engineDryMass=630;
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
    }

    public double[] deriv(double[] y) {
        double[] doty = new double[5];
//        m o b h v
        double r = R+y[3];
        g = g0*Math.pow(R/(R+y[3]),2);
        double vhor = y[4]*Math.sin(y[2]);
        double vver = y[4]*Math.cos(y[2]);
        if (y[0] >= (mass_rocketParts[0]+mass_rocketParts[1]+mass_rocketParts[2]+mass_propellant[1]) && totalTime <= 175){
            Ft = maxFt[0];
            Isp = ispParts[0];
            doty[0] = -(Ft/(Isp * g0));
        }
        else if(y[0] <= (mass_rocketParts[0]+mass_rocketParts[1]+mass_rocketParts[2]+mass_propellant[1]) && totalTime <= 175){
            doty[0] = 0;
            Ft =0;
            y[0] = mass_rocketParts[1]+mass_rocketParts[2]+mass_propellant[1];
        }
        else if (y[0] >=  mass_rocketParts[1]+mass_rocketParts[2] && totalTime >= 175){
            Ft = maxFt[1];
            Isp = ispParts[1];
            doty[0] = -(Ft/(Isp * g0));
        }
        else if (y[0] <= mass_rocketParts[1]+mass_rocketParts[2] && totalTime >= 175){
            doty[0] = 0;
            Ft = 0;
            y[0] = mass_rocketParts[2];
        }
        doty[1] =  vhor/r;
        doty[2] = g*Math.sin(y[2])/y[4]- doty[1];
        doty[3] = vver;
        doty[4] = Ft/y[0]-g*Math.cos(y[2]);
        //System.out.println("g:"+g0*Math.pow(R/(R+y[3]),2)+" B:"+y[2]+" sin(B):"+Math.sin(y[2])+" v:"+y[4]+" 2nd term:"+Math.sin(y[2])/y[4]+" first part:"+g0*Math.pow(R/(R+y[3]),2)*Math.sin(y[2])/y[4]+" h:"+y[3]+" 2nd part:"+(y[4]*Math.sin(y[2]))/(R+y[3]));
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

/*        double[] yp = predict(y, k1, 0.5);
        double[] k2 = deriv(yp);

        yp = predict(y, k2, 0.5);
        double[] k3 = deriv(yp);

        yp = predict(y, k3, 1);
        double[] k4 = deriv(yp);

 */

        double[] dy = new double[5];
        for (int i = 0; i < k1.length; i++) {
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

    public static void main(String[] args){
        try{
        SolarSystem solarSystem = new SolarSystem(); //Solar System Instance
        Date date = new Date(2000, 0, 1, 0, 0, 0);
        Planet earth = solarSystem.getPlanets().getEarth();
        RocketLaunch8 rocket=new RocketLaunch8(new Vector3D(0,0,0), new Vector3D(0,0,0),date,earth);
        ArrayList xData = new ArrayList();
        ArrayList yData = new ArrayList();
        for (int i=0; i < (15000/ 0.01); i++) {
            rocket.setAcceleration();
            if(i%10==0){
                xData.add(rocket.getTime());
                yData.add(rocket.getH());
            }
            if(i==50000){
                System.out.println("Hello");
            }
        }

            // Create Chart
            XYChart chart = QuickChart.getChart("h(t)", "t", "h", "h(t)", xData, yData);

            // Show it
            new SwingWrapper(chart).displayChart();

            try{
                BitmapEncoder.saveBitmap(chart, "./h6(t)", BitmapEncoder.BitmapFormat.PNG);
            }
            catch(IOException e){
                System.out.println("fail");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
