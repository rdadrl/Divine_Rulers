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
import solarsystem.Projectile;
import solarsystem.Rocket;
import solarsystem.RocketLanderClosedLoop;
import utils.Date;
import utils.MathUtil;
import utils.Vector3D;

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
    private int updateVerletPerFrame = 10;

    // rocket vars
    private double MAX_POSSIBLE_FUEL_AMOUNT;
    private Rocket rocketObj;


    private final boolean DEBUG = true;

    @Override
    public void start(Stage landingStage) throws Exception {
        //add any fx componenents to this root group.
        root = new Pane();
        root.setStyle("-fx-background-image: url('textures/galaxy_starfield.png');");

        //our code down below:
        //Rocket_temp:
        ImageView rocket = new ImageView(new Image(new FileInputStream("./src/main/resources/rocket.png")));
        rocket.setPreserveRatio(true);
        rocket.setFitWidth(100);

        rocket.setLayoutY(50);
        rocket.setLayoutX((scnW - rocket.getFitWidth()) / 2); //center image

        //Labels:
        if (DEBUG) {
            debugText = new Label(constructDebugText());
            debugText.setTextFill(Color.WHITE);
            debugText.setLayoutX(10);
            debugText.setLayoutY(10);
            root.getChildren().add(debugText);
        }

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
        //add components here with a quote:
        root.getChildren().addAll(rocket, max_level_bg, curr_fuel_fg, fuel_icon);

        //below sets up the stage, if implementing as scene, don't forget to remove.
        mainScene = new Scene(root, scnW, scnH, true);

        //animations and updates to the gui elements:
        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                if (!pauseStatus) {
                    for(int i = 0; i < updateVerletPerFrame; i++) verletVelocity.updateLocation(10,
                            TimeUnit.MILLISECONDS);
                    if (DEBUG) debugText.setText(constructDebugText());
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
        return  "sec: " + df.format((date.getTimeInMillis()-startTime)/1000D) + "\n" +
                "y-pos: " + df.format(rocketObj.getCentralPos().getY()) + "\n" +
                "y-vel: " + df.format(rocketObj.getCentralVel().getY()) + "\n" +
                "x-pos: " + df.format(rocketObj.getCentralPos().getX()) + "\n" +
                "x-vel: " + df.format(rocketObj.getCentralVel().getX()) + "\n" +
                "t-pos: " + df.format(rocketObj.getCentralPos().getZ()) + "\n" +
                "t-vel: " + df.format(rocketObj.getCentralVel().getZ()) + "\n" +
                "Fuel    : " + df.format(rocketObj.getFuelMass());
    }
}
