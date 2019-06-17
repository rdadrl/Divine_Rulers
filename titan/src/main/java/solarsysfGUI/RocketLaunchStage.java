package solarsysfGUI;

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
import javafx.stage.Stage;
import solarsystem.*;
import solarsystem.SolarSystem;
import solarsystem.rocket.Projectile;
import utils.Date;
import utils.MathUtil;
import utils.vector.Vector3D;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * This is a clean rocket launching stage, to be introduced functionalities
 */
public class RocketLaunchStage extends Application {
    // running variables
    private boolean pauseStatus = false;

    // Timing variables
    private Date date = new Date(2002, 9, 18, 12, 0, 0);
    private final long dt = 6;
    private final TimeUnit timeUnit = TimeUnit.HOURS;
    private final int UPDATE_FREQUENCY_IN_MS = 10;
    // GUI variables
    private Scene mainScene;
    private Group root;
    private boolean goNorth, goSouth, goEast, goWest;
    private final boolean USELIGHTS = true;
    private Label identifierLabel;


    private HashMap<Sphere, Projectile> projectileList = new HashMap<>();
    private SolarSystem solarSystem;
    private CelestialObject followObject;

    private int DistanceMultiplier = 40;
    private double plntRadFact = (2.0/6371.0);

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
        CelestialObject uranusObj = solarSystem.getPlanets().getUranus();
        CelestialObject neptuneObj = solarSystem.getPlanets().getNeptune();
        CelestialObject venusObj = solarSystem.getPlanets().getVenus();
        followObject = sunObj; //set followup object as sun (the middle planet)

        //Create the planet spheres
        Sphere sun = createGUIobject(sunObj, new Image("textures/sunmap.jpg"), null);
        Sphere earth = createGUIobject(earthObj, new Image("textures/earthmap.jpg"), new Image("textures/earthbump.jpg"));
        Sphere mercury = createGUIobject(mercuryObj, new Image("textures/mercurymap.jpg"), new Image("textures/mercurybump.jpg"));
        Sphere mars = createGUIobject(marsObj, new Image("textures/marsmap.jpg"), new Image("textures/marsbump.jpg"));
        Sphere jupiter = createGUIobject(jupiterObj, new Image("textures/jupitermap.jpg"), null);
        Sphere saturn = createGUIobject(saturnObj, new Image("textures/saturnmap.jpg"), null);
        Sphere titan = createGUIobject(titanObj, new Image("textures/moonmap.jpg"), new Image("textures/moonbump.jpg"));
        Sphere uranus = createGUIobject(uranusObj, new Image("textures/uranusmap.jpg"), null);
        Sphere neptune = createGUIobject(neptuneObj, new Image("textures/neptunemap.jpg"), null);
        Sphere venus = createGUIobject(venusObj, new Image("textures/venusmap.jpg"), new Image("textures/venusbump.jpg"));
        ((PhongMaterial) titan.getMaterial()).setDiffuseColor(Color.LIGHTGOLDENRODYELLOW); //add titan a yellow tint

        //Planet Name Label
        identifierLabel = new Label();
        identifierLabel.setTextFill(Color.WHITE);

        //Date Label
        Label dateLabel = new Label(date.toDateString());
        dateLabel.setTextFill(Color.WHITE);

        //Pause/Play button
        Hyperlink pausePlayButton = new Hyperlink("❚❚");
        pausePlayButton.setTextFill(Color.WHITE);
        pausePlayButton.setBorder(null);
        pausePlayButton.setOnAction(e -> {
            pauseStatus = !pauseStatus;
            if (pauseStatus) pausePlayButton.setText("▶");
            else pausePlayButton.setText("❚❚");
        });

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

        projectileList = new HashMap<>();

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
        globalRoot.getChildren().addAll(dateLabel, identifierLabel, pausePlayButton);
        //Anchor the dateLabel
        globalRoot.setBottomAnchor(dateLabel, 10D);
        globalRoot.setRightAnchor(dateLabel, 10D);
        globalRoot.setLeftAnchor(identifierLabel, 10D);
        globalRoot.setTopAnchor(identifierLabel, 10D);

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
        class ODEupdater implements Runnable {
            private long lastUpdate = System.nanoTime();

            public void run() {
                while(!pauseStatus) {
                    if (System.nanoTime() - lastUpdate >= UPDATE_FREQUENCY_IN_MS * 1000000) {
                        solarSystem.updateAnimation(dt, timeUnit);
                        lastUpdate = System.nanoTime();
                    }
                }
            }
        }
        Thread ODEu = new Thread(new ODEupdater());
        ODEu.start();

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                if (!pauseStatus) {
                    dateLabel.setText(date.toDateString());
                    Vector3D coordinate;

                    //have to update the sun manually because it's enormous
                    coordinate = sunObj.getCentralPos().substract(followObject.getCentralPos());
                    sun.setRadius(sunObj.getRadius() * plntRadFact/50.0);
                    sun.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                    sun.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                    sun.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);
                    sun.setRotate(sun.getRotate() + 2);

                    //updateGUIobject(sun, sunObj);
                    updateGUIobject(earth, earthObj);
                    updateGUIobject(mercury, mercuryObj);
                    updateGUIobject(mars, marsObj);
                    updateGUIobject(jupiter, jupiterObj);
                    updateGUIobject(saturn, saturnObj);
                    updateGUIobject(titan, titanObj);
                    updateGUIobject(uranus, uranusObj);
                    updateGUIobject(neptune, neptuneObj);
                    updateGUIobject(venus, venusObj);

                    //Update projectile positions & check whether it's close enough to titan to activate landing phase
                    for(Sphere guiObject: projectileList.keySet()){
                        updateGUIobject(guiObject, projectileList.get(guiObject));
                        if (projectileList.get(guiObject).getClosestDistanceThisProjectile() <= 1.0171236527952513E8 * 1.1) { //i love to play with this variable, and you will too.
                            System.out.println("Initiate the landing phase capt'!");
                            this.stop();
                            try {
                                //Application landingPhaseApp =
                                        //new LandingPhase(projectileList.get(guiObject), date);
                                //landingPhaseApp.start(new Stage());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if(pauseStatus) pauseStatus = true;
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


    private Sphere createGUIobject(CelestialObject object, Image diffuseMap, Image bumpMap){
        Sphere guiOb = new Sphere(object.getRadius() * plntRadFact);
        PhongMaterial guiMat = new PhongMaterial();
        //if no texture, apply purple as diffmap
        if (diffuseMap == null && bumpMap == null) guiMat.setDiffuseColor(Color.PURPLE);
        guiMat.setDiffuseMap(diffuseMap);
        guiMat.setBumpMap(bumpMap);
        guiOb.setMaterial(guiMat);

        guiOb.setOnMouseEntered(new EventHandler<MouseEvent> () {
            @Override
            public void handle(MouseEvent t) {
                Vector3D coordinate = object.getCentralPos();
                //limit decimals to 3 points
                float sX = ((int) (coordinate.getX() * 1000)) / 1000F;
                float sY = ((int) (coordinate.getY() * 1000)) / 1000F;
                float sZ = ((int) (coordinate.getZ() * 1000)) / 1000F;

                identifierLabel.setText(object.getName() + ":\nX: " + sX + "\nY: " + sY + "\nZ: " + sZ);
            }
        });
        guiOb.setOnMouseExited(e -> identifierLabel.setText(""));

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
