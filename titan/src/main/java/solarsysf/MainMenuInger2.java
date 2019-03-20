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
import solarsystem.CannonBall;
import solarsystem.CelestialObjects;
import solarsystem.ObjectInSpace;
import solarsystem.SolarSystem;
import utils.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

//shapes


public class MainMenuInger2 extends Application {
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
        ArrayList<ObjectInSpace> allObj =
                new ArrayList<>(solarSystem.getPlanets().getAll());
        ObjectInSpace cannonballObj = new CannonBall(20, 0.01,
                solarSystem.getPlanets().getEarth(),
                solarSystem.getPlanets().getMars());
        cannonballObj.initializeCartesianCoordinates(date);
        allObj.add(cannonballObj);
        solarSystem.setAllObjects(allObj);

        CelestialObjects earthObj = solarSystem.getPlanets().getEarth();
        CelestialObjects sunObj = solarSystem.getPlanets().getSun();
        CelestialObjects mercuryObj = solarSystem.getPlanets().getMercury();
        CelestialObjects marsObj = solarSystem.getPlanets().getMars();
        CelestialObjects jupiterObj = solarSystem.getPlanets().getJupiter();
        CelestialObjects saturnObj = solarSystem.getPlanets().getSaturn();
        CelestialObjects titanObj = solarSystem.getPlanets().getTitan();
        CelestialObjects urAnusObj = solarSystem.getPlanets().getUranus();
        CelestialObjects neptuneObj = solarSystem.getPlanets().getNeptune();
        CelestialObjects venusObj = solarSystem.getPlanets().getVenus();
        
        // Sun
        Sphere sun      = new Sphere(sunObj.getRadius() / MathUtil.AU);
        PhongMaterial sunMaterial = new PhongMaterial();
        sunMaterial.setDiffuseColor(Color.ORANGE);
        sunMaterial.setSpecularColor(Color.ORANGERED);
        sun.setMaterial(sunMaterial);
        sun.setTranslateX(0);
        sun.setTranslateZ(0);
        sun.setTranslateY(0);

        //Earth
        Sphere earth    = new Sphere(earthObj.getRadius() / 1000);
        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseColor(Color.DARKSEAGREEN);
        earthMaterial.setSpecularColor(Color.GREEN);
        earth.setMaterial(earthMaterial);
        Vector3D earthCoordinate = earthObj.getHEEpos(date);
        earth.setTranslateX(earthCoordinate.getX() * 50);
        earth.setTranslateY(earthCoordinate.getY() * 50 * -1);
        earth.setTranslateZ(earthCoordinate.getZ() * 50);

        //Mercury
        Sphere mercury = new Sphere(mercuryObj.getRadius() / 1000);
        PhongMaterial mercuryMaterial = new PhongMaterial();
        mercuryMaterial.setDiffuseColor(Color.GREEN);
        mercuryMaterial.setSpecularColor(Color.DARKSLATEGRAY);
        mercury.setMaterial(mercuryMaterial);
        Vector3D mercuryCoordinate = earthObj.getHEEpos(date);
        mercury.setTranslateX(mercuryCoordinate.getX() * 50);
        mercury.setTranslateY(mercuryCoordinate.getY() * 50 * -1);
        mercury.setTranslateZ(mercuryCoordinate.getZ() * 50);

        //Mars
        Sphere mars = new Sphere(marsObj.getRadius() / 1000);
        PhongMaterial marsMaterial = new PhongMaterial();
        marsMaterial.setDiffuseColor(Color.INDIANRED);
        marsMaterial.setSpecularColor(Color.MEDIUMVIOLETRED);
        mars.setMaterial(marsMaterial);
        Vector3D marsCoordinate = marsObj.getHEEpos(date);
        mars.setTranslateX(marsCoordinate.getX() * 50);
        mars.setTranslateY(marsCoordinate.getY() * 50 * -1);
        mars.setTranslateZ(marsCoordinate.getZ() * 50);

        //Jupiter
        Sphere jupiter = new Sphere(jupiterObj.getRadius() / 1000);
        PhongMaterial jupiterMaterial = new PhongMaterial();
        jupiterMaterial.setDiffuseColor(Color.SANDYBROWN);
        jupiterMaterial.setSpecularColor(Color.MEDIUMVIOLETRED);
        jupiter.setMaterial(jupiterMaterial);
        Vector3D jupiterCoordinate = jupiterObj.getHEEpos(date);
        jupiter.setTranslateX(jupiterCoordinate.getX() * 50);
        jupiter.setTranslateY(jupiterCoordinate.getY() * 50 * -1);
        jupiter.setTranslateZ(jupiterCoordinate.getZ() * 50);

        //Cannonball
        Sphere cannonball = new Sphere(jupiterObj.getRadius() / 1000);
        PhongMaterial cannonbalMaterial = new PhongMaterial();
        cannonbalMaterial.setDiffuseColor(Color.SANDYBROWN);
        cannonbalMaterial.setSpecularColor(Color.MEDIUMVIOLETRED);
        cannonball.setMaterial(cannonbalMaterial);
        Vector3D cannonballCoordinate = cannonballObj.getHEEpos();
        cannonball.setTranslateX(cannonballCoordinate.getX() * 50);
        cannonball.setTranslateY(cannonballCoordinate.getY() * 50 * -1);
        cannonball.setTranslateZ(cannonballCoordinate.getZ() * 50);

        //Camera
        PerspectiveCamera camera = new PerspectiveCamera(false);
        camera.setTranslateX(-400);
        camera.setTranslateY(-300);
        camera.setTranslateZ(100);

        root = new Group();
        root.getChildren().addAll(sun, earth, mercury, mars, jupiter, cannonball);
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



        VerletVelocity verletVelocity = new VerletVelocity(solarSystem.getAllObjects(),
                date);
        final long startNanoTime = System.nanoTime();
        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                double t = (currentNanoTime - startNanoTime) / 1000000000.0;
                verletVelocity.updateLocation(1, TimeUnit.DAYS);

                if (t > 1) {
                    t = 0;
                    date.add(Calendar.DATE, 1);
                    Vector3D coordinate;

                    coordinate = earthObj.getHEEpos();
                    earth.setTranslateX(coordinate.getX() * 50 / MathUtil.AU);
                    earth.setTranslateY(coordinate.getY() * 50 * -1 / MathUtil.AU);
                    earth.setTranslateZ(coordinate.getZ() * 50 / MathUtil.AU);

                    coordinate = mercuryObj.getHEEpos();
                    mercury.setTranslateX(coordinate.getX() * 50 / MathUtil.AU);
                    mercury.setTranslateY(coordinate.getY() * 50 * -1 / MathUtil.AU);
                    mercury.setTranslateZ(coordinate.getZ() * 50 / MathUtil.AU);

                    coordinate = marsObj.getHEEpos();
                    mars.setTranslateX(coordinate.getX() * 50 / MathUtil.AU);
                    mars.setTranslateY(coordinate.getY() * 50 * -1 / MathUtil.AU);
                    mars.setTranslateZ(coordinate.getZ() * 50 / MathUtil.AU);

                    coordinate = jupiterObj.getHEEpos();
                    jupiter.setTranslateX(coordinate.getX() * 50 / MathUtil.AU);
                    jupiter.setTranslateY(coordinate.getY() * 50 * -1 / MathUtil.AU);
                    jupiter.setTranslateZ(coordinate.getZ() * 50 / MathUtil.AU);
                }
            }
        }.start();


        primaryStage.show();
    }
    public static void main(String args[]){
        launch(args);
    }
}
