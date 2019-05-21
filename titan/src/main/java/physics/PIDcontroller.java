package physics;

/**
 * http://brettbeauregard.com/blog/2011/04/improving-the-beginner%E2%80%99s-pid-reset-windup/
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
    private double outMin = -Double.MAX_VALUE;
    private double outMax = Double.MAX_VALUE;


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

    public void setOutLimits(double outMin, double outMax) {
        if (outMin > outMax) {
            throw new IllegalArgumentException("minimum saturation needs to be less than maximum saturation");
        }
        this.outMin = outMin;
        this.outMax = outMax;
        if(totalError> outMax) totalError= outMax;
        else if(totalError< outMin) totalError= outMin;
    }

    public double calculateOutput(double input, double dt) {
        error = input - target;

        // For P gain
        double pError = Kp * error ;

        // For D gain
        double derivatator = (error - oldError)/dt;
        double dError = Kd * derivatator;
        oldError = error;

        // for I gain with anti windup
        totalError += error;
        if(totalError> outMax) {totalError= outMax;}
        else if(totalError< outMin) {totalError= outMin;}
        double integrator = totalError *dt;
        double iError = Ki * integrator;

        double output = pError + dError + iError;
        if(output > outMax) {output = outMax;}
        if(output < outMin) {output = outMin;}


        return output;
    }

    public void resetTotalError(){
        totalError = 0;
    }

    public void reduceTotalError(double percentage){
        totalError = totalError*percentage;
    }
}
