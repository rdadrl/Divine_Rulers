package solarsysf;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import physics.VerletVelocity;
import solarsystem.CannonBall;
import solarsystem.CelestialObject;
import solarsystem.Planet;
import solarsystem.SolarSystem;
import utils.Date;
import utils.MathUtil;
import utils.Vector3D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class MainMenuE10 extends Application {
    // runing variables
    private final boolean RUNFRAMEFORFRAME = false;
    private final boolean Rocket = true;

    // Timing variables
    private Date date = new Date(2002, 9, 10, 12, 0, 0);
    private final long dt = 6;
    private final TimeUnit timeUnit = TimeUnit.HOURS;


    /*
    private double cannonballMin = 95.24313;
    private double cannonballMax = 95.24318;
    private double inclinationMin = 38.74850;
    private double inclinationMax = 38.74855;
    */

    private double cannonballMin = 94.4;
    private double cannonballMax = 94.9;
    private double inclinationMin = 36.59;
    private double inclinationMax = 36.71;

    // gui variables
    private Scene mainScene;
    private Group root;
    private boolean goNorth, goSouth, goEast, goWest;
    private boolean pauseStatus = false;
    private final boolean USELIGHTS = true;


    private HashMap<Sphere, CelestialObject> spaceObjectsList = new HashMap<>();
    private SolarSystem solarSystem;
    private final int DistanceMultiplier = 40;
    private final int plntRadFact = 10000000;
    private double AU = MathUtil.AU;

    @Override
    public void start(Stage primaryStage) {
        try {
            solarSystem = new SolarSystem();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        //Cannonball Debug Label
        Label cannonballLabel = new Label();
        cannonballLabel.setTextFill(Color.WHITE);

        //Pause/Play button
        Hyperlink pausePlayButton = new Hyperlink("▶");//("❚❚");
        pausePlayButton.setTextFill(Color.WHITE);
        pausePlayButton.setBorder(null);
        pausePlayButton.setOnAction(e -> pauseStatus = !pauseStatus );

        //Light fx
        PointLight pointLight = null;
        AmbientLight ambientLight = null;
        if (USELIGHTS) {
            pointLight = new PointLight(Color.WHITE);
            pointLight.setTranslateX(0);
            pointLight.setTranslateY(0);
            pointLight.setTranslateZ(0);
            ambientLight = new AmbientLight();
        }

        Rotate rxBox = new Rotate(90, 0, 0, 0, Rotate.X_AXIS);
        // Sun
        Sphere sun  = new Sphere(sunObj.getRadius() / MathUtil.AU * 1000000); // sun is 100 times smaller
        PhongMaterial sunMaterial = new PhongMaterial();
        sunMaterial.setDiffuseMap(new Image("textures/sunmap.jpg"));
        sun.setMaterial(sunMaterial);
        sun.getTransforms().add(rxBox);
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
        sun.setOnMouseExited(e -> identifierLabel.setText(""));

        //Earth
        Sphere earth    = new Sphere(earthObj.getRadius() / MathUtil.AU * plntRadFact);
        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseMap(new Image("textures/earthmap.jpg"));
        earthMaterial.setBumpMap(new Image("textures/earthbump.jpg"));
        //earthMaterial.setSpecularMap(new Image("textures/earthspecular.jpg"));
        earth.setMaterial(earthMaterial);
        earth.getTransforms().add(rxBox);
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
        earth.setOnMouseExited(e -> identifierLabel.setText(""));

        spaceObjectsList = new HashMap<>();


        //Mercury
        Sphere mercury = new Sphere(mercuryObj.getRadius() / MathUtil.AU * plntRadFact);
        PhongMaterial mercuryMaterial = new PhongMaterial();
        mercuryMaterial.setDiffuseMap(new Image("textures/mercurymap.jpg"));
        mercuryMaterial.setBumpMap(new Image("textures/mercurybump.jpg"));
        mercury.setMaterial(mercuryMaterial);
        mercury.getTransforms().add(rxBox);

        //Mars
        Sphere mars = new Sphere(marsObj.getRadius() / MathUtil.AU * plntRadFact);
        PhongMaterial marsMaterial = new PhongMaterial();
        marsMaterial.setDiffuseMap(new Image("textures/marsmap.jpg"));
        marsMaterial.setBumpMap(new Image("textures/marsbump.jpg"));
        mars.setMaterial(marsMaterial);
        mars.getTransforms().add(rxBox);

        //Jupiter
        Sphere jupiter = new Sphere(jupiterObj.getRadius() / MathUtil.AU * plntRadFact);
        PhongMaterial jupiterMaterial = new PhongMaterial();
        jupiterMaterial.setDiffuseMap(new Image("textures/jupitermap.jpg"));
        jupiter.setMaterial(jupiterMaterial);
        jupiter.getTransforms().add(rxBox);

        //Saturn
        Sphere saturn = new Sphere(saturnObj.getRadius() / MathUtil.AU * plntRadFact);
        PhongMaterial saturnMaterial = new PhongMaterial();
        saturnMaterial.setDiffuseMap(new Image("textures/saturnmap.jpg"));
        saturn.setMaterial(saturnMaterial);
        saturn.rotateProperty();
        saturn.getTransforms().addAll(rxBox);

        //Titan
        Sphere titan = new Sphere(titanObj.getRadius() / MathUtil.AU * plntRadFact);
        PhongMaterial titanMaterial = new PhongMaterial();
        titanMaterial.setDiffuseMap(new Image("textures/moonmap.jpg"));
        titanMaterial.setBumpMap(new Image("textures/moonbump.jpg"));
        titan.setMaterial(titanMaterial);
        titan.getTransforms().add(rxBox);

        //Uranus
        Sphere uranus = new Sphere(urAnusObj.getRadius() / MathUtil.AU * plntRadFact);
        PhongMaterial uranusMaterial = new PhongMaterial();
        uranusMaterial.setDiffuseMap(new Image("textures/uranusmap.jpg"));
        uranus.setMaterial(uranusMaterial);
        uranus.getTransforms().add(rxBox);

        //Neptune
        Sphere neptune = new Sphere(neptuneObj.getRadius() / MathUtil.AU * plntRadFact);
        PhongMaterial neptuneMaterial = new PhongMaterial();
        neptuneMaterial.setDiffuseMap(new Image("textures/neptunemap.jpg"));
        neptune.setMaterial(neptuneMaterial);
        neptune.getTransforms().add(rxBox);

        //Venus
        Sphere venus = new Sphere(venusObj.getRadius() / MathUtil.AU * plntRadFact);
        PhongMaterial venusMaterial = new PhongMaterial();
        venusMaterial.setDiffuseMap(new Image("textures/venusmap.jpg"));
        venusMaterial.setBumpMap(new Image("textures/venusbump.jpg"));
        venus.setMaterial(venusMaterial);
        venus.getTransforms().add(rxBox);


        //Cannonball
        if(Rocket) {
            for (int i = 0; i < 1000; i++) {
                Random r = new Random();
                /*
                double xv = -cannonballRange + (cannonballRange + cannonballRange) * r.nextDouble();
                double yv = -cannonballRange + (cannonballRange + cannonballRange) * r.nextDouble();
                double zv = -cannonballRange + (cannonballRange + cannonballRange) * r.nextDouble();
                */

                Double velocity = cannonballMin + (cannonballMax - cannonballMin) * r.nextDouble();
                Double inclination = inclinationMin + (inclinationMax - inclinationMin) * r.nextDouble();
                //Double inclination = ThreadLocalRandom.current().nextDouble(0, inclinationRange);
                CelestialObject cannonballObj = new CannonBall(100, 2000,
                        solarSystem.getPlanets().getEarth(),
                        solarSystem.getPlanets().getTitan(), date,
                        inclination, velocity);
                        //titanObj.getHEEpos().substract(earthObj.getHEEpos()).unit().scale
                // (velocity));
                spaceObjectsList.put(createGUIobject(cannonballObj), cannonballObj);
            }
        }

        /*Cannonball
        for(int i = 0; i < 1; i++){
            Random r = new Random();
            CelestialObject rocketObj = new rocket(1000, 1000,
                    solarSystem.getPlanets().getEarth(),
                    solarSystem.getPlanets().getTitan(), date, r.nextDouble()*1);
            spaceObjectsList.put(createGUIobject(rocketObj), rocketObj);
        }*/

        //Date Label
        Label dateLabel = new Label(date.toDateString());
        dateLabel.setTextFill(Color.WHITE);

        //Camera
        PerspectiveCamera camera = new PerspectiveCamera(false);
        camera.setTranslateX(-400);
        camera.setTranslateY(-400);
        camera.setTranslateZ(100);

        //root has the celestialobjects and the lights.
        root = new Group();
        if (USELIGHTS) root.getChildren().addAll(pointLight, ambientLight);
        root.getChildren().addAll(sun, earth, mercury, mars, jupiter, saturn, titan, uranus, neptune, venus);
        root.getChildren().addAll(spaceObjectsList.keySet());


        AnchorPane globalRoot = new AnchorPane();
        globalRoot.getChildren().addAll(dateLabel, identifierLabel, pausePlayButton, cannonballLabel);
        //Anchor the dateLabel
        globalRoot.setBottomAnchor(dateLabel, 10D);
        globalRoot.setRightAnchor(dateLabel, 10D);
        globalRoot.setLeftAnchor(identifierLabel, 10D);
        globalRoot.setTopAnchor(identifierLabel, 10D);
        globalRoot.setLeftAnchor(cannonballLabel, 10D);
        globalRoot.setBottomAnchor(cannonballLabel, 10D);

        SubScene subScene = new SubScene(root,800,600,false, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        globalRoot.getChildren().add(subScene);


        mainScene = new Scene(globalRoot, 800, 600, true);
        globalRoot.setStyle("-fx-background-image: url(\"textures/galaxy_starfield.png\");");
        primaryStage.setTitle("SolarSysF - Solar Visualization From Scratch");
        primaryStage.setScene(mainScene);


        ArrayList<CelestialObject> allObj =
                new ArrayList<>(spaceObjectsList.values());
        allObj.addAll(solarSystem.getPlanets().getAll());
        solarSystem.setAllObjects(allObj);

        VerletVelocity verletVelocity = new VerletVelocity(solarSystem.getAllObjects(), date);


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
                if (!pauseStatus ) {
                    verletVelocity.updateLocation(dt, timeUnit);
                    dateLabel.setText(date.toDateString());

                    Vector3D coordinate;

                    coordinate = sunObj.getHEEpos();
                    sun.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    sun.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    sun.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);
                    sun.setRotate(sun.getRotate() + 2);
                    //System.out.println("Sun:\nX: " + coordinate.getX() + "\nY: " + coordinate// .getY() + "\nZ: " + coordinate.getZ());

                    coordinate = earthObj.getHEEpos();
                    earth.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    earth.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    earth.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);
                    earth.setRotate(earth.getRotate() + 20);

                    coordinate = mercuryObj.getHEEpos();
                    mercury.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    mercury.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    mercury.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);
                    mercury.setRotate(mercury.getRotate() + 20);

                    coordinate = marsObj.getHEEpos();
                    mars.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    mars.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    mars.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);
                    mars.setRotate(mars.getRotate() + 20);

                    coordinate = jupiterObj.getHEEpos();
                    jupiter.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    jupiter.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    jupiter.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);
                    jupiter.setRotate(jupiter.getRotate() + 2);

                    coordinate = saturnObj.getHEEpos();
                    saturn.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    saturn.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    saturn.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);
                    saturn.setRotate(saturn.getRotate() + 2);

                    //Titan Update
                    coordinate = titanObj.getHEEpos();
                    titan.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    titan.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    titan.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);
                    titan.setRotate(titan.getRotate() + 20);

                    coordinate = urAnusObj.getHEEpos();
                    uranus.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    uranus.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    uranus.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);
                    uranus.setRotate(uranus.getRotate() + 20);

                    coordinate = neptuneObj.getHEEpos();
                    neptune.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    neptune.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    neptune.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);
                    neptune.setRotate(neptune.getRotate() + 20);

                    coordinate = venusObj.getHEEpos();
                    venus.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    venus.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    venus.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);
                    venus.setRotate(venus.getRotate() + 20);

                    for(Sphere guiObject: spaceObjectsList.keySet()){
                        updateGUIobject(guiObject, spaceObjectsList.get(guiObject));
                    }


                    /*
                    //Cannonball Update
                    coordinate = cannonballObj.getHEEpos();
                    cannonball.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    cannonball.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    cannonball.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);
                    cannonballLabel.setText("Cannonball:\nX: " + coordinate.getX() + "\nY: " + coordinate.getY() + "\nZ: " + coordinate.getZ());
                    */

                    //System.out.println(date + "\tCb_pos: " + cannonballObj.getHEEpos() + "\t" +
                    // cannonballObj.getHEEvel());
                    if(RUNFRAMEFORFRAME) pauseStatus = true;
                }
                //Move the camera
                if (goNorth) camera.setTranslateY(camera.getTranslateY() - 10);
                if (goSouth) camera.setTranslateY(camera.getTranslateY() + 10);
                if (goEast)  camera.setTranslateX(camera.getTranslateX() + 10);
                if (goWest)  camera.setTranslateX(camera.getTranslateX() - 10);
            }
        }.start();


        primaryStage.show();
    }
    public static void main(String args[]){
        launch(args);
    }

    private Sphere createGUIobject(CelestialObject object){
        Sphere guiOb = new Sphere(object.getRadius() / MathUtil.AU * plntRadFact);
        PhongMaterial guiMat = new PhongMaterial();
        guiMat.setDiffuseColor(Color.PURPLE);
        guiOb.setMaterial(guiMat);
        return guiOb;
    }

    private void updateGUIobject(Sphere guiObject, CelestialObject cObject){
        Vector3D coordinate = cObject.getHEEpos();
        guiObject.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
        guiObject.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
        guiObject.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);
        guiObject.setRotate(guiObject.getRotate() + 20);
    }
}
