package physics;

import utils.MathUtil;
import utils.vector.Vector3D;


/**
 *
 * Solver of the lambert problem using Izzie algorithm. Not done yet!
 *
 * Sources:
 * https://github.com/poliastro/poliastro/blob/master/src/poliastro/core/iod.py
 * https://github.com/esa/pykep/blob/master/src/lambert_problem.cpp
 *
 * TODO: REWRITE
 *
 *
 */
public abstract class LambertSolver {
    public LambertSolver(Vector3D r1, Vector3D r2, double t, double mu) {
        // check the requirements
        if(t <= 0) {
            throw new IllegalArgumentException("Flight time needs to be positive");
        }
        if(mu <=0) {
            throw new IllegalArgumentException("Gravity parameter needs to be positive");
        }
        // initialize parameters
        Vector3D c = r2.substract(r1); // chord
        double c_n = c.norm(); // norm of the cord
        double r1_n = r1.norm(); // norm of position vector 1
        double r2_n = r2.norm(); // norm of position vector 2
        double s = (r1_n + r2_n + c_n)/2.0; // semi-perimeter
        Vector3D ir1 = r1.unit(); // unit vector of r1
        Vector3D ir2 = r2.unit(); // unit vector of r2
        Vector3D ih = ir1.cross(ir2); // angular momentum
        ih = ih.unit();
        double lambda2 = 1.0 - (c_n/s);
        double lambda = Math.sqrt(lambda2);

        // initialize it1 and it2
        Vector3D it1, it2;
        if(ih.getZ() == 0){
            throw new IllegalArgumentException("The angular component needs to have a z component");
        }else if(ih.getZ() < 0){
            lambda = - lambda;
            it1 = ir1.cross(ih);
            it2 = ir2.cross(ih);
        }else{
            it1 = ih.cross(ir1);
            it2 = ih.cross(ir2);
        }
        it1 = it1.unit();
        it2 = it2.unit();

        double T = Math.sqrt((2.0 * mu) / s * s * s) * t;

    }

    private double[] findxy(double lambda, double T) {
        if(!(Math.abs(lambda) < 1)){  throw new IllegalArgumentException();}
        if(!(T > 0)){  throw new IllegalArgumentException();}
        double M_max = Math.floor(T / Math.PI);
        double T_00 = Math.acos(lambda) + lambda * Math.sqrt(1 - lambda * lambda);
        double T_0 = T_00 + M_max * Math.PI;
        if ((T < T_0) && (M_max > 0)) {
            double T_min = halley(T_0, 0, lambda, M_max, 1E-13, 12);

            if (T_min > T) {
                M_max = M_max - 1;
            }
        }

        return null;
    }

    private double halley(double T_min, double x0, double lambda, double M_max, double eps,
                            double iter_max) {
        double x_old = x0;
        double x_new = 0.0;
        double err, DT, DDT, DDDT; double[] derivatives;
        for (int i = 0; i < iter_max; i++){
            derivatives = derivatives(T_min, x_old, lambda);
            DT = derivatives[0];
            DDT = derivatives[1];
            DDDT = derivatives[2];
            if(DT != 0) {
                x_new = x_old - ((DT * DDT ) / ((DDT * DDT) - (DT * DDDT / 2.0)));
            }
            err = Math.abs(x_old - x_new);
            if(err < eps) {
                return T_min;
            }
            T_min = x2tof(x_new, lambda, M_max);
            x_old = x_new;
        }
        throw new RuntimeException("Hayley itteration failed to converge");
    }

