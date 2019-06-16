package solarsysfGUI;

import javafx.application.Application;
import javafx.stage.Stage;
import solarsystem.SolarSystem;
import solarsystem.rocket.launch.RocketLauncher;
import utils.Date;
import utils.vector.Vector3D;
//TODO: Change scale of Visualizer, distance are currently not adapted for a launch
public class LandingPhase3DRocketLaunch extends Application { //TODO: Should rename visualizer as used for launch and landing
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            SolarSystem solarSystem = new SolarSystem(); //Solar System Instance
            Date date = new Date(2000, 0, 1, 0, 0, 0);

            Application landingPhaseApp = new LandingPhase3d(new RocketLauncher(new Vector3D(0,0,-0.610865),
                    new Vector3D(0,0,0),date ), date);
            //((LandingPhase3d) landingPhaseApp).setODEsolver(new VerletVelocity());
            landingPhaseApp.start(new Stage()); //start with a empty stage (or you may go ahead and give some specs)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
