package solarsysf;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
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
import java.util.concurrent.TimeUnit;


public class MainMenuInger extends Application {
    private final double ScFactor = 40.0 ;
    private Scene mainScene;
    private Group root;
    private boolean goNorth, goSouth, goEast, goWest;
    @Override

    // TODO: Add WASD keys to move around the zoomPane :)
    public void start(Stage primaryStage) {
        


        SolarSystem solarSystem = null;
        try {
            solarSystem = new SolarSystem();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Main Animations Handler
        VerletVelocity verletVelocity = new VerletVelocity(solarSystem.getPlanets().getAll());
        Date date = verletVelocity.getCurrentDate();


        /*
        ArrayList<ObjectInSpace> allObj =
                new ArrayList<>(solarSystem.getPlanets().getAll());
        ObjectInSpace cannonballObj = new CannonBall(20, 0.01,
                solarSystem.getPlanets().getEarth(),
                solarSystem.getPlanets().getMars());
        //((CannonBall) cannonballObj).setLaunchForce(new Vector3D(0, 0, 1000));
        cannonballObj.initializeCartesianCoordinates(date);
        allObj.add(cannonballObj);
        solarSystem.setAllObjects(allObj);
        */


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
        Sphere sun = new Sphere(sunObj.getRadius() * ScFactor); // sun is 100 times smaller
        PhongMaterial sunMaterial = new PhongMaterial();
        sunMaterial.setDiffuseColor(Color.ORANGE);
        sunMaterial.setSpecularColor(Color.ORANGERED);
        sun.setMaterial(sunMaterial);
        sun.setTranslateX(0);
        sun.setTranslateZ(0);
        sun.setTranslateY(0);

        //Earth
        Sphere earth    = new Sphere(scale(earthObj));
        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseColor(Color.DARKSEAGREEN);
        earthMaterial.setSpecularColor(Color.GREEN);
        earth.setMaterial(earthMaterial);
        Vector3D earthCoordinate = earthObj.getHEEpos();
        earth.setTranslateX(earthCoordinate.getX() * ScFactor);
        earth.setTranslateY(earthCoordinate.getY() * ScFactor * -1);
        earth.setTranslateZ(earthCoordinate.getZ() * ScFactor);

        //Mercury
        Sphere mercury = new Sphere(scale(mercuryObj));
        PhongMaterial mercuryMaterial = new PhongMaterial();
        mercuryMaterial.setDiffuseColor(Color.GREEN);
        mercuryMaterial.setSpecularColor(Color.DARKSLATEGRAY);
        mercury.setMaterial(mercuryMaterial);
        Vector3D mercuryCoordinate = earthObj.getHEEpos();
        mercury.setTranslateX(mercuryCoordinate.getX() * ScFactor);
        mercury.setTranslateY(mercuryCoordinate.getY() * ScFactor * -1);
        mercury.setTranslateZ(mercuryCoordinate.getZ() * ScFactor);

        //Mars
        Sphere mars = new Sphere(scale(marsObj));
        PhongMaterial marsMaterial = new PhongMaterial();
        marsMaterial.setDiffuseColor(Color.INDIANRED);
        marsMaterial.setSpecularColor(Color.MEDIUMVIOLETRED);
        mars.setMaterial(marsMaterial);
        Vector3D marsCoordinate = marsObj.getHEEpos();
        mars.setTranslateX(marsCoordinate.getX() * ScFactor);
        mars.setTranslateY(marsCoordinate.getY() * ScFactor * -1);
        mars.setTranslateZ(marsCoordinate.getZ() * ScFactor);

        //Jupiter
        Sphere jupiter = new Sphere(scale(jupiterObj));
        PhongMaterial jupiterMaterial = new PhongMaterial();
        jupiterMaterial.setDiffuseColor(Color.SANDYBROWN);
        jupiterMaterial.setSpecularColor(Color.MEDIUMVIOLETRED);
        jupiter.setMaterial(jupiterMaterial);
        Vector3D jupiterCoordinate = jupiterObj.getHEEpos();
        jupiter.setTranslateX(jupiterCoordinate.getX() * ScFactor);
        jupiter.setTranslateY(jupiterCoordinate.getY() * ScFactor * -1);
        jupiter.setTranslateZ(jupiterCoordinate.getZ() * ScFactor);

        //Saturn
        Sphere saturn = new Sphere(scale(saturnObj));
        PhongMaterial saturnMaterial = new PhongMaterial();
        saturnMaterial.setDiffuseColor(Color.RED);
        saturnMaterial.setSpecularColor(Color.BLUE);
        saturn.setMaterial(saturnMaterial);
        Vector3D saturnCoordinate = saturnObj.getHEEpos();
        saturn.setTranslateX(saturnCoordinate.getX() * ScFactor);
        saturn.setTranslateY(saturnCoordinate.getY() * ScFactor * -1);
        saturn.setTranslateZ(saturnCoordinate.getZ() * ScFactor);

        //Titan
        Sphere titan = new Sphere(scale(titanObj));
        PhongMaterial titanMaterial = new PhongMaterial();
        titanMaterial.setDiffuseColor(Color.WHITE);
        titanMaterial.setSpecularColor(Color.SANDYBROWN);
        titan.setMaterial(titanMaterial);
        Vector3D titanCoordinate = titanObj.getHEEpos();
        titan.setTranslateX(titanCoordinate.getX() * ScFactor);
        titan.setTranslateY(titanCoordinate.getY() * ScFactor* -1);
        titan.setTranslateZ(titanCoordinate.getZ() * ScFactor);

        //Uranus
        Sphere uranus = new Sphere(scale(urAnusObj));
        PhongMaterial uranusMaterial = new PhongMaterial();
        uranusMaterial.setDiffuseColor(Color.LIGHTBLUE);
        uranusMaterial.setSpecularColor(Color.CADETBLUE);
        uranus.setMaterial(uranusMaterial);
        Vector3D uranusCoordinate = urAnusObj.getHEEpos();
        uranus.setTranslateX(uranusCoordinate.getX() * ScFactor);
        uranus.setTranslateY(uranusCoordinate.getY() * ScFactor* -1);
        uranus.setTranslateZ(uranusCoordinate.getZ() * ScFactor);

        //Neptune
        Sphere neptune = new Sphere(scale(neptuneObj));
        PhongMaterial neptuneMaterial = new PhongMaterial();
        neptuneMaterial.setDiffuseColor(Color.DARKBLUE);
        neptuneMaterial.setSpecularColor(Color.DARKGRAY);
        neptune.setMaterial(neptuneMaterial);
        Vector3D neptuneCoordinate = neptuneObj.getHEEpos();
        neptune.setTranslateX(neptuneCoordinate.getX() * ScFactor);
        neptune.setTranslateY(neptuneCoordinate.getY() * ScFactor * -1);
        neptune.setTranslateZ(neptuneCoordinate.getZ() * ScFactor);

        //Venus
        Sphere venus = new Sphere(scale(venusObj));
        PhongMaterial venusMaterial = new PhongMaterial();
        venusMaterial.setDiffuseColor(Color.SANDYBROWN);
        venusMaterial.setSpecularColor(Color.ROSYBROWN);
        venus.setMaterial(venusMaterial);
        Vector3D venusCoordinate = venusObj.getHEEpos();
        venus.setTranslateX(venusCoordinate.getX() * ScFactor);
        venus.setTranslateY(venusCoordinate.getY() * ScFactor* -1);
        venus.setTranslateZ(venusCoordinate.getZ() * ScFactor);

        /*
        //Cannonball
        Sphere cannonball = new Sphere(earthObj.getRadius() * ScFactor / MathUtil.ScFactor * 1000000000);
        PhongMaterial cannonbalMaterial = new PhongMaterial();
        cannonbalMaterial.setDiffuseColor(Color.ORANGE);
        cannonball.setMaterial(cannonbalMaterial);
        Vector3D cannonballCoordinate = cannonballObj.getHEEpos();
        cannonball.setTranslateX(cannonballCoordinate.getX() * ScFactor * 40);
        cannonball.setTranslateY(cannonballCoordinate.getY() * ScFactor * 40 * -1);
        cannonball.setTranslateZ(cannonballCoordinate.getZ() * ScFactor * 40);
        */


        //Date Label
        Label dateLabel = new Label(date.toDateString());
        dateLabel.setTextFill(Color.WHITE);


        //Camera
        PerspectiveCamera camera = new PerspectiveCamera(false);
        camera.setTranslateX(-400);
        camera.setTranslateY(-400);
        camera.setTranslateZ(100);

        root = new Group();
        root.getChildren().addAll(sun, earth, mercury, mars, jupiter, saturn, titan, uranus, neptune, venus);


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

        final long startNanoTime = System.nanoTime();
        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                double t = (currentNanoTime - startNanoTime) / 1000000000.0;
                verletVelocity.updateLocation(1, TimeUnit.DAYS);
                dateLabel.setText(date.toDateString());
                if (t > 1) {
                    t = 0;

                    Vector3D coordinate;

                    coordinate = earthObj.getHEEpos();
                    earth.setTranslateX(coordinate.getX() * scale(earthObj));
                    earth.setTranslateY(coordinate.getY() * scale(earthObj) *-1);
                    earth.setTranslateZ(coordinate.getZ() * scale(earthObj ));

                    coordinate = mercuryObj.getHEEpos();
                    mercury.setTranslateX(coordinate.getX() * scale(mercuryObj) );
                    mercury.setTranslateY(coordinate.getY() * scale(mercuryObj) *-1);
                    mercury.setTranslateZ(coordinate.getZ() * scale(mercuryObj ));

                    coordinate = marsObj.getHEEpos();
                    mars.setTranslateX(coordinate.getX() * scale(marsObj) );
                    mars.setTranslateY(coordinate.getY() * scale(marsObj) *-1);
                    mars.setTranslateZ(coordinate.getZ() * scale(marsObj) );

                    coordinate = jupiterObj.getHEEpos();
                    jupiter.setTranslateX(coordinate.getX() * scale(jupiterObj) );
                    jupiter.setTranslateY(coordinate.getY() * scale(jupiterObj) *-1);
                    jupiter.setTranslateZ(coordinate.getZ() * scale(jupiterObj) );

                    coordinate = saturnObj.getHEEpos();
                    //System.out.println(date.toDateString()+ ", S: " + coordinate);
                    saturn.setTranslateX(coordinate.getX() * scale(saturnObj) );
                    saturn.setTranslateY(coordinate.getY() * scale(saturnObj) *-1);
                    saturn.setTranslateZ(coordinate.getZ() * scale(saturnObj) );

                    //Titan Update
                    coordinate = titanObj.getHEEpos();
                    //System.out.println(date.toDateString()+ ", T: " + coordinate);
                    titan.setTranslateX(coordinate.getX() * scale(titanObj) );
                    titan.setTranslateY(coordinate.getY() * scale(titanObj) *-1);
                    titan.setTranslateZ(coordinate.getZ() * scale(titanObj) );

                    coordinate = urAnusObj.getHEEpos();
                    uranus.setTranslateX(coordinate.getX() * scale(urAnusObj) );
                    uranus.setTranslateY(coordinate.getY() * scale(urAnusObj) *-1);
                    uranus.setTranslateZ(coordinate.getZ() * scale(urAnusObj) );

                    coordinate = neptuneObj.getHEEpos();
                    neptune.setTranslateX(coordinate.getX() * scale(neptuneObj) );
                    neptune.setTranslateY(coordinate.getY() * scale(neptuneObj) *-1);
                    neptune.setTranslateZ(coordinate.getZ() * scale(neptuneObj) );

                    coordinate = venusObj.getHEEpos();
                    venus.setTranslateX(coordinate.getX() * scale(venusObj) );
                    venus.setTranslateY(coordinate.getY() * scale(venusObj) *-1);
                    venus.setTranslateZ(coordinate.getZ() * scale(venusObj) );

                    //Cannonball Update
                    /*coordinate = cannonballObj.getHEEpos();
                    cannonball.setTranslateX(coordinate.getX() * ScFactor );
                    cannonball.setTranslateY(coordinate.getY() * ScFactor *-1);
                    cannonball.setTranslateZ(coordinate.getZ() * ScFactor );*/

                    //System.out.println("Cannonball Positions: X:" + coordinate.getX() * ScFactor + " Y:" + coordinate.getY() * ScFactor + " Z:" + coordinate.getZ() * ScFactor);
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
    
    private double scale(CelestialObjects objectInSpace){
        return Math.pow(objectInSpace.getSemiMajorAxisJ2000(), (1/5D)) / MathUtil.AU;
    }
}
