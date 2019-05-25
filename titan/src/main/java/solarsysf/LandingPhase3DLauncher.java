package solarsysf;

import javafx.application.Application;
import javafx.stage.Stage;
import physics.RungeKutta4;
import solarsystem.RocketLanderClosedLoop_advancedWind_good;
import solarsystem.SolarSystem;
import utils.Date;
import utils.Vector3D;

public class LandingPhase3DLauncher extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            SolarSystem solarSystem = new SolarSystem(); //Solar System Instance
            Date date = new Date(2002, 9, 18, 12, 0, 0); //Date

            Application landingPhaseApp = new LandingPhase3d(new RocketLanderClosedLoop_advancedWind_good(100,
                    new Vector3D(1000, 170000,0), new Vector3D(-30,-700,0),date, true), date);
            ((LandingPhase3d) landingPhaseApp).setODEsolver(new RungeKutta4());
            landingPhaseApp.start(new Stage()); //start with a empty stage (or you may go ahead and give some specs)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
