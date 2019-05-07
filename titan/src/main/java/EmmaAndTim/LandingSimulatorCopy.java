package EmmaAndTim;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;
import javafx.stage.Stage;

import java.io.File;

public class LandingSimulatorCopy extends Application {
    double windowWidth = 500.0D;
    double windowHeight = 500.0D;
    public double x;
    final double r;
    public double y;
    double k;
    double u;
    double v_0;
    double m_0;
    final double g;
    final double TitanX;
    final double TitanY;
    public int t;

    public LandingSimulatorCopy() {
        this.x = this.windowWidth / 2.0;
        this.r = 50.0;
        this.y = this.windowHeight - this.windowHeight / 5.0 - 50.0;
        this.k = 0.1;
        this.u = 0.2;
        this.v_0 = 0.0;
        this.m_0 = 100.0;
        this.g = 1.352;
        this.TitanX = this.windowWidth / 2.0;
        this.TitanY = this.windowHeight - this.windowHeight / 5.0;
        this.t = 0;
    }

    public LandingSimulatorCopy(double burnRate, double exhaust, double mass, double initialVel){
        k=burnRate;
        this.x = this.windowWidth / 2.0;
        this.r = 50.0;
        this.y = this.windowHeight - this.windowHeight / 5.0 - 50.0;
        u = exhaust;
        v_0 = initialVel;
        m_0 = mass;
        this.g = 1.352;
        this.TitanX = this.windowWidth / 2.0;
        this.TitanY = this.windowHeight - this.windowHeight / 5.0;
        this.t = 0;
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Team Rocket_temp");
        Group root = new Group();
        Canvas canvas = new Canvas(this.windowWidth, this.windowHeight);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        this.drawRocket(graphicsContext);
        Label label = new Label();
        Circle titan = new Circle();
        titan.setCenterX(this.TitanX);
        titan.setCenterY(this.TitanY);
        titan.setRadius(50.0D);
        File f = new File("D:\\titann.PNG");
        Image image=new Image(f.toURI().toString());
        titan.setFill(new ImagePattern(image));
        Task<Void> task = new Task<Void>() {
            public Void call() throws Exception {
                for(int i = 0; i < 100; ++i) {
                    System.out.println("T = " + LandingSimulatorCopy.this.t);
                    ++LandingSimulatorCopy.this.t;
                    this.updateMessage("\t\tT: " + LandingSimulatorCopy.this.t + "\n\t\tX: " + LandingSimulatorCopy.this.x + "\n\t\tY: " + LandingSimulatorCopy.this.y);
                    Thread.sleep(250);
                }

                return null;
            }
        };
        task.messageProperty().addListener((obs, oldMessage, newMessage) -> {
            System.out.println("Y = " + this.y);
            if (this.y > 0) {
                System.out.println("Update");
                label.setText(newMessage);
                this.updatePosition();
                this.drawRocket(graphicsContext);
            }

        });
        (new Thread(task)).start();

        root.getChildren().addAll(new Node[]{canvas, label, titan});
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void updatePosition() {
        double m = this.m_0 - this.k * (double)this.t;
        double q = this.v_0 * (double)this.t;
        double p = m * Math.log(m / this.m_0) + this.k * (double)this.t;
        double s = -1.352D * (double)this.t / (2.0D * this.k);
//        System.out.println("m = " + m);
//        System.out.println("q = " + q);
//        System.out.println("p = " + p);
//        System.out.println("Log = " + Math.log(m / this.m_0));
//        System.out.println("s = " + s);
//        System.out.println("y = " + this.y);
        this.y += q + this.u / this.k * p + s;
    }

    public void updateVelocity() {
    }

    public void drawRocket(GraphicsContext graphicsContext) {
        graphicsContext.clearRect(0.0, 0.0, this.windowWidth, this.windowHeight);
        graphicsContext.strokePolygon(new double[]{this.x, this.x - 8, this.x - 8, this.x - 2, this.x - 6, this.x + 6, this.x + 2, this.x + 8, this.x + 8, this.x}, new double[]{this.y - 50, this.y - 40, this.y - 10, this.y - 10, this.y, this.y, this.y - 10, this.y - 10, this.y - 40, this.y - 50}, 10);
    }

    public static void main(String[] args) {
        launch(args);
    }
}