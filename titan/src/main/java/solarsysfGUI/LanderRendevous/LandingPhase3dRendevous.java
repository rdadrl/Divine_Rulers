package solarsysfGUI.LanderRendevous;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.*;
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
import utils.Date;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class LandingPhase3dRendevous extends Application {
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
    // rocket vars
    private SpaceCraft SpaceCraftObj;
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


    //Something is VEEERY wrong with titan, or rocket. Not sure. for the sake of simplicity, I'll switch back to 2D for this phase.
    @Override
    public void start(Stage landingStage) throws Exception {
        //add any fx componenents to this root group.
        root = new Group();
        globalRoot = new StackPane();
        globalRoot.setStyle("-fx-background-image: url('textures/galaxy_starfield.png');");

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

        //Titan & SpaceCraft:
        Box rocket = new Box(2000, 100000, 20);
        rocket.setMaterial(new PhongMaterial(Color.BLUEVIOLET));
        Rotate rotate = new Rotate();
        rotate.setAxis(new Point3D(0,0,90));
        rocket.getTransforms().add(rotate);

        //rocket.setTranslateX(SpaceCraftObj.getCentralPos().getX());
        rocket.setTranslateX(20);
        rocket.setTranslateY(-40);

        Cylinder landingPad = new Cylinder(60, 1);
        landingPad.setTranslateY(0);
        landingPad.setMaterial(new PhongMaterial(Color.DARKRED));

        Sphere landingDot = new Sphere(2);
        landingPad.setMaterial(new PhongMaterial(Color.DARKBLUE));


        Sphere titan = new Sphere(3000);
        //titan.setTranslateY(3000);
        PhongMaterial titanMaterial = new PhongMaterial();
        titanMaterial.setDiffuseMap(new Image("textures/moonmap.jpg"));
        titanMaterial.setBumpMap(new Image("textures/moonbump.jpg"));
        titan.setMaterial(titanMaterial);

        root.getChildren().addAll(titan, rocket, light, landingDot);
        //Camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setFieldOfView(35);
        camera.setTranslateZ(-500);
        camera.setTranslateX(0);
        camera.setNearClip(1);
        camera.setFarClip(171000);
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

        globalRoot.getChildren().addAll(subScene);

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
                while(!pauseStatus) {
                    if (SpaceCraftObj.phaseFinished()) { //if landed
                        System.out.println("Thanks for flying with Paredis Spacelines.");
                        pauseStatus = true;
                        oneMoreRun = true;
                    }
                    else if (System.nanoTime() - lastUpdate >= verletUpdateUnitInMs * 1000000) {
                        vVref.updateLocation(10, TimeUnit.HOURS);
                        lastUpdate = System.nanoTime();
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
                if (key == KeyCode.SPACE) goUp = true;
                if (key == KeyCode.SHIFT) goDown = true;

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
                if (key == KeyCode.SPACE) goUp = false;
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
                if (key == KeyCode.MINUS){

                    rocket.setHeight(rocket.getHeight() * 0.5);
                    rocket.setWidth(rocket.getWidth() * 0.5);

                }
                if (key == KeyCode.EQUALS){
                    rocket.setHeight(rocket.getHeight() * 1.25);
                    rocket.setWidth(rocket.getWidth() * 1.25);

                }
                if (key == KeyCode.EQUALS) {
                    rocket.setWidth(rocket.getHeight());
                }
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
            long lastFrame = System.nanoTime(); //... still, it doesn't have to be very precise.
            public void handle(long currentN)
            {
                long differancePerAnimationFrameInMS = (currentN - lastFrame) / 1000000L;
                if ((oneMoreRun) || (!pauseStatus && differancePerAnimationFrameInMS >= 1000 / MAX_ANIMATION_FPS)) {
                    rocket.setTranslateX(SpaceCraftObj.getCentralPos().getX()/1000000D);
                    rocket.setTranslateY(-SpaceCraftObj.getCentralPos().getY()/1000000D);
                    rocket.setTranslateY(rocket.getTranslateY() - rocket.getHeight());
                    rocket.setTranslateZ(0);
                    rotate.setAngle(-Math.toDegrees(SpaceCraftObj.getCentralPos().getZ()));
//                    if (counter % 10 == 0 || oneMoreRun) System.out.println("LD " +
//                            "X: " + landingDot.getTranslateX() +
//                            ", Y: " + landingDot.getTranslateY() +
//                            ", Z: " + landingDot.getTranslateZ()
//                    );
//                    if (counter % 10 == 0 || oneMoreRun) System.out.println("RK " +
//                            "X: " + rocket.getTranslateX() +
//                            ", Y: " + rocket.getTranslateY() +
//                            ", Z: " + rocket.getTranslateZ()
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






            }
        }.start();

        mainScene.setCursor(Cursor.CROSSHAIR);
        landingStage.setTitle("SolarSysF - Titan Landing");
        landingStage.setScene(mainScene);
        landingStage.setResizable(false);
        landingStage.show();
    }

    public LandingPhase3dRendevous(SpaceCraft SpaceCraftObj, Date date) throws Exception {
        this.date = date;
        this.SpaceCraftObj = SpaceCraftObj;
        startTime = date.getTimeInMillis();
        obj = new ArrayList<>();
        obj.add(SpaceCraftObj);
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
        return  "fps\t\t: " + currentFPS + "\n" +
                "sec\t\t: " + df.format((date.getTimeInMillis()-startTime)/1000D) + "\n" +
                "y-pos\t: " + df.format(SpaceCraftObj.getCentralPos().getY()) + "\n" +
                "y-vel\t: " + df.format(SpaceCraftObj.getCentralVel().getY()) + "\n" +
                "x-pos\t: " + df.format(SpaceCraftObj.getCentralPos().getX()) + "\n" +
                "x-vel\t: " + df.format(SpaceCraftObj.getCentralVel().getX()) + "\n" +
                "t-pos\t: " + df.format(SpaceCraftObj.getCentralPos().getZ()) + "\n" +
                "t-vel\t\t: " + df.format(SpaceCraftObj.getCentralVel().getZ()) + "\n" +
                "speed\t: " + verletUpdateUnitMultiplier + "x";
    }
}
