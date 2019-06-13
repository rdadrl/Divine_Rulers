package solarsysfGUI;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import javafx.stage.Stage;

/**
 * Main running class for the simulator
 */
public class MainMenu extends Application {
    Scene mainScene;
    StackPane globalRoot;
    @Override
    public void start(Stage primaryStage) {
        globalRoot = new StackPane();

        mainScene = new Scene(globalRoot, 800, 600, true);
        globalRoot.setStyle("-fx-background-image: url('textures/galaxy_starfield.png');");
        primaryStage.setTitle("SolarSysF - Solar Visualization From Scratch");
        primaryStage.setScene(mainScene);

        final long startNanoTime = System.nanoTime();
        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {

            }
        }.start();


        primaryStage.show();
    }
    public static void main(String args[]){
        launch(args);
    }
}
