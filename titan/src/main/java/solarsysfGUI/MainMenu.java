package solarsysfGUI;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

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

        globalRoot.setAlignment(logoView, Pos.TOP_CENTER);
        globalRoot.setAlignment(rocket, Pos.CENTER_RIGHT);

        mainScene = new Scene(globalRoot, 800, 600, true);
        primaryStage.setTitle("SolarSysF - Solar Visualization From Scratch");
        primaryStage.setScene(mainScene);

        final long startNanoTime = System.nanoTime();
        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                //rotate the rocket
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
}
