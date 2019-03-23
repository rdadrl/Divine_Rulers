
import javafx.application.Application;
import javafx.stage.Stage;
import solarsysf.MainMenu;

/**
 *
 *  TODO: NEXT PHASE
 *  - HIT TITAN!
 *  - TRY RUNGE KUTTA: 4TH ORDER
 *  - LANDING OF ROCKET
 *  - EXPERIMENTS?
 *  - GENECTIC ALGORITHM TO TEST VARIATIONS ON THE ROCKET PARAMETERS
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



