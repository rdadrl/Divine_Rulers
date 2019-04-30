package solarsysf;

import javafx.application.Application;
import javafx.stage.Stage;
import solarsystem.CannonBall;
import solarsystem.SolarSystem;
import utils.Date;

public class LandingPhaseLauncher extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            SolarSystem solarSystem = new SolarSystem(); //Solar System Instance
            Date date = new Date(2002, 9, 18, 12, 0, 0); //Date

            Application landingPhaseApp = new LandingPhase(new CannonBall(100, 200,
                    solarSystem.getPlanets().getEarth(),
                    solarSystem.getPlanets().getTitan(), date,
                    100, 100));

            landingPhaseApp.start(new Stage()); //start with a empty stage (or you may go ahead and give some specs)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
