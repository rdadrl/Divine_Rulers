package solarsysf;

import javafx.application.Application;
import javafx.stage.Stage;
import solarsystem.RocketLanderClosedLoop_advancedWind_trial;
import solarsystem.SolarSystem;
import utils.Date;
import utils.Vector3D;

public class LandingPhaseLauncher extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            SolarSystem solarSystem = new SolarSystem(); //Solar System Instance
            Date date = new Date(2002, 9, 18, 12, 0, 0); //Date

            Application landingPhaseApp = new LandingPhaseInger(new RocketLanderClosedLoop_advancedWind_trial(100,
                    new Vector3D(300, 170000,0), new Vector3D(0,-700,0),date, true), date);
            landingPhaseApp.start(new Stage()); //start with a empty stage (or you may go ahead and give some specs)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
