package solarsysfGUI;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import physics.ODEsolver;
import physics.VerletVelocity;
import solarsystem.rocket.lunarLander.Lunarlander;
import utils.Date;

import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class LandingPhase extends Application {
    // yipping variables
    private Scene mainScene;
    private Pane root;
    private int scnW = 800;
    private int scnH = 600;
    private boolean pauseStatus = false; //play/pause animations and yipping updates
    private Label debugText;
    private Date date;
    private long startTime;
    private ODEsolver ODEsolver;
    private final int MAX_ANIMATION_FPS =30;
    private int currentFPS;
    // InterPlanetaryRocket vars
    private double MAX_POSSIBLE_FUEL_AMOUNT;
    private Lunarlander lunarlanderObj;
    private FlameFxViewer mainThrusterIV;
    private final int MAX_MAIN_THRUSTER_WIDTH = 150;
    private final int FASTEST_MAIN_THRUSTER_ANIMATION = 40; //in ms
    private final int SLOWEST_MAIN_THRUSTER_ANIMATION = 15; //in ms

    private ArrayList<Lunarlander> obj;

    private final boolean DEBUG = true;

    @Override
    public void start(Stage landingStage) throws Exception {
        //add any fx componenents to this root group.
        root = new Pane();
        root.setStyle("-fx-background-image: url('textures/galaxy_starfield.png');");

        //our code down below:
        //Lunarlander:
        ImageView rocket = new ImageView(new Image(new FileInputStream("./src/main/resources/InterPlanetaryRocket.png")));
        rocket.setPreserveRatio(true);
        rocket.setFitWidth(100);

        rocket.setLayoutY(50);
        rocket.setLayoutX((scnW - rocket.getFitWidth()) / 2); //center image

        //Fuel Level Display
        ImageView fuel_icon = new ImageView(new Image(new FileInputStream("./src/main/resources/fuel_icon.png")));
        fuel_icon.setPreserveRatio(true);
        fuel_icon.setFitWidth(35);
        fuel_icon.setLayoutX(scnW - fuel_icon.getFitWidth() - 10);
        fuel_icon.setLayoutY(scnH - fuel_icon.getFitWidth() - 10);
        //background rectangle
        Rectangle max_level_bg = new Rectangle();
        max_level_bg.setHeight(150);
        max_level_bg.setWidth(29);
        max_level_bg.setFill(Color.rgb(222, 225, 209));

        max_level_bg.setLayoutX(fuel_icon.getLayoutX() + 3);
        max_level_bg.setLayoutY(fuel_icon.getLayoutY() - 150 + 10);
        //actual level display rectangle
        Rectangle curr_fuel_fg = new Rectangle();
        curr_fuel_fg.setHeight((max_level_bg.getHeight() / MAX_POSSIBLE_FUEL_AMOUNT) * lunarlanderObj.getFuelMass());
        curr_fuel_fg.setWidth(25);
        curr_fuel_fg.setFill(Color.MEDIUMVIOLETRED);
        curr_fuel_fg.setLayoutX(max_level_bg.getLayoutX() + 2);
        curr_fuel_fg.setLayoutY(max_level_bg.getLayoutY() + (150 - curr_fuel_fg.getHeight()));

        fuel_icon.toFront(); //have this icon to front.

        //InterPlanetaryRocket thruster flame
        mainThrusterIV = new FlameFxViewer();
        mainThrusterIV.setPreserveRatio(true);
        mainThrusterIV.setFitWidth(MAX_MAIN_THRUSTER_WIDTH);
        mainThrusterIV.setRotate(180);

        mainThrusterIV.setLayoutX(rocket.getLayoutX() + (rocket.getFitWidth() / 2) - (mainThrusterIV.getFitWidth() / 2));
        mainThrusterIV.setLayoutY(rocket.getLayoutY() + 150);

        //Labels:
        if (DEBUG) {
            debugText = new Label(constructDebugText());
            debugText.setTextFill(Color.WHITE);
            debugText.setLayoutX(10);
            debugText.setLayoutY(10);
            root.getChildren().add(debugText);
        }

        //add components here with a quote:
        root.getChildren().addAll(mainThrusterIV, rocket, max_level_bg, curr_fuel_fg, fuel_icon);

        //below sets up the stage, if implementing as scene, don't forget to remove.
        mainScene = new Scene(root, scnW, scnH, true);

        class verletUpdater implements Runnable {

            private ODEsolver vVref;
            private long lastUpdate = System.nanoTime();
            public verletUpdater(ODEsolver ref) {
                this.vVref = ref;
            }

            public void run() {
                while(!pauseStatus) {
                    if (System.nanoTime() - lastUpdate >= 10 * 1000000) {
                        vVref.updateLocation(10, TimeUnit.MILLISECONDS);
                        lastUpdate = System.nanoTime();
                    }
                }
            }
        }
        Thread vVt = new Thread(new verletUpdater(ODEsolver));
        vVt.start();
        //animations and updates to the yipping elements:
        new AnimationTimer()
        {
            long lastFrame = System.nanoTime(); //... still, it doesn't have to be very precise.
            public void handle(long currentN)
            {
                long differancePerAnimationFrameInMS = (currentN - lastFrame) / 1000000L;
                if (!pauseStatus && differancePerAnimationFrameInMS >= 1000 / MAX_ANIMATION_FPS) {
                    //Update Fuel Display
                    curr_fuel_fg.setHeight((max_level_bg.getHeight() / MAX_POSSIBLE_FUEL_AMOUNT) * lunarlanderObj.getFuelMass());
                    curr_fuel_fg.setLayoutY(max_level_bg.getLayoutY() + (150 - curr_fuel_fg.getHeight()));
                    //Animate Thruster Flame
                    mainThrusterIV.animate();
                    int newmTIVwidth = (int) (MAX_MAIN_THRUSTER_WIDTH * lunarlanderObj.getMainThrusterForceAsPercentage());
                    if (newmTIVwidth != (int) mainThrusterIV.getFitWidth()) mainThrusterIV.setFitWidth(newmTIVwidth);
                    mainThrusterIV.setAnimationSpeed(Math.max(SLOWEST_MAIN_THRUSTER_ANIMATION, (int) (FASTEST_MAIN_THRUSTER_ANIMATION * lunarlanderObj.getMainThrusterForceAsPercentage())));
                    mainThrusterIV.setLayoutX(rocket.getLayoutX() + (rocket.getFitWidth() / 2) - (mainThrusterIV.getFitWidth() / 2));
                    //mainThrusterIV.setLayoutY(InterPlanetaryRocket.getLayoutY() + 150); unnecessary for now...
                    if (DEBUG) debugText.setText(constructDebugText());
                    currentFPS = (int) (1000 / differancePerAnimationFrameInMS);
                    lastFrame= currentN;
                }

            }
        }.start();

        landingStage.setTitle("SolarSysF - Titan Landing");
        landingStage.setScene(mainScene);
        landingStage.setResizable(false);
        landingStage.show();
    }

    public LandingPhase (Lunarlander lunarlanderObj, Date date) throws Exception {
        this.date = date;
        this.lunarlanderObj = lunarlanderObj;
        this.MAX_POSSIBLE_FUEL_AMOUNT = lunarlanderObj.getFuelMass();
        startTime = date.getTimeInMillis();
        obj = new ArrayList<>();
        obj.add(lunarlanderObj);
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
                "y-pos\t: " + df.format(lunarlanderObj.getCentralPos().getY()) + "\n" +
                "y-vel\t: " + df.format(lunarlanderObj.getCentralVel().getY()) + "\n" +
                "x-pos\t: " + df.format(lunarlanderObj.getCentralPos().getX()) + "\n" +
                "x-vel\t: " + df.format(lunarlanderObj.getCentralVel().getX()) + "\n" +
                "t-pos\t: " + df.format(lunarlanderObj.getCentralPos().getZ()) + "\n" +
                "t-vel\t\t: " + df.format(lunarlanderObj.getCentralVel().getZ()) + "\n" +
                "ft%\t\t: " + df.format(lunarlanderObj.getMainThrusterForceAsPercentage()) + "\n" +
                "ft_sp\t: " + mainThrusterIV.getAnimationSpeed() + "\n" +
                "ft_wd\t: " + mainThrusterIV.getFitWidth() + "\n" +
                "fuel\t\t: " + df.format(lunarlanderObj.getFuelMass());
    }
}
