package solarsysf;

import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Ellipse;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
//shapes
import javafx.scene.shape.Sphere;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import solarsystem.CannonBall;
import solarsystem.CelestialObjects;
import solarsystem.ObjectInSpace;
import solarsystem.SolarSystem;
import utils.MathUtil;
import utils.Vector3D;
import utils.Date;
import utils.VerletVelocity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;


public class MainMenu extends Application {
    private Scene mainScene;
    private Group root;
    private boolean goNorth, goSouth, goEast, goWest;
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
        Sphere sun      = new Sphere(sunObj.getRadius() / MathUtil.AU * 1000000);
        PhongMaterial sunMaterial = new PhongMaterial();
        sunMaterial.setDiffuseColor(Color.ORANGE);
        sunMaterial.setSpecularColor(Color.ORANGERED);
        sun.setMaterial(sunMaterial);
        sun.setTranslateX(0);
        sun.setTranslateZ(0);
        sun.setTranslateY(0);

        //Earth
        Sphere earth    = new Sphere(earthObj.getRadius() / MathUtil.AU * 100000000);
        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseColor(Color.DARKSEAGREEN);
        earthMaterial.setSpecularColor(Color.GREEN);
        earth.setMaterial(earthMaterial);
        Vector3D earthCoordinate = earthObj.getHEEpos(date);
        earth.setTranslateX(earthCoordinate.getX() * 30);
        earth.setTranslateY(earthCoordinate.getY() * 30 * -1);
        earth.setTranslateZ(earthCoordinate.getZ() * 30);

        //Mercury
        Sphere mercury = new Sphere(mercuryObj.getRadius() / MathUtil.AU * 100000000);
        PhongMaterial mercuryMaterial = new PhongMaterial();
        mercuryMaterial.setDiffuseColor(Color.GREEN);
        mercuryMaterial.setSpecularColor(Color.DARKSLATEGRAY);
        mercury.setMaterial(mercuryMaterial);
        Vector3D mercuryCoordinate = earthObj.getHEEpos(date);
        mercury.setTranslateX(mercuryCoordinate.getX() * 30);
        mercury.setTranslateY(mercuryCoordinate.getY() * 30 * -1);
        mercury.setTranslateZ(mercuryCoordinate.getZ() * 30);

        //Mars
        Sphere mars = new Sphere(marsObj.getRadius() / MathUtil.AU * 100000000);
        PhongMaterial marsMaterial = new PhongMaterial();
        marsMaterial.setDiffuseColor(Color.INDIANRED);
        marsMaterial.setSpecularColor(Color.MEDIUMVIOLETRED);
        mars.setMaterial(marsMaterial);
        Vector3D marsCoordinate = marsObj.getHEEpos(date);
        mars.setTranslateX(marsCoordinate.getX() * 30);
        mars.setTranslateY(marsCoordinate.getY() * 30 * -1);
        mars.setTranslateZ(marsCoordinate.getZ() * 30);

        //Jupiter
        Sphere jupiter = new Sphere(jupiterObj.getRadius() / MathUtil.AU * 10000000);
        PhongMaterial jupiterMaterial = new PhongMaterial();
        jupiterMaterial.setDiffuseColor(Color.SANDYBROWN);
        jupiterMaterial.setSpecularColor(Color.MEDIUMVIOLETRED);
        jupiter.setMaterial(jupiterMaterial);
        Vector3D jupiterCoordinate = jupiterObj.getHEEpos(date);
        jupiter.setTranslateX(jupiterCoordinate.getX() * 30);
        jupiter.setTranslateY(jupiterCoordinate.getY() * 30 * -1);
        jupiter.setTranslateZ(jupiterCoordinate.getZ() * 30);

        //Saturn
        Sphere saturn = new Sphere(saturnObj.getRadius() / MathUtil.AU * 10000000);
        PhongMaterial saturnMaterial = new PhongMaterial();
        saturnMaterial.setDiffuseColor(Color.LIGHTYELLOW);
        saturnMaterial.setSpecularColor(Color.BLUE);
        saturn.setMaterial(saturnMaterial);
        Vector3D saturnCoordinate = saturnObj.getHEEpos(date);
        saturn.setTranslateX(saturnCoordinate.getX() * 30);
        saturn.setTranslateY(saturnCoordinate.getY() * 30 * -1);
        saturn.setTranslateZ(saturnCoordinate.getZ() * 30);

        //Titan
        Sphere titan = new Sphere(titanObj.getRadius() / MathUtil.AU * 1000000000);
        PhongMaterial titanMaterial = new PhongMaterial();
        titanMaterial.setDiffuseColor(Color.GREEN);
        titanMaterial.setSpecularColor(Color.SANDYBROWN);
        titan.setMaterial(titanMaterial);
        Vector3D titanCoordinate = jupiterObj.getHEEpos(date);
        titan.setTranslateX(titanCoordinate.getX() * 30);
        titan.setTranslateY(titanCoordinate.getY() * 30 * -1);
        titan.setTranslateZ(titanCoordinate.getZ() * 30);

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

        //Date Label
        Label dateLabel = new Label(date.toDateString());
        dateLabel.setTextFill(Color.WHITE);

        //Camera
        PerspectiveCamera camera = new PerspectiveCamera(false);
        camera.setTranslateX(-400);
        camera.setTranslateY(-300);
        camera.setTranslateZ(100);

