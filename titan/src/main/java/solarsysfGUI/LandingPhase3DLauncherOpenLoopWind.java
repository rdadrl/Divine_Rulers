package solarsysfGUI;

import javafx.application.Application;
import javafx.stage.Stage;
import solarsystem.rocket.lunarLander.openLoop.LunarlanderLanderOpenLoopVerletWind;
import solarsystem.SolarSystem;
import utils.Date;
import utils.vector.Vector3D;

public class LandingPhase3DLauncherOpenLoopWind extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            SolarSystem solarSystem = new SolarSystem(); //Solar System Instance
            Date date = new Date(2000, 0, 1, 0, 0, 0);

            Application landingPhaseApp = new LandingPhase3d(new LunarlanderLanderOpenLoopVerletWind( new Vector3D(1000, 170000
                    , 0),
                    new Vector3D(0, 0, 0),true,5, date), date);
            //((LandingPhase3d) landingPhaseApp).setODEsolver(new VerletVelocity());
            landingPhaseApp.start(new Stage()); //start with a empty stage (or you may go ahead and give some specs)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
