package solarsysfGUI.LanderRendevous;

import javafx.application.Application;
import javafx.stage.Stage;
import physics.RungeKutta4;
import solarsystem.SolarSystem;
import solarsystem.Trajectory;
import solarsystem.rocket.mainRocket2DRendezvous.MainRocket2DRendezvous;
import utils.Date;
import utils.vector.Vector3D;

public class LandingPhase3DLauncherRendezvous extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Date date = new Date(2002, 9, 18, 12, 0, 0); //Date
            double titanMass = 1.3452*Math.pow(10,23); // Mass in kg
            double sphereOfInfluence = 122187e04*Math.pow(titanMass/5.6834e26,(2.0/5.0)); // Semimajor axis of Titan times ratio masses of titan and satur

            Vector3D initialPos = new Vector3D(-0.7071, 0.7071,0);
            initialPos = initialPos.scale(1.3*sphereOfInfluence);
            //initialPos = initialPos.add((new Vector3D(0,-1,0)).scale(solarSystem.getPlanets().getTitan().getRadius()*1e03));
            Vector3D initialVel = new Vector3D(0,-1,0).scale(5e02);
            Trajectory trajectory = new Trajectory(titanMass, sphereOfInfluence, 0, 0);

            MainRocket2DRendezvous rocket = new MainRocket2DRendezvous(100000000, initialPos,
                    initialVel, date, trajectory);
            LandingPhase3dRendevous landingPhaseApp = new LandingPhase3dRendevous(rocket, date);
            landingPhaseApp.setODEsolver(new RungeKutta4());
            landingPhaseApp.start(new Stage()); //start with a empty stage (or you may go ahead and give some specs)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