    private double x2tof(double x, double lambda, double N) {
        double series = 0.01;
        double larange = 0.2;
        double dist = Math.abs(x - 1);
        double l2 = lambda * lambda;
        if ((dist < larange) && (dist > series)) {
            double a = 1.0 / (1.0 - x * x);
            if (a > 0) {
                double alpha = 2.0 * Math.acos(x);
                double beta = 2.0 * Math.asin(Math.sqrt(l2/ a));
                if (lambda < 0.0) beta = -beta;
                return ((a * Math.sqrt(a) * ((alpha - Math.sin(alpha)) - (beta - Math.sin(beta)) + 2.0 * Math.PI * N)) / 2.0);
            } else {
                double alpha = 2.0 * MathUtil.acosh(x);
                double beta = 2.0 * MathUtil.asinh(Math.sqrt(-l2/ a));
                if (lambda < 0.0) beta = -beta;
                return (-a * Math.sqrt(-a) * ((beta - Math.sinh(beta)) - (alpha - Math.sinh(alpha))) / 2.0);
            }
        }
        double K = l2;
        double E = x * x - 1.0;
        double rho = Math.abs(E);
        double z = Math.sqrt(1 + K * E);
        if (dist < series) {
            double eta = z - lambda * x;
            double S1 = 0.5 * (1 - lambda - x * eta);
            double Q = (4.0 /3.0) * F(S1, 1E-11);
            return (eta * eta * eta * Q + 4.0 * lambda * eta) / 2.0 + N * Math.PI / Math.pow(rho,
                    1.5);
        } else {
            double y = Math.sqrt(rho);
            double g = x * z - lambda * E;
            double d;
            if (E < 0) {
                double l = Math.acos(g);
                d = N * Math.PI + l;
            } else {
                double f = y * (z - lambda * x);
                d = Math.log(f + g);
            }
            return (x - lambda * z - d / y ) / E;
        }
    }




    /*def x2tof2(x,lam,N=0):
            """
    Computes the non-dimensional time of flight T as a function of x where
    x is the Lancaster-Blanchard variable. It uses different expressions (Lagrange,
                                                                          Battin or Lancaster-Blanchard)) according
    to the distance to x=1, as to avoid numerical problems of different formulations
	"""
    from numpy import sqrt,arccos,pi,log
	#from math import atan2
        series = 0.01
        lagrange = 0.2
        dist = abs(x-1)
        if dist < lagrange and dist > series:
                return x2tof(x,lam,N)
        K = lam*lam
        E = x*x-1
        rho = abs(E)
        z = sqrt(1+K*E)
	    if dist < series:
            eta = z-lam*x
            S1 = 0.5*(1-lam-x*eta)
            Q,_ = F(3.0,1.0,5.0/2.0,S1,1e-11)
            Q = 4.0/3.0*Q
            return (eta**3*Q+4*lam*eta)/2 + N*pi / (rho**(3.0/2.0))
        else:
            y=sqrt(rho)
            g = x*z - lam*E
            if E<0:
                l = arccos(g)
                d=N*pi+l
		    if E>0:
                f = y*(z-lam*x)
                d=log(f+g)
		return (x-lam*z-d/y)/E
	*/

    private double[] derivatives(double T, double x, double lambda){
        // set up shortcuts
        double l2 = lambda * lambda;
        double l3 = l2 * lambda;
        double l5 = l2 * l3;
        double y = Math.sqrt(1.0 - l2 * (1.0 - x * x));
        double y2 = y * y;
        double y3 = y2 * y;
        double y5 = y2 * y3;
        // calculate derivatives
        double DT = (3.0 * T * x - 2.0 + 2.0 * l3 * x / y);
        DT = DT / (1.0 - x * x);
        double DDT = (3.0 * T +5.0 * x * DT + 2.0 * (1.0 - l2) * l3 / y3);
        DDT = DDT / (1.0 - x * x);
        double DDDT = (7.0 * x * DDT) + (8.0 * DT) - (6.0 * (1.0 - l2) * l5 * x / y5);
        DDDT = DDDT / (1.0 - x * x);
        return new double[]{DT, DDT, DDDT};
    }

    private double F(double z, double tol) {
        double Sj = 1.0;
        double Cj = 1.0;
        double err = 1.0;
        double j = 0;
        double Cj1, Sj1;
        while (err > tol) {
            Cj1 = Cj * (3.0 + j) * (1.0 + j) / (2.5 + j) * z / (j + 1);
            Sj1 = Sj + Cj1;
            err = Math.abs(Cj1);
            Sj = Sj1;
            Cj = Cj1;
            j = j + 1;
        }
        return Sj;
    }
}
