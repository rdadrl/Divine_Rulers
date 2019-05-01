package physics;

/**
 *
 *
 */
public class PIDcontroller {
    private double Kp;
    private double Ki;
    private double Kd;
    private double target;
    private double error;
    private double oldError;
    private double totalError;
    private double tolerance;


    public PIDcontroller(double Kp, double Ki, double Kd, double goal, double tolerance) {
        this.Kp = Kp;
        this.Ki = Ki;
        this.Kd = Kd;
        setTarget(goal);
        setTolerance(tolerance);
    }

    public PIDcontroller(double Kp, double Ki, double Kd) {
        this(Kp, Ki, Kd, 0, 0);
    }

    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public boolean onTarget() {
        return  Math.abs(error) < tolerance;
    }

    public double calculateOutput(double input, double dt) {
        error = input - target;
        totalError += error;
        double pError = Kp * error ;
        double derivatator = (error - oldError)/dt;
        double dError = Kd * derivatator;
        double iError = Ki * totalError *dt;

        double output = pError + dError + iError;
        oldError = error;

        return output;
    }
}
