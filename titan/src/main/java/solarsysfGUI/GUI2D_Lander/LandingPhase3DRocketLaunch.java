package solarsysfGUI.GUI2D_Lander;

import javafx.application.Application;
import javafx.stage.Stage;
import physics.VerletVelocity;
import solarsysfGUI.TitanFocus3D;
import solarsystem.Planet;
import solarsystem.SolarSystem;
import solarsystem.rocket.launch.RocketLauncher;
import utils.Date;
import utils.vector.Vector3D;
//TODO: Change "scale" of Visualizer, distance are currently not adapted for a launch
//TODO: 22km seems to be deepspace, it should be closer to the planet
//TODO: having the trajectory drawn thicker to be able to see it from further away
public class LandingPhase3DRocketLaunch extends Application { //TODO: Should rename visualizer as used for launch and landing
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            SolarSystem solarSystem = new SolarSystem(); //Solar System Instance
            Planet earth = solarSystem.getPlanets().getEarth();
            Date date = new Date(2000, 0, 1, 0, 0, 0);
// TODO: TitanFocus3D should work with Spacecraft as well
            Application landingPhaseApp = new TitanFocus3D(new RocketLauncher(new Vector3D(0,0,-0.610865),
                    new Vector3D(0,0,0),date, earth ), date);
            ((TitanFocus3D) landingPhaseApp).setODEsolver(new VerletVelocity());
            landingPhaseApp.start(new Stage());  //start with a empty stage (or you may go ahead and give some specs)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
