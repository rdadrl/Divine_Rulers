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

    public PIDcontroller(double Kp, double Ki, double Kd) {
        this.Kp = Kp;
        this.Ki = Ki;
        this.Kd = Kd;
    }

    public PIDcontroller(double Kp, double Ki, double Kd, double goal, double tolerance) {
        this(Kp, Ki, Kd);
        setTarget(goal);
        setTolerance(tolerance);
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

    public double calculateOutput(double input) {
        error = target - input;
        totalError += error;
        double pError = Kp * error;
        double dError = Kd * (error - oldError);
        double iError = Ki * totalError;

        double output = pError + dError + iError;
        oldError = error;

        return output;
    }
}
