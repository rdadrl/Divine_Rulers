package solarsysf;

import jGntx.Evolutionary;
import jGntx.Individual;
import jGntx.Population;
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
import solarsystem.Projectile;
import utils.MathUtil;
import solarsystem.CannonBall;
import solarsystem.CelestialObject;
import solarsystem.SolarSystem;
import utils.Date;
import utils.Vector3D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 * This is a version of the simulator for which we hit titan. For debugging purpuses I added some
 * functionality. For example you can change the planet size by pressing + and -. You can
 * change planetary perspectives etc. Arda you can check whether you want to integrate any of
 * these functionalities.
 */
public class MainMenuTitanHit extends Application {
    // runing variables
    private final boolean RUNFRAMEFORFRAME = false;
    private boolean pauseStatus = false;
    private final boolean CANNON_BALL = true;
    private final boolean INSTANT_LAUNCH_LANDING_PHASE = false;
    // Timing variables
    private Date date = new Date(2002, 9, 18, 12, 0, 0);
    private final long dt = 6;
    private final TimeUnit timeUnit = TimeUnit.HOURS;

    // Genetic Alghoritm Variables
    private final double MAX_MUTATION_MULTIPLIER = 1000;

    // Constants for the cannonball
    private final int CANNONBALL_AMOUNT = 11; //11 best performing crossovers to generate 10 children that will replace the remaining worse performing 10.
    private final double CANNONBALL_MIN = 70;//95.2441141057166;
    private final double CANNONBALL_MAX = 120;//95.2441141057167;
    private final double INCLINATION_MIN = 20;//38.744689463330;
    private final double INCLINATION_MAX = 50;//38.744689463339;

    // gui variables
    private Scene mainScene;
    private Group root;
    private boolean goNorth, goSouth, goEast, goWest;
    private final boolean USELIGHTS = true;


    private HashMap<Sphere, Projectile> projectileList = new HashMap<>();
    private SolarSystem solarSystem;
    private CelestialObject followObject;

    //private int DistanceMultiplier = 1;
    //private double plntRadFact = 1000.0/MathUtil.AU;
    private int DistanceMultiplier = 40;
    private double plntRadFact = (2.0/6371.0);
    private double AU = MathUtil.AU;

