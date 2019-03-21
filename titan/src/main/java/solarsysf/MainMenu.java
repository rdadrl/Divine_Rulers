package solarsysf;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;

import javafx.scene.*;
import javafx.scene.control.Label;

import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
import java.util.concurrent.TimeUnit;


public class MainMenu extends Application {
    private Scene mainScene;
    private Group root;
    private boolean goNorth, goSouth, goEast, goWest;
    private boolean pauseStatus = false;
    private final boolean USELIGHTS = false;
    private final int DistanceMultiplier = 40;
    @Override
    public void start(Stage primaryStage) {

        Date date = new Date(2010, 0, 1, 12, 0, 0);
        SolarSystem solarSystem = null;
        try {
            solarSystem = new SolarSystem();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<CelestialObject> allObj =
                new ArrayList<>(solarSystem.getPlanets().getAll());
        CelestialObject cannonballObj = new CannonBall(20, 1,
                solarSystem.getPlanets().getEarth(),
                solarSystem.getPlanets().getMars());
        //((CannonBall) cannonballObj).setLaunchForce(new Vector3D(0, 0, 1000));
        cannonballObj.initializeCartesianCoordinates(date);
        allObj.add(cannonballObj);
        solarSystem.setAllObjects(allObj);

        Planet earthObj = solarSystem.getPlanets().getEarth();
        Planet sunObj = solarSystem.getPlanets().getSun();
        Planet mercuryObj = solarSystem.getPlanets().getMercury();
        Planet marsObj = solarSystem.getPlanets().getMars();
        Planet jupiterObj = solarSystem.getPlanets().getJupiter();
        Planet saturnObj = solarSystem.getPlanets().getSaturn();
        Planet titanObj = solarSystem.getPlanets().getTitan();
        Planet urAnusObj = solarSystem.getPlanets().getUranus();
        Planet neptuneObj = solarSystem.getPlanets().getNeptune();
        Planet venusObj = solarSystem.getPlanets().getVenus();

        //Planet Name Label
        Label identifierLabel = new Label();
        identifierLabel.setTextFill(Color.WHITE);

        // Sun
        Sphere sun      = new Sphere(sunObj.getRadius() / MathUtil.AU * 1000000); // sun is 100 times smaller
        PhongMaterial sunMaterial = new PhongMaterial();

        sunMaterial.setDiffuseMap(new Image("textures/sunmap.jpg"));

        sun.setMaterial(sunMaterial);
        sun.setTranslateX(0);
        sun.setTranslateZ(0);
        sun.setTranslateY(0);

        PointLight pointLight = null;
        AmbientLight ambientLight = null;
        if (USELIGHTS) {
            pointLight = new PointLight(Color.WHITE);
            pointLight.setTranslateX(0);
            pointLight.setTranslateY(0);
            pointLight.setTranslateZ(0);
            ambientLight = new AmbientLight();
        }

        sun.setOnMouseEntered(new EventHandler<MouseEvent> () {
            @Override
            public void handle(MouseEvent t) {
                Vector3D coordinate = sunObj.getHEEpos();
                //limit decimals to 3 points
                float sX = ((int) (coordinate.getX() * 1000)) / 1000F;
                float sY = ((int) (coordinate.getY() * 1000)) / 1000F;
                float sZ = ((int) (coordinate.getZ() * 1000)) / 1000F;

                identifierLabel.setText("Sun:\nX: " + sX + "\nY: " + sY + "\nZ: " + sZ);
            }
        });

        sun.setOnMouseExited(new EventHandler<MouseEvent> () {
            @Override
            public void handle(MouseEvent t) {
                identifierLabel.setText("");
            }
        });

        //Earth
        Sphere earth    = new Sphere(earthObj.getRadius() / MathUtil.AU * 100000000);
        PhongMaterial earthMaterial = new PhongMaterial();

        earthMaterial.setDiffuseMap(new Image("textures/earthmap.jpg"));
        earthMaterial.setBumpMap(new Image("textures/earthbump.jpg"));
        earthMaterial.setSpecularMap(new Image("textures/earthspecular.jpg"));

        earth.setMaterial(earthMaterial);
        Vector3D earthCoordinate = earthObj.getHEEpos(date);
        earth.setTranslateX(earthCoordinate.getX() * DistanceMultiplier);
        earth.setTranslateY(earthCoordinate.getY() * DistanceMultiplier * -1);
        earth.setTranslateZ(earthCoordinate.getZ() * DistanceMultiplier);

        earth.setOnMouseEntered(new EventHandler<MouseEvent> () {
            @Override
            public void handle(MouseEvent t) {
                Vector3D coordinate = earthObj.getHEEpos();
                //limit decimals to 3 points
                float sX = ((int) (coordinate.getX() * 1000)) / 1000F;
                float sY = ((int) (coordinate.getY() * 1000)) / 1000F;
                float sZ = ((int) (coordinate.getZ() * 1000)) / 1000F;

                identifierLabel.setText("Earth:\nX: " + sX + "\nY: " + sY + "\nZ: " + sZ);
            }
        });

        earth.setOnMouseExited(new EventHandler<MouseEvent> () {
            @Override
            public void handle(MouseEvent t) {
                identifierLabel.setText("");
            }
        });

        //Mercury
        Sphere mercury = new Sphere(mercuryObj.getRadius() / MathUtil.AU * 100000000);
        PhongMaterial mercuryMaterial = new PhongMaterial();

        mercuryMaterial.setDiffuseMap(new Image("textures/mercurymap.jpg"));
        mercuryMaterial.setBumpMap(new Image("textures/mercurybump.jpg"));

        mercury.setMaterial(mercuryMaterial);
        Vector3D mercuryCoordinate = earthObj.getHEEpos(date);
        mercury.setTranslateX(mercuryCoordinate.getX() * DistanceMultiplier);
        mercury.setTranslateY(mercuryCoordinate.getY() * DistanceMultiplier * -1);
        mercury.setTranslateZ(mercuryCoordinate.getZ() * DistanceMultiplier);

        //Mars
        Sphere mars = new Sphere(marsObj.getRadius() / MathUtil.AU * 100000000);
        PhongMaterial marsMaterial = new PhongMaterial();

        marsMaterial.setDiffuseMap(new Image("textures/marsmap.jpg"));
        marsMaterial.setBumpMap(new Image("textures/marsbump.jpg"));

        mars.setMaterial(marsMaterial);
        Vector3D marsCoordinate = marsObj.getHEEpos(date);
        mars.setTranslateX(marsCoordinate.getX() * DistanceMultiplier);
        mars.setTranslateY(marsCoordinate.getY() * DistanceMultiplier * -1);
        mars.setTranslateZ(marsCoordinate.getZ() * DistanceMultiplier);

        //Jupiter
        Sphere jupiter = new Sphere(jupiterObj.getRadius() / MathUtil.AU * 100000000);
        PhongMaterial jupiterMaterial = new PhongMaterial();

        jupiterMaterial.setDiffuseMap(new Image("textures/jupitermap.jpg"));

        jupiter.setMaterial(jupiterMaterial);
        Vector3D jupiterCoordinate = jupiterObj.getHEEpos(date);
        jupiter.setTranslateX(jupiterCoordinate.getX() * DistanceMultiplier);
        jupiter.setTranslateY(jupiterCoordinate.getY() * DistanceMultiplier * -1);
        jupiter.setTranslateZ(jupiterCoordinate.getZ() * DistanceMultiplier);

        //Saturn
        Sphere saturn = new Sphere(saturnObj.getRadius() / MathUtil.AU * 100000000);
        PhongMaterial saturnMaterial = new PhongMaterial();

        saturnMaterial.setDiffuseMap(new Image("textures/saturnmap.jpg"));

        saturn.setMaterial(saturnMaterial);
        Vector3D saturnCoordinate = saturnObj.getHEEpos(date);
        saturn.setTranslateX(saturnCoordinate.getX() * DistanceMultiplier);
        saturn.setTranslateY(saturnCoordinate.getY() * DistanceMultiplier * -1);
        saturn.setTranslateZ(saturnCoordinate.getZ() * DistanceMultiplier);

        //Titan
        Sphere titan = new Sphere(titanObj.getRadius() / MathUtil.AU * 100000000);
        PhongMaterial titanMaterial = new PhongMaterial();

        titanMaterial.setDiffuseMap(new Image("textures/moonmap.jpg"));
        titanMaterial.setBumpMap(new Image("textures/moonbump.jpg"));

        titan.setMaterial(titanMaterial);
        Vector3D titanCoordinate = titanObj.getHEEpos(date);
        titan.setTranslateX(titanCoordinate.getX() * DistanceMultiplier);
        titan.setTranslateY(titanCoordinate.getY() * DistanceMultiplier * -1);
        titan.setTranslateZ(titanCoordinate.getZ() * DistanceMultiplier);

        //Uranus
        Sphere uranus = new Sphere(urAnusObj.getRadius() / MathUtil.AU * 100000000);
        PhongMaterial uranusMaterial = new PhongMaterial();

        uranusMaterial.setDiffuseMap(new Image("textures/uranusmap.jpg"));

        uranus.setMaterial(uranusMaterial);
        Vector3D uranusCoordinate = urAnusObj.getHEEpos(date);
        uranus.setTranslateX(uranusCoordinate.getX() * DistanceMultiplier);
        uranus.setTranslateY(uranusCoordinate.getY() * DistanceMultiplier * -1);
        uranus.setTranslateZ(uranusCoordinate.getZ() * DistanceMultiplier);

        //Neptune
        Sphere neptune = new Sphere(neptuneObj.getRadius() / MathUtil.AU * 100000000);
        PhongMaterial neptuneMaterial = new PhongMaterial();

        neptuneMaterial.setDiffuseMap(new Image("textures/neptunemap.jpg"));

        neptune.setMaterial(neptuneMaterial);
        Vector3D neptuneCoordinate = neptuneObj.getHEEpos(date);
        neptune.setTranslateX(neptuneCoordinate.getX() * DistanceMultiplier);
        neptune.setTranslateY(neptuneCoordinate.getY() * DistanceMultiplier * -1);
        neptune.setTranslateZ(neptuneCoordinate.getZ() * DistanceMultiplier);

        //Venus
        Sphere venus = new Sphere(venusObj.getRadius() / MathUtil.AU * 100000000);
        PhongMaterial venusMaterial = new PhongMaterial();

        venusMaterial.setDiffuseMap(new Image("textures/venusmap.jpg"));
        venusMaterial.setBumpMap(new Image("textures/venusbump.jpg"));

        venus.setMaterial(venusMaterial);
        Vector3D venusCoordinate = venusObj.getHEEpos(date);
        venus.setTranslateX(venusCoordinate.getX() * DistanceMultiplier);
        venus.setTranslateY(venusCoordinate.getY() * DistanceMultiplier * -1);
        venus.setTranslateZ(venusCoordinate.getZ() * DistanceMultiplier);

        //Cannonball
        Sphere cannonball = new Sphere(earthObj.getRadius() / MathUtil.AU * 300000000);
        PhongMaterial cannonbalMaterial = new PhongMaterial();
        cannonbalMaterial.setDiffuseColor(Color.PURPLE);
        cannonball.setMaterial(cannonbalMaterial);
        Vector3D cannonballCoordinate = cannonballObj.getHEEpos();
        cannonball.setTranslateX(earthCoordinate.getX() * DistanceMultiplier);
        cannonball.setTranslateY(earthCoordinate.getY() * DistanceMultiplier * -1);
        cannonball.setTranslateZ(earthCoordinate.getZ() * DistanceMultiplier);

        //Date Label
        Label dateLabel = new Label(date.toDateString());
        dateLabel.setTextFill(Color.WHITE);

        //Camera
        PerspectiveCamera camera = new PerspectiveCamera(false);
        camera.setTranslateX(-400);
        camera.setTranslateY(-400);
        camera.setTranslateZ(100);

        root = new Group();
        if (USELIGHTS) root.getChildren().addAll(pointLight, ambientLight);
        root.getChildren().addAll(sun, earth, mercury, mars, jupiter, saturn, titan, uranus, neptune, venus, cannonball);


        AnchorPane globalRoot = new AnchorPane();
        globalRoot.getChildren().addAll(dateLabel, identifierLabel);
        //Anchor the dateLabel
        globalRoot.setBottomAnchor(dateLabel, 10D);
        globalRoot.setRightAnchor(dateLabel, 10D);
        globalRoot.setLeftAnchor(identifierLabel, 10D);
        globalRoot.setTopAnchor(identifierLabel, 10D);

        SubScene subScene = new SubScene(root,800,600,false, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        globalRoot.getChildren().add(subScene);


        mainScene = new Scene(globalRoot, 800, 600, true);
        globalRoot.setStyle("-fx-background-image: url(\"textures/galaxy_starfield.png\");");
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
                if (!pauseStatus) {
                    verletVelocity.updateLocation(12, TimeUnit.HOURS);
                    dateLabel.setText(date.toDateString());

                    Vector3D coordinate;

                    coordinate = earthObj.getHEEpos();
                    earth.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    earth.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    earth.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);

                    coordinate = mercuryObj.getHEEpos();
                    mercury.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    mercury.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    mercury.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);

                    coordinate = marsObj.getHEEpos();
                    mars.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    mars.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    mars.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);

                    coordinate = jupiterObj.getHEEpos();
                    jupiter.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    jupiter.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    jupiter.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);

                    coordinate = saturnObj.getHEEpos();
                    saturn.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    saturn.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    saturn.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);

                    //Titan Update
                    coordinate = titanObj.getHEEpos();
                    titan.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    titan.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    titan.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);

                    coordinate = urAnusObj.getHEEpos();
                    uranus.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    uranus.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    uranus.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);

                    coordinate = neptuneObj.getHEEpos();
                    neptune.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    neptune.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    neptune.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);

                    coordinate = venusObj.getHEEpos();
                    venus.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    venus.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    venus.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);

                    //Cannonball Update
                    coordinate = cannonballObj.getHEEpos();
                    cannonball.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    cannonball.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    cannonball.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);

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