        root = new Group();
        root.getChildren().addAll(sun, earth, mercury, mars, jupiter, saturn,
                titan, cannonball);


        AnchorPane globalRoot = new AnchorPane();
        globalRoot.getChildren().addAll(dateLabel);
        //Anchor the dateLabel
        globalRoot.setBottomAnchor(dateLabel, 10D);
        globalRoot.setRightAnchor(dateLabel, 10D);

        SubScene subScene = new SubScene(root,800,600,false,SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        globalRoot.getChildren().add(subScene);


        mainScene = new Scene(globalRoot, 800, 600, true);
        globalRoot.setStyle("-fx-background-color: #000;");
        primaryStage.setTitle("SolarSysF - Solar Visualization From Scratch");
        primaryStage.setScene(mainScene);

        final double SCALE_DELTA = 1.1;
        mainScene.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override public void handle(ScrollEvent event) {
                event.consume();

                if (event.getDeltaY() == 0) {
                    return;
                }

                double scaleFactor = (event.getDeltaY() > 0)
                                        ? SCALE_DELTA
                                        : 1/SCALE_DELTA;

                System.out.println(camera.getTranslateZ());
                if (camera.getTranslateZ() * scaleFactor > 2 && camera.getTranslateZ() * scaleFactor < 1000) camera.setTranslateZ(camera.getTranslateZ() * scaleFactor);
            }
        });

        //KeyUp&KeyDown https://stackoverflow.com/questions/29962395/how-to-write-a-keylistener-for-javafx
        mainScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:    goNorth = true; break;
                    case DOWN:  goSouth = true; break;
                    case LEFT:  goWest  = true; break;
                    case RIGHT: goEast  = true; break;
                }
            }
        });

        mainScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:    goNorth = false; break;
                    case DOWN:  goSouth = false; break;
                    case LEFT:  goWest  = false; break;
                    case RIGHT: goEast  = false; break;
                }
            }
        });
        //Main Animations Handler
        VerletVelocity verletVelocity = new VerletVelocity(solarSystem.getAllObjects(), date);
        final long startNanoTime = System.nanoTime();
        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                double t = (currentNanoTime - startNanoTime) / 1000000000.0;
                //verletVelocity.updateLocation(1, TimeUnit.DAYS);
                dateLabel.setText(date.toDateString());
                if (t > 1) {
                    t = 0;
                    date.add(Calendar.DATE, 1);

                    Vector3D coordinate;

                    coordinate = earthObj.getHEEpos(date);
                    earth.setTranslateX(coordinate.getX() * 30 / MathUtil.AU);
                    earth.setTranslateY(coordinate.getY() * 30 * -1 / MathUtil.AU);
                    earth.setTranslateZ(coordinate.getZ() * 30 / MathUtil.AU);

                    coordinate = mercuryObj.getHEEpos(date);
                    mercury.setTranslateX(coordinate.getX() * 30 / MathUtil.AU);
                    mercury.setTranslateY(coordinate.getY() * 30 * -1 / MathUtil.AU);
                    mercury.setTranslateZ(coordinate.getZ() * 30 / MathUtil.AU);

                    coordinate = marsObj.getHEEpos(date);
                    mars.setTranslateX(coordinate.getX() * 30 / MathUtil.AU);
                    mars.setTranslateY(coordinate.getY() * 30 * -1 / MathUtil.AU);
                    mars.setTranslateZ(coordinate.getZ() * 30 / MathUtil.AU);

                    coordinate = jupiterObj.getHEEpos(date);
                    jupiter.setTranslateX(coordinate.getX() * 30 / MathUtil.AU);
                    jupiter.setTranslateY(coordinate.getY() * 30 * -1 / MathUtil.AU);
                    jupiter.setTranslateZ(coordinate.getZ() * 30 / MathUtil.AU);

                    coordinate = saturnObj.getHEEpos(date);
                    saturn.setTranslateX(coordinate.getX() * 30 / MathUtil.AU);
                    saturn.setTranslateY(coordinate.getY() * 30 * -1 / MathUtil.AU);
                    saturn.setTranslateZ(coordinate.getZ() * 30 / MathUtil.AU);

                    coordinate = titanObj.getHEEpos(date);
                    titan.setTranslateX(coordinate.getX() * 30 / MathUtil.AU);
                    titan.setTranslateY(coordinate.getY() * 30 * -1 / MathUtil.AU);
                    titan.setTranslateZ(coordinate.getZ() * 30 / MathUtil.AU);

                    coordinate = cannonballObj.getHEEpos();
                    titan.setTranslateX(coordinate.getX() * 30 / MathUtil.AU);
                    titan.setTranslateY(coordinate.getY() * 30 * -1 / MathUtil.AU);
                    titan.setTranslateZ(coordinate.getZ() * 30 / MathUtil.AU);

                    //Move the camera
                    if (goNorth) camera.setTranslateY(camera.getTranslateY() - 10);
                    if (goSouth) camera.setTranslateY(camera.getTranslateY() + 10);
                    if (goEast)  camera.setTranslateX(camera.getTranslateX() + 10);
                    if (goWest)  camera.setTranslateX(camera.getTranslateX() - 10);
                }
            }
        }.start();


        primaryStage.show();
    }
    public static void main(String args[]){
        launch(args);
    }
}
