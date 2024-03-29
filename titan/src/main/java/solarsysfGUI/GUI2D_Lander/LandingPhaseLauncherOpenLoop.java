package solarsysfGUI.GUI2D_Lander;

import javafx.application.Application;
import javafx.stage.Stage;
import solarsystem.SolarSystem;
import utils.Date;

public class LandingPhaseLauncherOpenLoop extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            SolarSystem solarSystem = new SolarSystem(); //Solar System Instance
            Date date = new Date(2002, 9, 18, 12, 0, 0); //Date

           /* Application landingPhaseApp =
                    new LandingPhaseInger(new RocketLanderOpenLoopCopy(new Vector3D(300, 170000,0),
                            new Vector3D(0,0,0), date), date);
            landingPhaseApp.start(new Stage()); */ //start with a empty stage (or you may go ahead and give some specs)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
