package solarsysfGUI;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.stage.Stage;
import physics.ODEsolver;
import physics.VerletVelocity;
import solarsystem.rocket.SpaceCraft;
import solarsystem.rocket.lunarLander.Lunarlander;
import utils.Date;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class EarthFocus3D extends Application {

    // yipping variables
    private Scene mainScene;
    private StackPane globalRoot;
    private Group root;
    private int scnW = 800;
    private int scnH = 600;
    private final boolean DEBUG = true;
    private boolean pauseStatus = false; //play/pause animations and yipping updates
    private boolean oneMoreRun = false;
    private Label debugText;
    private Date date;
    private long startTime;
    private ODEsolver ODEsolver;
    private final int MAX_ANIMATION_FPS =30;
    private int currentFPS;
    private double verletUpdateUnitInMs = 10;
    private int verletUpdateUnitMultiplier = 1;
    private int counter;
    // InterPlanetaryRocket vars
    private SpaceCraft spaceCraftObj;
    private ArrayList<SpaceCraft> obj;

    //HID Event flags
    private final int CAMERA_MOVEMENT_STEP_SIZE = 10;
    private final int CAMERA_MAX_SPEED = 100;
    private final int CAMERA_INITAL_SPEED = 50;
    private final int CAMERA_MAX_ANGLE = 60;
    // < Camera rotations
    private Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
    private Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);

    private boolean goNorth;
    private int goNorthDelta = 50;
    private boolean goSouth;
    private int goSouthDelta = 50;
    private boolean goEast;
    private int goEastDelta = 50;
    private boolean goWest;
    private int goWestDelta = 50;
    private boolean goUp;
    private int goUpDelta = 50;
    private boolean goDown;
    private int goDownDelta = 50;

    //Trail vars
    Group trail = new Group();
    PhongMaterial trailMaterial;

    //Something is VEEERY wrong with titan, or InterPlanetaryRocket. Not sure. for the sake of simplicity, I'll switch back to 2D for this phase.
    @Override
    public void start(Stage landingStage) throws Exception {
        //add any fx componenents to this root group.
        root = new Group();
        globalRoot = new StackPane();
        globalRoot.setStyle("-fx-background-image: url('textures/galaxy_starfield.png');");
        trailMaterial = new PhongMaterial();
        trailMaterial.setDiffuseColor(Color.WHITE);
        //Labels:
        if (DEBUG) {
            debugText = new Label(constructDebugText());
            debugText.setTextFill(Color.WHITE);
            debugText.setLayoutX(10);
            debugText.setLayoutY(10);
            globalRoot.getChildren().add(debugText);
            globalRoot.setAlignment(debugText, Pos.TOP_LEFT);
            debugText.toFront();
        }

        AmbientLight light = new AmbientLight();
        light.setColor(Color.LIGHTGOLDENRODYELLOW);

        //Titan & Lunarlander:
        Box rocket = new Box(200, 1000, 200);
        rocket.setMaterial(new PhongMaterial(Color.BLUEVIOLET));
        Rotate rotate = new Rotate();
        rotate.setAxis(new Point3D(0,0,90));
        rocket.getTransforms().add(rotate);

        //InterPlanetaryRocket.setTranslateX(spaceCraftObj.getCentralPos().getX());
        rocket.setTranslateX(20);
        rocket.setTranslateY(-40);

        Cylinder landingPad = new Cylinder(60, 1);
        landingPad.setTranslateY(0);
        landingPad.setMaterial(new PhongMaterial(Color.DARKRED));

        Sphere landingDot = new Sphere(2);
        landingPad.setMaterial(new PhongMaterial(Color.DARKBLUE));


        Sphere earth = new Sphere(6371);
        earth.setTranslateY(6500);
        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseMap(new Image("textures/earthmap.jpg"));
        earthMaterial.setBumpMap(new Image("textures/earthbump.jpg"));
        earthMaterial.setSpecularMap(new Image("textures/earthspecular.jpg"));
        earth.setMaterial(earthMaterial);

        root.getChildren().addAll(earth, rocket, light, landingDot, trail);
        //Camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setFieldOfView(35);
        camera.setTranslateZ(-3000);
        camera.setTranslateX(0);
        camera.setNearClip(1);
        camera.setFarClip(2000000);
        //camera.getTransforms().addAll(xRotate, yRotate);

        SubScene subScene = new SubScene(root,800,600,false, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setLayoutX(0);
        subScene.setLayoutY(0);

        /* Middle Cursor - DEPRECIATED
        Image cursorImg = new Image("mid_cursor.png");
        ImageView cursorView = new ImageView(cursorImg);
        cursorView.setPreserveRatio(true);
        cursorView.setFitWidth(scnH / 10);
        cursorView.toFront();*/

        //Pause/Play button
        Hyperlink pausePlayButton = new Hyperlink("❚❚");
        pausePlayButton.setTextFill(Color.WHITE);
        pausePlayButton.setBorder(null);
        pausePlayButton.setOnAction(e -> {
            pauseStatus = !pauseStatus;
            if (pauseStatus) pausePlayButton.setText("▶");
            else pausePlayButton.setText("❚❚");
        });
        globalRoot.setAlignment(pausePlayButton, Pos.TOP_RIGHT);

        globalRoot.getChildren().addAll(subScene, pausePlayButton);

        //Anchor the elems
        //globalRoot.setAlignment(cursorView, Pos.CENTER);
        globalRoot.setAlignment(subScene, Pos.CENTER);

        //below sets up the stage, if implementing as scene, don't forget to remove.
        mainScene = new Scene(globalRoot, scnW, scnH, true);

        class verletUpdater implements Runnable {

            private ODEsolver vVref;
            private long lastUpdate = System.nanoTime();
            public verletUpdater(ODEsolver ref) {
                this.vVref = ref;
            }

            public void run() {
                while (true) {
                    if (!pauseStatus) {
                        if (spaceCraftObj instanceof Lunarlander) {
                            Lunarlander lunarlander = (Lunarlander) spaceCraftObj;
                            if (lunarlander.getLanded()) { //if landed
                                System.out.println("Thanks for flying with Paredis Spacelines.");
                                pauseStatus = true;
                                oneMoreRun = true;
                            } else if (System.nanoTime() - lastUpdate >= verletUpdateUnitInMs * 1000000) {
                                vVref.updateLocation(10, TimeUnit.MILLISECONDS);
                                lastUpdate = System.nanoTime();
                            }
                        } else {
                            if (spaceCraftObj.phaseFinished()) { //if landed
                                System.out.println("Thanks for flying with Paredis Spacelines.");
                                pauseStatus = true;
                                oneMoreRun = true;
                            } else if (System.nanoTime() - lastUpdate >= verletUpdateUnitInMs * 1000000) {
                                vVref.updateLocation(10, TimeUnit.MILLISECONDS);
                                lastUpdate = System.nanoTime();
                            }

                        }

                    }
                }
            }
        }

        Thread vVt = new Thread(new verletUpdater(ODEsolver));
        vVt.start();


        //HID Events
        mainScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                event.consume();
                KeyCode key = event.getCode();
                if (key == KeyCode.UP) goNorth = true;
                if (key == KeyCode.DOWN) goSouth = true;
                if (key == KeyCode.RIGHT) goEast = true;
                if (key == KeyCode.LEFT) goWest = true;
                if (key == KeyCode.CONTROL) goUp = true;
                if (key == KeyCode.SHIFT) goDown = true;

                if (key == KeyCode.R) {
                    if (trail.getChildren().size() > 0) {
                        Sphere trailStart = (Sphere) (trail.getChildren().get(0));
                        trailStart.setRadius(trailStart.getRadius() * 0.8);
                    }
                }
                if (key == KeyCode.T) {
                    if (trail.getChildren().size() > 0) {
                        Sphere trailStart = (Sphere) (trail.getChildren().get(0));
                        trailStart.setRadius(trailStart.getRadius() * 1.2);
                    }
                }

                if (key == KeyCode.F9) if (verletUpdateUnitInMs <= 10) {
                    verletUpdateUnitInMs = verletUpdateUnitInMs / 2;
                    verletUpdateUnitMultiplier = verletUpdateUnitMultiplier * 2;
                }
                if (key == KeyCode.F7) if (verletUpdateUnitInMs <= 10 && verletUpdateUnitMultiplier / 2 >= 1) {
                    verletUpdateUnitInMs = verletUpdateUnitInMs * 2;
                    verletUpdateUnitMultiplier = verletUpdateUnitMultiplier / 2;
                }
            }
        });

        mainScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                event.consume();
                KeyCode key = event.getCode();
                if (key == KeyCode.UP) goNorth = false;
                if (key == KeyCode.DOWN) goSouth = false;
                if (key == KeyCode.RIGHT) goEast = false;
                if (key == KeyCode.LEFT) goWest = false;
                if (key == KeyCode.CONTROL) goUp = false;
                if (key == KeyCode.SHIFT) goDown = false;
                if (key == KeyCode.F10) {
                    landingStage.setFullScreen(!landingStage.isFullScreen());
                    if (landingStage.isFullScreen()) {
                        landingStage.setHeight(scnH);
                        landingStage.setWidth(scnW);
                    }
                    else {
                        landingStage.setHeight(Screen.getPrimary().getBounds().getHeight());
                        landingStage.setWidth(Screen.getPrimary().getBounds().getWidth());
                    }
                }
                if (key == KeyCode.P) pauseStatus = !pauseStatus;
            }
        });

        mainScene.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override public void handle(ScrollEvent event) {
                event.consume();
                if (event.getDeltaY() == 0) return;
                else if (event.getDeltaY() > 0) camera.setTranslateZ((camera.getTranslateZ() * 0.9));
                else camera.setTranslateZ((camera.getTranslateZ() *1.1));
            }
        });
        mainScene.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                double xDelta = event.getX() - (scnH / 2D);
                double yDelta = (scnH / 2D) - event.getY();
                xRotate.setAngle((yDelta / (scnH / 2D)) * CAMERA_MAX_ANGLE);
                yRotate.setAngle((xDelta / (scnW / 2D)) * CAMERA_MAX_ANGLE);
            }
        });
        //animations and updates to the yipping elements:
        new AnimationTimer()
        {
            int waitFramesForTrail = 0;
            long lastFrame = System.nanoTime(); //... still, it doesn't have to be very precise.
            public void handle(long currentN)
            {
                long differancePerAnimationFrameInMS = (currentN - lastFrame) / 1000000L;
                if ((oneMoreRun) || (!pauseStatus && differancePerAnimationFrameInMS >= 1000 / MAX_ANIMATION_FPS)) {
                    rocket.setTranslateX(spaceCraftObj.getCentralPos().getX());
                    if(spaceCraftObj instanceof Lunarlander) rocket.setTranslateY(-spaceCraftObj.getCentralPos().getY()/100);
                    else rocket.setTranslateY(-spaceCraftObj.getCentralPos().getY()/100);
                    rocket.setTranslateY(rocket.getTranslateY() - rocket.getHeight() / 2D);
                    rocket.setTranslateZ(0);
                    rotate.setAngle(-Math.toDegrees(spaceCraftObj.getCentralPos().getZ()));
//                    if (counter % 10 == 0 || oneMoreRun) System.out.println("LD " +
//                            "X: " + landingDot.getTranslateX() +
//                            ", Y: " + landingDot.getTranslateY() +
//                            ", Z: " + landingDot.getTranslateZ()
//                    );
//                    if (counter % 10 == 0 || oneMoreRun) System.out.println("RK " +
//                            "X: " + InterPlanetaryRocket.getTranslateX() +
//                            ", Y: " + InterPlanetaryRocket.getTranslateY() +
//                            ", Z: " + InterPlanetaryRocket.getTranslateZ()
//                    );
                    if (DEBUG) debugText.setText(constructDebugText());
                    currentFPS = (int) (1000 / differancePerAnimationFrameInMS);
                    lastFrame= currentN;
                    if(oneMoreRun) oneMoreRun = false;
                    counter ++;
                }

                //Handle key events
                if (goNorth) {
                    camera.setTranslateZ(camera.getTranslateZ() + goNorthDelta);
                    if (goNorthDelta < CAMERA_MAX_SPEED) goNorthDelta += CAMERA_MOVEMENT_STEP_SIZE;
                }
                else if (goNorthDelta > CAMERA_INITAL_SPEED) goNorthDelta = CAMERA_INITAL_SPEED;

                if (goSouth) {
                    camera.setTranslateZ(camera.getTranslateZ() - goSouthDelta);
                    if (goSouthDelta < CAMERA_MAX_SPEED) goSouthDelta += CAMERA_MOVEMENT_STEP_SIZE;
                }
                else if (goSouthDelta > CAMERA_INITAL_SPEED) goSouthDelta = CAMERA_INITAL_SPEED;

                if (goEast) {
                    camera.setTranslateX(camera.getTranslateX() + goEastDelta);
                    if (goEastDelta < CAMERA_MAX_SPEED) goEastDelta += CAMERA_MOVEMENT_STEP_SIZE;
                }
                else if (goEastDelta > CAMERA_INITAL_SPEED) goEastDelta = CAMERA_INITAL_SPEED;

                if (goWest) {
                    camera.setTranslateX(camera.getTranslateX() - goWestDelta);
                    if (goWestDelta < CAMERA_MAX_SPEED) goWestDelta += CAMERA_MOVEMENT_STEP_SIZE;
                }
                else if (goWestDelta > CAMERA_INITAL_SPEED) goWestDelta = CAMERA_INITAL_SPEED;

                if (goUp) {
                    camera.setTranslateY(camera.getTranslateY() + goUpDelta);
                    if (goUpDelta < CAMERA_MAX_SPEED) goUpDelta += CAMERA_MOVEMENT_STEP_SIZE;
                }
                else if (goUpDelta > CAMERA_INITAL_SPEED) goUpDelta = CAMERA_INITAL_SPEED;

                if (goDown) {
                    camera.setTranslateY(camera.getTranslateY() - goDownDelta);
                    if (goDownDelta < CAMERA_MAX_SPEED) goDownDelta += CAMERA_MOVEMENT_STEP_SIZE;
                }
                else if (goDownDelta > CAMERA_INITAL_SPEED) goDownDelta = CAMERA_INITAL_SPEED;

                //Trail
                if (waitFramesForTrail >= 30 / verletUpdateUnitMultiplier) { //if around a second passed
                    waitFramesForTrail = 0;

                    Sphere trailDot = new Sphere(5);
                    trailDot.setMaterial(trailMaterial);
                    trailDot.setTranslateY(rocket.getTranslateY());
                    trailDot.setTranslateX(rocket.getTranslateX());
                    trailDot.setTranslateZ(rocket.getTranslateZ());
                    if (trail.getChildren().size() > 0) trailDot.radiusProperty().bind(((Sphere) trail.getChildren().get(0)).radiusProperty());
                    trail.getChildren().add(trailDot);
                }
                else waitFramesForTrail++;

            }
        }.start();

        mainScene.setCursor(Cursor.CROSSHAIR);
        landingStage.setTitle("SolarSysF - Titan Landing");
        landingStage.setScene(mainScene);
        landingStage.setResizable(false);
        landingStage.show();
    }

    public EarthFocus3D(SpaceCraft spaceCraftObj, Date date) throws Exception {
        this.date = date;
        this.spaceCraftObj = spaceCraftObj;
        startTime = date.getTimeInMillis();
        obj = new ArrayList<>();
        obj.add(spaceCraftObj);
        this.ODEsolver = new VerletVelocity(obj, date);

        //push to start
    }

    public void setODEsolver(physics.ODEsolver ODEsolver) {
        this.ODEsolver = ODEsolver;
        ODEsolver.initialize(obj, date);
    }

    public String constructDebugText () {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        if(spaceCraftObj instanceof Lunarlander) {
            Lunarlander lunarlander = (Lunarlander) spaceCraftObj;
            return  "fps\t\t: " + currentFPS + "\n" +
                    "sec\t\t: " + df.format((date.getTimeInMillis()-startTime)/1000D) + "\n" +
                    "y-pos\t: " + df.format(spaceCraftObj.getCentralPos().getY()) + "\n" +
                    "y-vel\t: " + df.format(spaceCraftObj.getCentralVel().getY()) + "\n" +
                    "x-pos\t: " + df.format(spaceCraftObj.getCentralPos().getX()) + "\n" +
                    "x-vel\t: " + df.format(spaceCraftObj.getCentralVel().getX()) + "\n" +
                    "t-pos\t: " + df.format(spaceCraftObj.getCentralPos().getZ()) + "\n" +
                    "t-vel\t\t: " + df.format(spaceCraftObj.getCentralVel().getZ()) + "\n" +
                    "ft%\t\t: " + df.format(lunarlander.getMainThrusterForceAsPercentage()) + "\n" +
                    "fuel\t\t: " + df.format(lunarlander.getFuelMass()) + "\n" +
                    "c-wind\t: " + df.format(lunarlander.getCurrentWindSpeed()) + "\n" +
                    "m-wind\t: " + df.format(lunarlander.getMeanWindSpeed()) + "\n" +
                    "speed\t: " + verletUpdateUnitMultiplier + "x";

        }

        return  "fps\t\t: " + currentFPS + "\n" +
                "sec\t\t: " + df.format((date.getTimeInMillis()-startTime)/1000D) + "\n" +
                "y-pos\t: " + df.format(spaceCraftObj.getCentralPos().getY()) + "\n" +
                "y-vel\t: " + df.format(spaceCraftObj.getCentralVel().getY()) + "\n" +
                "x-pos\t: " + df.format(spaceCraftObj.getCentralPos().getX()) + "\n" +
                "x-vel\t: " + df.format(spaceCraftObj.getCentralVel().getX()) + "\n" +
                "t-pos\t: " + df.format(spaceCraftObj.getCentralPos().getZ()) + "\n" +
                "t-vel\t\t: " + df.format(spaceCraftObj.getCentralVel().getZ()) + "\n";
    }
}
