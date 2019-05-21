package solarsysf;

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
import physics.VerletVelocity;
import solarsystem.Rocket;
import utils.Date;

import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class LandingPhase extends Application {
    // gui variables
    private Scene mainScene;
    private Pane root;
    private int scnW = 800;
    private int scnH = 600;
    private boolean pauseStatus = false; //play/pause animations and gui updates
    private Label debugText;
    private Date date;
    private long startTime;
    private VerletVelocity verletVelocity;
    private final int MAX_ANIMATION_FPS =30;
    private int currentFPS;
    // rocket vars
    private double MAX_POSSIBLE_FUEL_AMOUNT;
    private Rocket rocketObj;
    private FlameFxViewer mainThrusterIV;
    private final int MAX_MAIN_THRUSTER_WIDTH = 150;
    private final int FASTEST_MAIN_THRUSTER_ANIMATION = 40; //in ms
    private final int SLOWEST_MAIN_THRUSTER_ANIMATION = 15; //in ms

    private final boolean DEBUG = true;

    @Override
    public void start(Stage landingStage) throws Exception {
        //add any fx componenents to this root group.
        root = new Pane();
        root.setStyle("-fx-background-image: url('textures/galaxy_starfield.png');");

        //our code down below:
        //Rocket:
        ImageView rocket = new ImageView(new Image(new FileInputStream("./src/main/resources/rocket.png")));
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
        curr_fuel_fg.setHeight((max_level_bg.getHeight() / MAX_POSSIBLE_FUEL_AMOUNT) * rocketObj.getFuelMass());
        curr_fuel_fg.setWidth(25);
        curr_fuel_fg.setFill(Color.MEDIUMVIOLETRED);
        curr_fuel_fg.setLayoutX(max_level_bg.getLayoutX() + 2);
        curr_fuel_fg.setLayoutY(max_level_bg.getLayoutY() + (150 - curr_fuel_fg.getHeight()));

        fuel_icon.toFront(); //have this icon to front.

        //rocket thruster flame
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

            private VerletVelocity vVref;
            private long lastUpdate = System.nanoTime();
            public verletUpdater(VerletVelocity ref) {
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
        Thread vVt = new Thread(new verletUpdater(verletVelocity));
        vVt.start();
        //animations and updates to the gui elements:
        new AnimationTimer()
        {
            long lastFrame = System.nanoTime(); //... still, it doesn't have to be very precise.
            public void handle(long currentN)
            {
                long differancePerAnimationFrameInMS = (currentN - lastFrame) / 1000000L;
                if (!pauseStatus && differancePerAnimationFrameInMS >= 1000 / MAX_ANIMATION_FPS) {
                    //Update Fuel Display
                    curr_fuel_fg.setHeight((max_level_bg.getHeight() / MAX_POSSIBLE_FUEL_AMOUNT) * rocketObj.getFuelMass());
                    curr_fuel_fg.setLayoutY(max_level_bg.getLayoutY() + (150 - curr_fuel_fg.getHeight()));
                    //Animate Thruster Flame
                    mainThrusterIV.animate();
                    int newmTIVwidth = (int) (MAX_MAIN_THRUSTER_WIDTH * rocketObj.getMainThrusterForceAsPercentage());
                    if (newmTIVwidth != (int) mainThrusterIV.getFitWidth()) mainThrusterIV.setFitWidth(newmTIVwidth);
                    mainThrusterIV.setAnimationSpeed(Math.max(SLOWEST_MAIN_THRUSTER_ANIMATION, (int) (FASTEST_MAIN_THRUSTER_ANIMATION * rocketObj.getMainThrusterForceAsPercentage())));
                    mainThrusterIV.setLayoutX(rocket.getLayoutX() + (rocket.getFitWidth() / 2) - (mainThrusterIV.getFitWidth() / 2));
                    //mainThrusterIV.setLayoutY(rocket.getLayoutY() + 150); unnecessary for now...
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

    public LandingPhase (Rocket rocketObj, Date date) throws Exception {
        this.date = date;
        this.rocketObj = rocketObj;
        this.MAX_POSSIBLE_FUEL_AMOUNT = rocketObj.getFuelMass();
        startTime = date.getTimeInMillis();
        ArrayList<Rocket> obj = new ArrayList<>();
        obj.add(rocketObj);
        this.verletVelocity = new VerletVelocity(obj, date);

        //push to start
    }

    public String constructDebugText () {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        return  "fps\t\t: " + currentFPS + "\n" +
                "sec\t\t: " + df.format((date.getTimeInMillis()-startTime)/1000D) + "\n" +
                "y-pos\t: " + df.format(rocketObj.getCentralPos().getY()) + "\n" +
                "y-vel\t: " + df.format(rocketObj.getCentralVel().getY()) + "\n" +
                "x-pos\t: " + df.format(rocketObj.getCentralPos().getX()) + "\n" +
                "x-vel\t: " + df.format(rocketObj.getCentralVel().getX()) + "\n" +
                "t-pos\t: " + df.format(rocketObj.getCentralPos().getZ()) + "\n" +
                "t-vel\t\t: " + df.format(rocketObj.getCentralVel().getZ()) + "\n" +
                "ft%\t\t: " + df.format(rocketObj.getMainThrusterForceAsPercentage()) + "\n" +
                "ft_sp\t: " + mainThrusterIV.getAnimationSpeed() + "\n" +
                "ft_wd\t: " + mainThrusterIV.getFitWidth() + "\n" +
                "fuel\t\t: " + df.format(rocketObj.getFuelMass());
    }
}
