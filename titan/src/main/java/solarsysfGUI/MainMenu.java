package solarsysfGUI;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Map;

/**
 * Main running class for the simulator
 */
public class MainMenu extends Application {
    Scene mainScene;
    StackPane globalRoot;

    @Override
    public void start(Stage primaryStage) {
        globalRoot = new StackPane();
        globalRoot.setStyle("-fx-background-color: black;");
        // Create the media source.
        File bgfile = new File("src/main/resources/bg_vid.mp4");
        Media media = null;
        try {
            media = new Media(bgfile.toURI().toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Create the player and set to play automatically.
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            }
        });

        // Create the view and add it to the Scene.
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setOpacity(0.5);
        globalRoot.getChildren().add(mediaView);

        int rocketScale = 25;
        Group rocket = loadRocket();
        rocket.setScaleX(rocketScale);
        rocket.setScaleY(rocketScale);
        rocket.setScaleZ(rocketScale);
        Rotate rotate = new Rotate(0, Rotate.Y_AXIS);
        rocket.getTransforms().add(rotate);

        rocket.setTranslateX(-75);
        globalRoot.getChildren().add(rocket);

        //logo
        File logofile = new File("src/main/resources/starsysf_logo.png");
        Image logo = null;
        try {
            logo = new Image(logofile.toURI().toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ImageView logoView = new ImageView(logo);
        logoView.setPreserveRatio(true);
        logoView.setFitWidth(600);
        globalRoot.getChildren().add(logoView);

        VBox buttonGroup = new VBox(20);

        Button startButton = new Button();
        startButton.setText("s t a r t");
        startButton.setOnAction(event -> {
            try {
                RocketLaunchStage newGame = new RocketLaunchStage();
                newGame.start(new Stage());
                primaryStage.close();
            }
            catch(Exception e) {
                System.out.println("Unable to start a new game. Could not initialize Rocket Launch Stage.");
            }
        });
        startButton.setStyle(buttonStyleCreator(75, 10));

        Button quitButton = new Button();
        quitButton.setText("q u i t");
        quitButton.setOnAction(event -> System.exit(0));
        quitButton.setStyle(buttonStyleCreator(50, 10));

        buttonGroup.getChildren().addAll(startButton, quitButton);
        buttonGroup.setTranslateX(-5);
        buttonGroup.setAlignment(Pos.CENTER_LEFT);

        globalRoot.getChildren().add(buttonGroup);

        mediaView.toBack();

        globalRoot.setAlignment(logoView, Pos.TOP_CENTER);
        globalRoot.setAlignment(rocket, Pos.CENTER_RIGHT);
        globalRoot.setAlignment(buttonGroup, Pos.CENTER_LEFT);

        mainScene = new Scene(globalRoot, 800, 600);
        primaryStage.setTitle("SolarSysF - Solar Visualization From Scratch");
        primaryStage.setScene(mainScene);

        final long startNanoTime = System.nanoTime();
        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                //rotate the InterPlanetaryRocket
                rotate.setAngle(rotate.getAngle() + 0.5);
            }
        }.start();


        primaryStage.show();
    }
    public static void main(String args[]){
        launch(args);
    }

    public Group loadRocket() {
        String FILE_LOC = "src/main/resources/rocketObj/scifi_cartoon_rocket.obj";

        File file = new File(FILE_LOC);
        ObjModelImporter objImporter = new ObjModelImporter();
        TdsModelImporter tdsImporter = new TdsModelImporter();
        String filename = "";
        try {
            filename = file.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        objImporter.read(filename);
        final Node[] tdsMesh = (Node[]) objImporter.getImport();

        Map<String, PhongMaterial> mapTexs = objImporter.getNamedMaterials();
        Iterator<String> it = mapTexs.keySet().iterator();
        boolean differCol = true;
        while (it.hasNext()) {
            String key = it.next();
            if (differCol) mapTexs.get(key).setDiffuseColor(Color.RED);
            else mapTexs.get(key).setDiffuseColor(Color.WHITE);

            differCol = !differCol;
        }

        objImporter.close();

        Group rocketGroup = new Group();
        for (int i = 1; i < tdsMesh.length; i++) {
            rocketGroup.getChildren().add(tdsMesh[i]);
        }
        return rocketGroup;
    }
    public static String buttonStyleCreator(int width, int height) {
        return  "-fx-background-color: \n" +
                "        #090a0c,\n" +
                "        linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),\n" +
                "        linear-gradient(#20262b, #191d22),\n" +
                "        radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));\n" +
                "    -fx-background-radius: 5,4,3,5;\n" +
                "    -fx-background-insets: 0,1,2,0;\n" +
                "    -fx-text-fill: white;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );\n" +
                "    -fx-font-family: \"monospace\";\n" +
                "    -fx-text-fill: linear-gradient(white, #d0d0d0);\n" +
                "    -fx-font-size: 12px;\n" +
                "    -fx-opacity: 0.75;\n" +
                "    -fx-padding: " + height + " " + width + " " + height + " " + width + ";";
    }
}
