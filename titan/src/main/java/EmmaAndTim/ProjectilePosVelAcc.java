package EmmaAndTim;

public class ProjectilePosVelAcc {
    double initialHorizontalVelocity;
    double initialVerticalVelocity;
    double horizontalPosition;
    double verticalPosition;
    double horizontalVelocity;
    double verticalVelocity;
    double horizontalAcceleration;
    double verticalAcceleration;

    ProjectilePosVelAcc(double initialHorizontalVelocity, double initialVerticalVelocity){
        this.initialHorizontalVelocity=horizontalVelocity;
        this.initialVerticalVelocity=verticalVelocity;
    }

    public double updateVerticalPosition(double initialTime, double finalTime, double initialVerticalPosition){
        double t= finalTime-initialTime;
        double verticalDisplacement=initialVerticalPosition+initialVerticalVelocity*t+0.5*verticalAcceleration*Math.pow(t,2);
        verticalPosition+=verticalDisplacement;
        return verticalPosition;
    }

    public double updateHorizontalPosition(double initialTime, double finalTime, double initialHorizontalPosition){
        double t= finalTime-initialTime;
        double horizontalDisplacement=initialHorizontalPosition+initialHorizontalVelocity*t+0.5*horizontalAcceleration*Math.pow(t,2);
        horizontalPosition+=horizontalDisplacement;
        return horizontalPosition;
    }

    public double updateVerticalVelocity(double initialTime, double finalTime, double initialVerticalVelocity){
        double t=finalTime-initialTime;
        verticalVelocity=initialVerticalVelocity+verticalAcceleration*t;
        return verticalVelocity;
    }

    public double updateHorizontalVelocity(double initialTime, double finalTime){
        double t=finalTime-initialTime;
        horizontalVelocity=initialHorizontalVelocity+horizontalAcceleration*t;
        return horizontalVelocity;
    }

}
