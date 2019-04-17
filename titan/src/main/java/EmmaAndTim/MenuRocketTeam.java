package EmmaAndTim;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class MenuRocketTeam extends Application {

    public void start(Stage mainStage) throws Exception
    {
        Stage menu = new Stage();
        menu.initModality(Modality.APPLICATION_MODAL);
        menu.setTitle("RocketSpecification");
        menu.setMinWidth(200);
        menu.setMinHeight(400);

        Label burnRateL=new Label("Burn Rate");
        Label exhaustL=new Label("Exhaust Velocity");
        Label massL=new Label("Mass");
        Label initialVelL=new Label("Initial Velocity");

        TextField burnRate = new TextField();
        burnRate.setPromptText("burn rate");
        TextField exhaust = new TextField();
        exhaust.setPromptText("exhaust velocity");
        TextField mass = new TextField();
        mass.setPromptText("mass");
        TextField initialVel = new TextField();
        initialVel.setPromptText("initial velocity");

        Button startButton = new Button("To infinity…and beyond!");

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LandingSimulatorCopy rocket= new LandingSimulatorCopy(Double.parseDouble(burnRate.getText()), Double.parseDouble(exhaust.getText()),
                        Double.parseDouble(mass.getText()), Double.parseDouble(initialVel.getText()));
                Stage simulation=new Stage();
                menu.close();
                try{rocket.start(simulation);}
                catch(Exception e){
                    System.out.println("Unable to launch the simulation");
                }
            }
        });


        //Positioning in the window
        GridPane view = new GridPane();
        view.setPadding(new Insets(10, 10, 10, 10));
        view.setVgap(5);
        view.setHgap(3);

        GridPane.setConstraints(burnRateL, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(burnRate, 0, 2, 1, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(exhaustL, 0, 3, 1, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(exhaust, 0, 4, 1, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(massL, 0, 5, 1, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(mass, 0, 6, 1, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(initialVelL, 0, 7, 1, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(initialVel, 0, 8, 1, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(startButton, 0, 9, 1, 1, HPos.CENTER, VPos.CENTER);

        view.getChildren().addAll(burnRate,startButton,exhaust,mass,initialVel, burnRateL, exhaustL, massL, initialVelL);
        Scene sceneAlertBox = new Scene(view);
        menu.setScene(sceneAlertBox);
        menu.showAndWait();

    }

    public static void main(String[] args)
    {
        launch(args);

    }
}
