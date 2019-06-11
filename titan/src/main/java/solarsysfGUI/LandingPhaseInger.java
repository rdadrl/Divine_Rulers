package solarsysfGUI;

/**
 *
 *
 */

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
import solarsystem.rocket.lunarLander.Lunarlander;
import utils.Date;

import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class LandingPhaseInger extends Application {
    // yipping variables
    private Scene mainScene;
    private Pane root;
    private int scnW = 800;
    private int scnH = 600;
    private boolean pauseStatus = false; //play/pause animations and yipping updates
    private Label debugText;
    private Date date;
    private long startTime;
    private VerletVelocity verletVelocity;
    private int updateVerletPerFrame = 70;

    // rocket vars
    private double MAX_POSSIBLE_FUEL_AMOUNT;
    private Lunarlander lunarlanderObj;


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
        curr_fuel_fg.setHeight((max_level_bg.getHeight() / MAX_POSSIBLE_FUEL_AMOUNT) * lunarlanderObj.getFuelMass());
        curr_fuel_fg.setWidth(25);
        curr_fuel_fg.setFill(Color.MEDIUMVIOLETRED);
        curr_fuel_fg.setLayoutX(max_level_bg.getLayoutX() + 2);
        curr_fuel_fg.setLayoutY(max_level_bg.getLayoutY() + (150 - curr_fuel_fg.getHeight()));

        fuel_icon.toFront(); //have this icon to front.
        //add components here with a quote:
        root.getChildren().addAll(rocket, max_level_bg, curr_fuel_fg, fuel_icon);

        //below sets up the stage, if implementing as scene, don't forget to remove.
        mainScene = new Scene(root, scnW, scnH, true);

        //animations and updates to the yipping elements:
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

    public LandingPhaseInger (Lunarlander lunarlanderObj, Date date) throws Exception {
        this.date = date;
        this.lunarlanderObj = lunarlanderObj;
        this.MAX_POSSIBLE_FUEL_AMOUNT = lunarlanderObj.getFuelMass();
        startTime = date.getTimeInMillis();
        ArrayList<Lunarlander> obj = new ArrayList<>();
        obj.add(lunarlanderObj);
        this.verletVelocity = new VerletVelocity(obj, date);

        //push to start
    }

    public String constructDebugText () {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        return  "sec: " + df.format((date.getTimeInMillis()-startTime)/1000D) + "\n" +
                "y-pos: " + df.format(lunarlanderObj.getCentralPos().getY()) + "\n" +
                "y-vel: " + df.format(lunarlanderObj.getCentralVel().getY()) + "\n" +
                "x-pos: " + df.format(lunarlanderObj.getCentralPos().getX()) + "\n" +
                "x-vel: " + df.format(lunarlanderObj.getCentralVel().getX()) + "\n" +
                "t-pos: " + df.format(lunarlanderObj.getCentralPos().getZ()) + "\n" +
                "t-vel: " + df.format(lunarlanderObj.getCentralVel().getZ()) + "\n" +
                "Fuel    : " + df.format(lunarlanderObj.getFuelMass());
    }
}
