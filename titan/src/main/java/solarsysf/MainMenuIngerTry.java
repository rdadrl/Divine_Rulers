package solarsysf;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import solarsystem.CelestialObjects;
import solarsystem.SolarSystem;
import utils.Date;
import utils.MathUtil;
import utils.Vector3D;
import utils.VerletVelocity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

//shapes


public class MainMenuIngerTry extends Application {
    private Scene mainScene;
    private Group root;

    @Override

    // TODO: Add WASD keys to move around the zoomPane :)
    public void start(Stage primaryStage) {
        Date date = new Date(2000, 0, 1, 0, 0, 0);
        SolarSystem solarSystem = null;
        try {
            solarSystem = new SolarSystem();
        } catch (IOException e) {
            e.printStackTrace();
        }
        CelestialObjects earthObj = solarSystem.getPlanets().getEarth();
        CelestialObjects sunObj = solarSystem.getPlanets().getSun();

        Sphere sun      = new Sphere(sunObj.getRadius() / 100000);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.ORANGE);
        material.setSpecularColor(Color.ORANGERED);
        sun.setMaterial(material);
        sun.setTranslateX(0);
        sun.setTranslateZ(0);
        sun.setTranslateY(0);
        Sphere earth    = new Sphere(earthObj.getRadius() / 1000);

        Vector3D coordinate = earthObj.getHEEpos(date);
        earth.setTranslateX(coordinate.getX() * 100 / MathUtil.AU);
        earth.setTranslateY(coordinate.getY() * 100 *-1 / MathUtil.AU);
        earth.setTranslateZ(coordinate.getZ() * 100 / MathUtil.AU);

        PerspectiveCamera camera = new PerspectiveCamera(false);
        camera.setTranslateX(-400);
        camera.setTranslateY(-300);
        camera.setTranslateZ(100);

        root = new Group();
        root.getChildren().addAll(sun, earth);
        /*final double SCALE_DELTA = 1.1;
        final Pane zoomPane = new Pane();

        zoomPane.getChildren().addAll(earth, sun);
        zoomPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override public void handle(ScrollEvent event) {
                event.consume();

                if (event.getDeltaY() == 0) {
                    return;
                }

                double scaleFactor =
                        (event.getDeltaY() > 0)
                                ? SCALE_DELTA
                                : 1/SCALE_DELTA;

                earth.setScaleX(earth.getScaleX() * scaleFactor);
                earth.setScaleY(earth.getScaleY() * scaleFactor);
                sun.setScaleX(sun.getScaleX() * scaleFactor);
                sun.setScaleY(sun.getScaleY() * scaleFactor);
            }
        }); */

        mainScene = new Scene(root, 800, 600, true, SceneAntialiasing.BALANCED);
        mainScene.setFill(Color.BLACK);
        mainScene.setCamera(camera);
        primaryStage.setTitle("SolarSysF - Solar Visualization From Scratch");
        primaryStage.setScene(mainScene);

        //ArrayList<CelestialObjects> planets = new ArrayList<>();
        //planets.add(solarSystem.getPlanets().getSun());
        //planets.add(solarSystem.getPlanets().getEarth());

        VerletVelocity verletVelocity =
                new VerletVelocity(solarSystem.getPlanets().getAll(), date);
        final long startNanoTime = System.nanoTime();
        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                double t = (currentNanoTime - startNanoTime) / 1000000000.0;
                verletVelocity.updateLocation(1, TimeUnit.DAYS);
                System.out.println(date);
                Vector3D coordinate = earthObj.getHEEpos();
                earth.setTranslateX(coordinate.getX() * 100 / MathUtil.AU);
                earth.setTranslateY(coordinate.getY() * 100 * -1 / MathUtil.AU);
                earth.setTranslateZ(coordinate.getZ() * 100 / MathUtil.AU);
            }
        }.start();

        primaryStage.show();

        primaryStage.show();
    }
    public static void main(String args[]){
        launch(args);
    }


}
