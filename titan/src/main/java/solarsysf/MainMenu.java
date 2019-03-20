package solarsysf;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;

import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

import javafx.stage.Stage;

import solarsystem.*;
import utils.*;

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

        Date date = new Date(2000, 0, 1, 12, 0, 0);
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
        //((CannonBall) cannonballObj).setLaunchForce(new Vector3D(0, 0, 1000));
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
        Sphere sun      = new Sphere(sunObj.getRadius() / MathUtil.AU * 1000000); // sun is 100 times smaller
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
        earth.setTranslateX(earthCoordinate.getX() * 40);
        earth.setTranslateY(earthCoordinate.getY() * 40 * -1);
        earth.setTranslateZ(earthCoordinate.getZ() * 40);

        //Mercury
        Sphere mercury = new Sphere(mercuryObj.getRadius() / MathUtil.AU * 100000000);
        PhongMaterial mercuryMaterial = new PhongMaterial();
        mercuryMaterial.setDiffuseColor(Color.GREEN);
        mercuryMaterial.setSpecularColor(Color.DARKSLATEGRAY);
        mercury.setMaterial(mercuryMaterial);
        Vector3D mercuryCoordinate = earthObj.getHEEpos(date);
        mercury.setTranslateX(mercuryCoordinate.getX() * 40);
        mercury.setTranslateY(mercuryCoordinate.getY() * 40 * -1);
        mercury.setTranslateZ(mercuryCoordinate.getZ() * 40);

        //Mars
        Sphere mars = new Sphere(marsObj.getRadius() / MathUtil.AU * 100000000);
        PhongMaterial marsMaterial = new PhongMaterial();
        marsMaterial.setDiffuseColor(Color.INDIANRED);
        marsMaterial.setSpecularColor(Color.MEDIUMVIOLETRED);
        mars.setMaterial(marsMaterial);
        Vector3D marsCoordinate = marsObj.getHEEpos(date);
        mars.setTranslateX(marsCoordinate.getX() * 40);
        mars.setTranslateY(marsCoordinate.getY() * 40 * -1);
        mars.setTranslateZ(marsCoordinate.getZ() * 40);

        //Jupiter
        Sphere jupiter = new Sphere(jupiterObj.getRadius() / MathUtil.AU * 100000000);
        PhongMaterial jupiterMaterial = new PhongMaterial();
        jupiterMaterial.setDiffuseColor(Color.SANDYBROWN);
        jupiterMaterial.setSpecularColor(Color.MEDIUMVIOLETRED);
        jupiter.setMaterial(jupiterMaterial);
        Vector3D jupiterCoordinate = jupiterObj.getHEEpos(date);
        jupiter.setTranslateX(jupiterCoordinate.getX() * 40);
        jupiter.setTranslateY(jupiterCoordinate.getY() * 40 * -1);
        jupiter.setTranslateZ(jupiterCoordinate.getZ() * 40);

        //Saturn
        Sphere saturn = new Sphere(saturnObj.getRadius() / MathUtil.AU * 100000000);
        PhongMaterial saturnMaterial = new PhongMaterial();
        saturnMaterial.setDiffuseColor(Color.RED);
        saturnMaterial.setSpecularColor(Color.BLUE);
        saturn.setMaterial(saturnMaterial);
        Vector3D saturnCoordinate = saturnObj.getHEEpos(date);
        saturn.setTranslateX(saturnCoordinate.getX() * 40);
        saturn.setTranslateY(saturnCoordinate.getY() * 40 * -1);
        saturn.setTranslateZ(saturnCoordinate.getZ() * 40);

        //Titan
        Sphere titan = new Sphere(titanObj.getRadius() / MathUtil.AU * 100000000);
        PhongMaterial titanMaterial = new PhongMaterial();
        titanMaterial.setDiffuseColor(Color.WHITE);
        titanMaterial.setSpecularColor(Color.SANDYBROWN);
        titan.setMaterial(titanMaterial);
        Vector3D titanCoordinate = titanObj.getHEEpos(date);
        titan.setTranslateX(titanCoordinate.getX() * 40);
        titan.setTranslateY(titanCoordinate.getY() * 40 * -1);
        titan.setTranslateZ(titanCoordinate.getZ() * 40);

        //Uranus
        Sphere uranus = new Sphere(urAnusObj.getRadius() / MathUtil.AU * 100000000);
        PhongMaterial uranusMaterial = new PhongMaterial();
        uranusMaterial.setDiffuseColor(Color.LIGHTBLUE);
        uranusMaterial.setSpecularColor(Color.CADETBLUE);
        uranus.setMaterial(uranusMaterial);
        Vector3D uranusCoordinate = urAnusObj.getHEEpos(date);
        uranus.setTranslateX(uranusCoordinate.getX() * 40);
        uranus.setTranslateY(uranusCoordinate.getY() * 40 * -1);
        uranus.setTranslateZ(uranusCoordinate.getZ() * 40);

        //Neptune
        Sphere neptune = new Sphere(neptuneObj.getRadius() / MathUtil.AU * 100000000);
        PhongMaterial neptuneMaterial = new PhongMaterial();
        neptuneMaterial.setDiffuseColor(Color.DARKBLUE);
        neptuneMaterial.setSpecularColor(Color.DARKGRAY);
        neptune.setMaterial(neptuneMaterial);
        Vector3D neptuneCoordinate = neptuneObj.getHEEpos(date);
        neptune.setTranslateX(neptuneCoordinate.getX() * 40);
        neptune.setTranslateY(neptuneCoordinate.getY() * 40 * -1);
        neptune.setTranslateZ(neptuneCoordinate.getZ() * 40);

        //Venus
        Sphere venus = new Sphere(venusObj.getRadius() / MathUtil.AU * 100000000);
        PhongMaterial venusMaterial = new PhongMaterial();
        venusMaterial.setDiffuseColor(Color.SANDYBROWN);
        venusMaterial.setSpecularColor(Color.ROSYBROWN);
        venus.setMaterial(venusMaterial);
        Vector3D venusCoordinate = venusObj.getHEEpos(date);
        venus.setTranslateX(venusCoordinate.getX() * 40);
        venus.setTranslateY(venusCoordinate.getY() * 40 * -1);
        venus.setTranslateZ(venusCoordinate.getZ() * 40);

        //Cannonball
        Sphere cannonball = new Sphere(earthObj.getRadius() / MathUtil.AU * 1000000000);
        PhongMaterial cannonbalMaterial = new PhongMaterial();
        cannonbalMaterial.setDiffuseColor(Color.ORANGE);
        cannonball.setMaterial(cannonbalMaterial);
        Vector3D cannonballCoordinate = cannonballObj.getHEEpos();
        cannonball.setTranslateX(cannonballCoordinate.getX() * 40);
        cannonball.setTranslateY(cannonballCoordinate.getY() * 40 * -1);
        cannonball.setTranslateZ(cannonballCoordinate.getZ() * 40);

        //Date Label
        Label dateLabel = new Label(date.toDateString());
        dateLabel.setTextFill(Color.WHITE);

        //Camera
        PerspectiveCamera camera = new PerspectiveCamera(false);
        camera.setTranslateX(-400);
        camera.setTranslateY(-400);
        camera.setTranslateZ(100);

        root = new Group();
        root.getChildren().addAll(sun, earth, mercury, mars, jupiter, saturn, titan, uranus, neptune, venus, cannonball);


        AnchorPane globalRoot = new AnchorPane();
        globalRoot.getChildren().addAll(dateLabel);
        //Anchor the dateLabel
        globalRoot.setBottomAnchor(dateLabel, 10D);
        globalRoot.setRightAnchor(dateLabel, 10D);

        SubScene subScene = new SubScene(root,800,600,false, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        globalRoot.getChildren().add(subScene);


        mainScene = new Scene(globalRoot, 800, 600, true);
        globalRoot.setStyle("-fx-background-color: #000;");
        primaryStage.setTitle("SolarSysF - Solar Visualization From Scratch");
        primaryStage.setScene(mainScene);

        mainScene.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override public void handle(ScrollEvent event) {
                event.consume();

                //System.out.println(event.getDeltaY());

                if (event.getDeltaY() == 0) return;
                else if (event.getDeltaY() > 0) camera.setTranslateZ(camera.getTranslateZ() + Math.max(20, event.getDeltaY()));
                else camera.setTranslateZ(camera.getTranslateZ() + Math.min(-20, event.getDeltaY()));
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
                verletVelocity.updateLocation(12, TimeUnit.HOURS);
                dateLabel.setText(date.toDateString());
                if (t > 1) {
                    t = 0;

                    Vector3D coordinate;

                    coordinate = earthObj.getHEEpos();
                    earth.setTranslateX(coordinate.getX() * 40 / MathUtil.AU);
                    earth.setTranslateY(coordinate.getY() * 40 * -1 / MathUtil.AU);
                    earth.setTranslateZ(coordinate.getZ() * 40 / MathUtil.AU);

                    coordinate = mercuryObj.getHEEpos();
                    mercury.setTranslateX(coordinate.getX() * 40 / MathUtil.AU);
                    mercury.setTranslateY(coordinate.getY() * 40 * -1 / MathUtil.AU);
                    mercury.setTranslateZ(coordinate.getZ() * 40 / MathUtil.AU);

                    coordinate = marsObj.getHEEpos();
                    mars.setTranslateX(coordinate.getX() * 40 / MathUtil.AU);
                    mars.setTranslateY(coordinate.getY() * 40 * -1 / MathUtil.AU);
                    mars.setTranslateZ(coordinate.getZ() * 40 / MathUtil.AU);

                    coordinate = jupiterObj.getHEEpos();
                    jupiter.setTranslateX(coordinate.getX() * 40 / MathUtil.AU);
                    jupiter.setTranslateY(coordinate.getY() * 40 * -1 / MathUtil.AU);
                    jupiter.setTranslateZ(coordinate.getZ() * 40 / MathUtil.AU);

                    coordinate = saturnObj.getHEEpos();
                    saturn.setTranslateX(coordinate.getX() * 40 / MathUtil.AU);
                    saturn.setTranslateY(coordinate.getY() * 40 * -1 / MathUtil.AU);
                    saturn.setTranslateZ(coordinate.getZ() * 40 / MathUtil.AU);

                    //Titan Update
                    coordinate = titanObj.getHEEpos();
                    titan.setTranslateX(coordinate.getX() * 40 / MathUtil.AU);
                    titan.setTranslateY(coordinate.getY() * 40 * -1 / MathUtil.AU);
                    titan.setTranslateZ(coordinate.getZ() * 40 / MathUtil.AU);

                    coordinate = urAnusObj.getHEEpos();
                    uranus.setTranslateX(coordinate.getX() * 40 / MathUtil.AU);
                    uranus.setTranslateY(coordinate.getY() * 40 * -1 / MathUtil.AU);
                    uranus.setTranslateZ(coordinate.getZ() * 40 / MathUtil.AU);

                    coordinate = neptuneObj.getHEEpos();
                    neptune.setTranslateX(coordinate.getX() * 40 / MathUtil.AU);
                    neptune.setTranslateY(coordinate.getY() * 40 * -1 / MathUtil.AU);
                    neptune.setTranslateZ(coordinate.getZ() * 40 / MathUtil.AU);

                    coordinate = venusObj.getHEEpos();
                    venus.setTranslateX(coordinate.getX() * 40 / MathUtil.AU);
                    venus.setTranslateY(coordinate.getY() * 40 * -1 / MathUtil.AU);
                    venus.setTranslateZ(coordinate.getZ() * 40 / MathUtil.AU);

                    //Cannonball Update
                    /*coordinate = cannonballObj.getHEEpos();
                    cannonball.setTranslateX(coordinate.getX() * 40 / MathUtil.AU);
                    cannonball.setTranslateY(coordinate.getY() * 40 * -1 / MathUtil.AU);
                    cannonball.setTranslateZ(coordinate.getZ() * 40 / MathUtil.AU);*/

                    //System.out.println("Cannonball Positions: X:" + coordinate.getX() + " Y:" + coordinate.getY() + " Z:" + coordinate.getZ());
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
