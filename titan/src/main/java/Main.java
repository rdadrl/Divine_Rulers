
import javafx.application.Application;
import javafx.stage.Stage;
import solarsysfGUI.MainMenu;

/**
 *
 *  TODO: NEXT PHASE
 *  - TRY RUNGE KUTTA: 4TH ORDER
 *  - EXPAND ROCKET CLASS ADD THRUST ETC.
 *  - LANDING OF ROCKET
 *  - EXPERIMENTS?
 *  - GENETIC ALGORITHM TO TEST VARIATIONS ON THE ROCKET PARAMETERS
 *  - CAMERA ROTATION
 *  - LAMBERT SOLVER?
 *
 *  Launcher of the game. Only run this file to launch the file
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        MainMenu solarSystemGUI = new MainMenu();
        solarSystemGUI.start( primaryStage);
    }
}



