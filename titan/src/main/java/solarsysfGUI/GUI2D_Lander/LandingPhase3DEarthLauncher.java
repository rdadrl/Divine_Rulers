package solarsysfGUI.GUI2D_Lander;

import javafx.application.Application;
import javafx.stage.Stage;
import physics.VerletVelocity;
import solarsysfGUI.EarthFocus3D;
import solarsysfGUI.TitanFocus3D;
import solarsystem.Planet;
import solarsystem.SolarSystem;
import solarsystem.rocket.launch.RocketLauncher;
import utils.Date;
import utils.vector.Vector3D;

public class LandingPhase3DEarthLauncher extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            SolarSystem solarSystem = new SolarSystem(); //Solar System Instance
            Planet earth = solarSystem.getPlanets().getEarth();
            Date date = new Date(2000, 0, 1, 0, 0, 0);

            Application landingPhaseApp = new EarthFocus3D(new RocketLauncher(new Vector3D(0,0,0),
                    new Vector3D(0,0,0),date, earth ), date);
            ((EarthFocus3D) landingPhaseApp).setODEsolver(new VerletVelocity());
            landingPhaseApp.start(new Stage());  //start with a empty stage (or you may go ahead and give some specs)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
