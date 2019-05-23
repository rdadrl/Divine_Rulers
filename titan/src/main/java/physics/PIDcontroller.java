package physics;

/**
 * http://brettbeauregard.com/blog/2011/04/improving-the-beginner%E2%80%99s-pid-reset-windup/
 * http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.867.4513&rep=rep1&type=pdf
 */
public class PIDcontroller {
    private double Kp;
    private double Ki;
    private double Kd;
    private double target_pos;
    private double target_vel;
    private double error_pos;
    private double error_vel;
    private double oldError;
    private double totalError_pos;
    private double tolerance;
    private double outMin = -Double.MAX_VALUE;
    private double outMax = Double.MAX_VALUE;


    public PIDcontroller(double Kp, double Ki, double Kd, double target_pos, double target_vel, double tolerance) {
        this.Kp = Kp;
        this.Ki = Ki;
        this.Kd = Kd;
        setTarget_pos(target_pos);
        setTarget_vel(target_vel);
        setTolerance(tolerance);
    }

    public PIDcontroller(double Kp, double Ki, double Kd) {
        this(Kp, Ki, Kd, 0, 0, 0);
    }

    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    public void setTarget_pos(double target_pos) {
        this.target_pos = target_pos;
    }

    public void setTarget_vel(double target_vel) {
        this.target_vel = target_vel;
    }

    public boolean onTarget() {
        return  Math.abs(error_pos) < tolerance;
    }

    public void setKd(double kd) {
        Kd = kd;
    }

    public void setKi(double ki) {
        Ki = ki;
    }

    public void setKp(double kp) {
        Kp = kp;
    }

    public void setOutLimits(double outMin, double outMax) {
        if (outMin > outMax) {
            throw new IllegalArgumentException("minimum saturation needs to be less than maximum saturation");
        }
        this.outMin = outMin;
        this.outMax = outMax;
        if(totalError_pos > outMax) totalError_pos = outMax;
        else if(totalError_pos < outMin) totalError_pos = outMin;
    }

    public double calculateOutput(double input, double dt) {
        error_pos = input - target_pos;

        // For P gain
        double pError = Kp * error_pos;

        // For D gain
        double derivatator = (error_pos - oldError)/dt;
        double dError = Kd * derivatator;
        oldError = error_pos;

        // for I gain with anti windup
        totalError_pos += error_pos;
        if(totalError_pos > outMax) {
            totalError_pos = outMax;}
        else if(totalError_pos < outMin) {
            totalError_pos = outMin;}
        double integrator = totalError_pos *dt;
        double iError = Ki * integrator;

        double output = pError + dError + iError;
        if(output > outMax) {output = outMax;}
        if(output < outMin) {output = outMin;}


        return output;
    }

    public double calculateOutput(double pos_input, double vel_input, double dt) {
        error_pos = pos_input - target_pos;
        error_vel = vel_input - target_vel;

        // For P gain
        double pError = Kp * error_pos;
        // For D gain
        double dError = Kd * error_vel;

        // for I gain with anti windup
        totalError_pos += error_pos;
        if(totalError_pos > outMax) {
            totalError_pos = outMax;}
        else if(totalError_pos < outMin) {
            totalError_pos = outMin;}
        double integrator = totalError_pos *dt;
        double iError = Ki * integrator;

        double output = pError + dError + iError;
        if(output > outMax) {output = outMax;}
        if(output < outMin) {output = outMin;}

        return output;
    }

    public void resetTotalError(){
        totalError_pos = 0;
    }

    public void reduceTotalError(double percentage){
        totalError_pos = totalError_pos *percentage;
    }
}