    @Override
    public void start(Stage primaryStage) {
        try {
            solarSystem = new SolarSystem();
        } catch (IOException e) {
            e.printStackTrace();
        }

        CelestialObject earthObj = solarSystem.getPlanets().getEarth();
        CelestialObject sunObj = solarSystem.getPlanets().getSun();
        CelestialObject mercuryObj = solarSystem.getPlanets().getMercury();
        CelestialObject marsObj = solarSystem.getPlanets().getMars();
        CelestialObject jupiterObj = solarSystem.getPlanets().getJupiter();
        CelestialObject saturnObj = solarSystem.getPlanets().getSaturn();
        CelestialObject titanObj = solarSystem.getPlanets().getTitan();
        CelestialObject urAnusObj = solarSystem.getPlanets().getUranus();
        CelestialObject neptuneObj = solarSystem.getPlanets().getNeptune();
        CelestialObject venusObj = solarSystem.getPlanets().getVenus();

        followObject = sunObj;

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

        //Gntx button
        Hyperlink gntxButton = new Hyperlink("❚GntX Cycle❚");//("❚❚");
        gntxButton.setTextFill(Color.WHITE);
        gntxButton.setBorder(null);

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

        Rotate rotate = new Rotate(90, 0, 0, 0, Rotate.X_AXIS);
        // Sun
        Sphere sun  = new Sphere((sunObj.getRadius() * plntRadFact) / 100); // sun is 100
        //Sphere sun  = new Sphere(); // sun is 100
        // times smaller
        PhongMaterial sunMaterial = new PhongMaterial();
        sunMaterial.setDiffuseMap(new Image("textures/sunmap.jpg"));
        sun.setMaterial(sunMaterial);
        sun.getTransforms().add(rotate);
        sun.setOnMouseEntered(new EventHandler<MouseEvent> () {
            @Override
            public void handle(MouseEvent t) {
                Vector3D coordinate = sunObj.getCentralPos();
                //limit decimals to 3 points
                float sX = ((int) (coordinate.getX() * 1000)) / 1000F;
                float sY = ((int) (coordinate.getY() * 1000)) / 1000F;
                float sZ = ((int) (coordinate.getZ() * 1000)) / 1000F;

                identifierLabel.setText("Sun:\nX: " + sX + "\nY: " + sY + "\nZ: " + sZ);
            }
        });
        sun.setOnMouseExited(e -> identifierLabel.setText(""));

        //Earth
        Sphere earth  = new Sphere(earthObj.getRadius() * plntRadFact);
        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseMap(new Image("textures/earthmap.jpg"));
        earthMaterial.setBumpMap(new Image("textures/earthbump.jpg"));
        //earthMaterial.setSpecularMap(new Image("textures/earthspecular.jpg"));
        earth.setMaterial(earthMaterial);
        earth.getTransforms().add(rotate);
        earth.setOnMouseEntered(new EventHandler<MouseEvent> () {
            @Override
            public void handle(MouseEvent t) {
                Vector3D coordinate = earthObj.getCentralPos();
                //limit decimals to 3 points
                float sX = ((int) (coordinate.getX() * 1000)) / 1000F;
                float sY = ((int) (coordinate.getY() * 1000)) / 1000F;
                float sZ = ((int) (coordinate.getZ() * 1000)) / 1000F;

                identifierLabel.setText("Earth:\nX: " + sX + "\nY: " + sY + "\nZ: " + sZ);
            }
        });
        earth.setOnMouseExited(e -> identifierLabel.setText(""));

        projectileList = new HashMap<>();


        //Mercury
        Sphere mercury = new Sphere(mercuryObj.getRadius() * plntRadFact);
        PhongMaterial mercuryMaterial = new PhongMaterial();
        mercuryMaterial.setDiffuseMap(new Image("textures/mercurymap.jpg"));
        mercuryMaterial.setBumpMap(new Image("textures/mercurybump.jpg"));
        mercury.setMaterial(mercuryMaterial);
        mercury.getTransforms().add(rotate);

        //Mars
        Sphere mars = new Sphere(marsObj.getRadius() * plntRadFact);
        PhongMaterial marsMaterial = new PhongMaterial();
        marsMaterial.setDiffuseMap(new Image("textures/marsmap.jpg"));
        marsMaterial.setBumpMap(new Image("textures/marsbump.jpg"));
        mars.setMaterial(marsMaterial);
        mars.getTransforms().add(rotate);

        //Jupiter
        Sphere jupiter = new Sphere(jupiterObj.getRadius() * plntRadFact);
        PhongMaterial jupiterMaterial = new PhongMaterial();
        jupiterMaterial.setDiffuseMap(new Image("textures/jupitermap.jpg"));
        jupiter.setMaterial(jupiterMaterial);
        jupiter.getTransforms().add(rotate);

        //Saturn
        Sphere saturn = new Sphere(saturnObj.getRadius() * plntRadFact);
        PhongMaterial saturnMaterial = new PhongMaterial();
        saturnMaterial.setDiffuseMap(new Image("textures/saturnmap.jpg"));
        saturn.setMaterial(saturnMaterial);
        saturn.rotateProperty();
        saturn.getTransforms().addAll(rotate);

        //Titan
        Sphere titan = new Sphere(titanObj.getRadius() * plntRadFact);
        PhongMaterial titanMaterial = new PhongMaterial();
        titanMaterial.setDiffuseMap(new Image("textures/moonmap.jpg"));
        titanMaterial.setBumpMap(new Image("textures/moonbump.jpg"));
        titan.setMaterial(titanMaterial);
        titan.getTransforms().add(rotate);

        //Uranus
        Sphere uranus = new Sphere(urAnusObj.getRadius() * plntRadFact);
        PhongMaterial uranusMaterial = new PhongMaterial();
        uranusMaterial.setDiffuseMap(new Image("textures/uranusmap.jpg"));
        uranus.setMaterial(uranusMaterial);
        uranus.getTransforms().add(rotate);

        //Neptune
        Sphere neptune = new Sphere(neptuneObj.getRadius() * plntRadFact);
        PhongMaterial neptuneMaterial = new PhongMaterial();
        neptuneMaterial.setDiffuseMap(new Image("textures/neptunemap.jpg"));
        neptune.setMaterial(neptuneMaterial);
        neptune.getTransforms().add(rotate);

        //Venus
        Sphere venus = new Sphere(venusObj.getRadius() * plntRadFact);
        PhongMaterial venusMaterial = new PhongMaterial();
        venusMaterial.setDiffuseMap(new Image("textures/venusmap.jpg"));
        venusMaterial.setBumpMap(new Image("textures/venusbump.jpg"));
        venus.setMaterial(venusMaterial);
        venus.getTransforms().add(rotate);

        Evolutionary betterBalls;
        //Cannonball
        if(CANNON_BALL) {
            betterBalls = createCannonBalls();
            gntxButton.setOnAction(e -> betterBalls.triggerCycleOver() );
        }

        //Date Label
        Label dateLabel = new Label(date.toDateString());
        dateLabel.setTextFill(Color.WHITE);

        //Camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-100);
        camera.setNearClip(0.0001);
        camera.setFarClip(2000);
        camera.setFieldOfView(35);

        //root has the celestialobjects and the lights.
        root = new Group();
        if (USELIGHTS) root.getChildren().addAll(pointLight, ambientLight);
        root.getChildren().addAll(sun, earth, mercury, mars, jupiter, saturn, titan, uranus, neptune, venus);
        root.getChildren().addAll(projectileList.keySet());


        AnchorPane globalRoot = new AnchorPane();
        globalRoot.getChildren().addAll(dateLabel, identifierLabel, pausePlayButton, gntxButton, cannonballLabel);
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
        globalRoot.setStyle("-fx-background-image: url('textures/galaxy_starfield.png');");
        primaryStage.setTitle("SolarSysF - Solar Visualization From Scratch");
        primaryStage.setScene(mainScene);

        mainScene.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override public void handle(ScrollEvent event) {
                event.consume();
                if (event.getDeltaY() == 0) return;
                else if (event.getDeltaY() > 0) camera.setTranslateZ((camera.getTranslateZ() * 0.9));
                else camera.setTranslateZ((camera.getTranslateZ() *1.1));
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

                    // change planet radius
                    case EQUALS: plntRadFact = plntRadFact * 1.1; break;
                    case MINUS: plntRadFact = plntRadFact * 0.9; break;
                    case DIGIT0: camera.setTranslateX(0); camera.setTranslateY(0); break;

                    case S: followObject = sunObj; camera.setTranslateX(0); camera.setTranslateY(0); break;
                    case E: followObject = earthObj; camera.setTranslateX(0); camera.setTranslateY(0);break;
                    case R: followObject = saturnObj; camera.setTranslateX(0); camera.setTranslateY(0);break;
                    case T: followObject = titanObj; camera.setTranslateX(0); camera.setTranslateY(0);break;

                    case DIGIT1: DistanceMultiplier = 40; plntRadFact = (1.0/6371.0); camera.setTranslateZ(-100); break;
                    case DIGIT2: DistanceMultiplier = 1; plntRadFact = (1000.0/MathUtil.AU); camera.setTranslateZ(-0.02); break;

                }
            }
        });

        ArrayList<CelestialObject> allObj =
                new ArrayList<>(projectileList.values());
        allObj.addAll(solarSystem.getPlanets().getAll());
        solarSystem.setAllAnimatedObjects(allObj);

        ArrayList<Projectile> projectileInstances = new ArrayList<>(projectileList.values());

        solarSystem.initializeAnimation(date, projectileInstances);

        final long startNanoTime = System.nanoTime();
        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                if (!pauseStatus) {
                    betterBalls.getPopulation().updateFitnessValues();
                    dateLabel.setText(date.toDateString());
                    Vector3D coordinate;

                    coordinate = sunObj.getCentralPos();
                    coordinate = coordinate.substract(followObject.getCentralPos());
                    sun.setRadius(sunObj.getRadius() * plntRadFact/50.0);
                    sun.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    sun.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    sun.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);
                    sun.setRotate(sun.getRotate() + 2);

                    coordinate = earthObj.getCentralPos();
                    coordinate = coordinate.substract(followObject.getCentralPos());
                    earth.setRadius(earthObj.getRadius() * plntRadFact);
                    earth.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    earth.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    earth.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);
                    earth.setRotate(earth.getRotate() + 20);

                    coordinate = mercuryObj.getCentralPos();
                    coordinate = coordinate.substract(followObject.getCentralPos());
                    mercury.setRadius(mercuryObj.getRadius() * plntRadFact);
                    mercury.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    mercury.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    mercury.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);
                    mercury.setRotate(mercury.getRotate() + 20);

                    coordinate = marsObj.getCentralPos();
                    coordinate = coordinate.substract(followObject.getCentralPos());
                    mars.setRadius(marsObj.getRadius() * plntRadFact);
                    mars.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    mars.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    mars.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);
                    mars.setRotate(mars.getRotate() + 20);

                    coordinate = jupiterObj.getCentralPos();
                    coordinate = coordinate.substract(followObject.getCentralPos());
                    jupiter.setRadius(jupiterObj.getRadius() * plntRadFact);
                    jupiter.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    jupiter.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    jupiter.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);
                    jupiter.setRotate(jupiter.getRotate() + 2);

                    coordinate = saturnObj.getCentralPos();
                    coordinate = coordinate.substract(followObject.getCentralPos());
                    saturn.setRadius(saturnObj.getRadius() * plntRadFact);
                    saturn.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    saturn.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    saturn.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);
                    saturn.setRotate(saturn.getRotate() + 2);

                    //Titan Update
                    coordinate = titanObj.getCentralPos();
                    coordinate = coordinate.substract(followObject.getCentralPos());
                    titan.setRadius(titanObj.getRadius() * plntRadFact);
                    titan.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    titan.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    titan.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);
                    titan.setRotate(titan.getRotate() + 20);

                    coordinate = urAnusObj.getCentralPos();
                    coordinate = coordinate.substract(followObject.getCentralPos());
                    uranus.setRadius(urAnusObj.getRadius() * plntRadFact);
                    uranus.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    uranus.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    uranus.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);
                    uranus.setRotate(uranus.getRotate() + 20);

                    coordinate = neptuneObj.getCentralPos();
                    coordinate = coordinate.substract(followObject.getCentralPos());
                    neptune.setRadius(neptuneObj.getRadius() * plntRadFact);
                    neptune.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    neptune.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    neptune.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);
                    neptune.setRotate(neptune.getRotate() + 20);

                    coordinate = venusObj.getCentralPos();
                    coordinate = coordinate.substract(followObject.getCentralPos());
                    venus.setRadius(venusObj.getRadius() * plntRadFact);
                    venus.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    venus.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    venus.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);
                    venus.setRotate(venus.getRotate() + 20);


                    for(Sphere guiObject: projectileList.keySet()){
                        updateGUIobject(guiObject, projectileList.get(guiObject));
                        if (projectileList.get(guiObject).getClosestDistanceThisProjectile() <= 1.0171236527952513E8 * 1.1 || INSTANT_LAUNCH_LANDING_PHASE) { //i love to play with this variable, and you will too.
                            System.out.println("Initiate the landing phase capt'!");
                            this.stop();
                            try {
                                Application landingPhaseApp = new LandingPhase(projectileList.get(guiObject));
                                landingPhaseApp.start(new Stage());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if(RUNFRAMEFORFRAME) pauseStatus = true;
                    solarSystem.updateAnimation(dt, timeUnit);
                }
                //Move the camera
                if (goNorth) camera.setTranslateY(camera.getTranslateY() - 1);
                if (goSouth) camera.setTranslateY(camera.getTranslateY() + 1);
                if (goEast)  camera.setTranslateX(camera.getTranslateX() + 1);
                if (goWest)  camera.setTranslateX(camera.getTranslateX() - 1);
            }
        }.start();


        primaryStage.show();
    }
    public static void main(String args[]){
        launch(args);
    }

    private Evolutionary createCannonBalls() {
        //create the individual list
        Individual<Projectile>[] indList = (Individual<Projectile>[]) new Individual[CANNONBALL_AMOUNT];
        for (int i = 0; i < CANNONBALL_AMOUNT; i++) {
            Random r = new Random();
            Double velocity = CANNONBALL_MIN + (CANNONBALL_MAX - CANNONBALL_MIN) * r.nextDouble();
            Double inclination = INCLINATION_MIN + (INCLINATION_MAX - INCLINATION_MIN) * r.nextDouble();
            //Double inclination = ThreadLocalRandom.current().nextDouble(0, inclinationRange);
            Projectile cannonballObj = new CannonBall(100, 2000,
                    solarSystem.getPlanets().getEarth(),
                    solarSystem.getPlanets().getTitan(), date,
                    inclination, velocity);
            //titanObj.getCentralPos().substract(earthObj.getCentralPos()).unit().scale
            // (velocity));
            projectileList.put(createGUIobject(cannonballObj), cannonballObj);
            indList[i] = new Individual<Projectile>(cannonballObj) {
                @Override
                public Projectile mutate(Projectile source) {
                    return new CannonBall(100, 2000,
                            solarSystem.getPlanets().getEarth(),
                            solarSystem.getPlanets().getTitan(), date,
                            Math.toDegrees(source.getDepartureInclination()) * r.nextDouble() * MAX_MUTATION_MULTIPLIER, source.getDepartureVelocity() * r.nextDouble() * MAX_MUTATION_MULTIPLIER / 1000);
                }
            };
        }

        Population<Projectile> cannonBalls = new Population(indList) {
            @Override
            public void updateFitnessValue(Individual ind) {
                double newFitness = ((Projectile) ind.getChromosome()).getClosestDistanceThisProjectile();
                if (ind.getFitness() < newFitness) ind.setFitness(newFitness);
            }
        };

        return new Evolutionary(cannonBalls) {
            @Override
            public void onCycleOver() {
                //Now, get those fitness values updated.
                this.getPopulation().updateFitnessValues();

                //Awesome, we did reset the whole system! now let's cleanup old cannonballs and make them betterballs!
                Individual[] pops = this.getPopulation().getPopulation();
                for (Individual ind : pops) {
                    ind.mutate(ind.getChromosome());
                }
                this.crossOver();

                date = new Date(2002, 9, 18, 12, 0, 0);
                solarSystem.initializeAnimation(date, new ArrayList<>(projectileList.values()));
            }

            @Override
            public void crossOver() {
                Population localPop = this.getPopulation();
                Individual<Projectile>[] indList = localPop.getPopulation();

                for (int i = 0; i < 10; i++) {
                    double newDepInc = (Math.toDegrees(indList[i].getChromosome().getDepartureInclination()) + Math.toDegrees(indList[i + 1].getChromosome().getDepartureInclination())) / 2;
                    double newDepVel = (indList[i].getChromosome().getDepartureVelocity() + indList[i + 1].getChromosome().getDepartureVelocity()) / 2000;

                    indList[indList.length - 1 - i].setChromosome(new CannonBall(100, 2000,
                            solarSystem.getPlanets().getEarth(),
                            solarSystem.getPlanets().getTitan(), date,
                            newDepInc, newDepVel));
                }
            }
        };
    }

    private Sphere createGUIobject(CelestialObject object){
        Sphere guiOb = new Sphere(object.getRadius() * plntRadFact);
        PhongMaterial guiMat = new PhongMaterial();
        guiMat.setDiffuseColor(Color.PURPLE);
        guiOb.setMaterial(guiMat);
        return guiOb;
    }

    private void updateGUIobject(Sphere guiObject, CelestialObject cObject){
        Vector3D coordinate = cObject.getCentralPos();
        coordinate = coordinate.substract(followObject.getCentralPos());
        guiObject.setRadius(cObject.getRadius() * plntRadFact);
        guiObject.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
        guiObject.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
        guiObject.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);
        guiObject.setRotate(guiObject.getRotate() + 20);
    }
}
