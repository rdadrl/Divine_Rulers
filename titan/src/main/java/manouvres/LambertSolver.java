package manouvres;

import utils.MathUtil;
import utils.vector.Vector3D;

import java.util.ArrayList;


/**
 *
 * Solver of the lambert problem using Izzie algorithm. Not done yet!
 *
 * This code has been adopted from the poliastro code.
 * Sources:
 * https://github.com/poliastro/poliastro/blob/master/src/poliastro/core/iod.py
 * https://github.com/esa/pykep/blob/master/src/lambert_problem.cpp
 *
 * TODO: REWRITE
 *
 */
public class LambertSolver {
    private ArrayList<Vector3D[]> velocityVectors;

    public LambertSolver(double k, Vector3D r1, Vector3D r2, double tof) {
        this(k, r1, r2, tof, 0, 35, 1e-8);
    }

    /**
     * @param r1 position from departure planet
     * @param r2 position arrival planet
     * @param tof time of flight
     * @param k gravitational parameter
     */
    public LambertSolver(double k, Vector3D r1, Vector3D r2, double tof, double M, double numiter, double rtol) {
        velocityVectors = izzo (k, r1, r2, tof, M, numiter, rtol);
    }

    static ArrayList<Vector3D[]> izzo(double k, Vector3D r1, Vector3D r2, double tof, double M, double numiter, double rtol) {

        // check the requirements
        if(tof <= 0) {
            throw new IllegalArgumentException("Flight time needs to be positive");
        }
        if(k <=0) {
            throw new IllegalArgumentException("Gravity parameter needs to be positive");
        }

        // chec collinearity

        // initialize parameters
        Vector3D c = r2.substract(r1); // chord
        double c_norm = c.norm(); // norm of the cord
        double r1_norm = r1.norm(); // norm of position vector 1
        double r2_norm = r2.norm(); // norm of position vector 2

        double s = (r1_norm + r2_norm + c_norm)/2.0; // semi-perimeter
        Vector3D i_r1 = r1.unit(); // unit vector of r1
        Vector3D i_r2 = r2.unit(); // unit vector of r2
        Vector3D i_h = i_r1.cross(i_r2); // angular momentum
        i_h = i_h.unit();

        double lambda2 = Math.min(1.0, 1.0 - (c_norm/s));
        double ll = Math.sqrt(lambda2);

        // initialize it1 and it2
        Vector3D i_t1, i_t2;
        if(i_h.getZ() == 0){
            throw new IllegalArgumentException("The angular component needs to have a z component");
        }else if(i_h.getZ() < 0){
            ll = - ll;
            i_t1 = i_r1.cross(i_h);
            i_t2 = i_r2.cross(i_h);
        }else{
            i_t1 = i_h.cross(i_r1);
            i_t2 = i_h.cross(i_r2);
        }
        //i_t1 = i_t1.unit();
        //i_t2 = i_t2.unit();

        // Non dimensional time of flight
        double T = Math.sqrt((2.0 * k) / (s * s * s)) * tof;

        // checked until T.


        ArrayList<double[]> xy = findxy(ll, T, M, numiter, rtol);

        // Reconstruct
        double gamma = Math.sqrt(k * s / 2);
        double rho = (r1_norm - r2_norm) / c_norm;
        double sigma = Math.sqrt(1 - rho * rho);

        ArrayList<Vector3D[]> results = new ArrayList<>();

        for (double[] xy_itt:  xy){
            double x    = xy_itt[0];
            double y    = xy_itt[1];
            double[] v_arr = reconstruct(x, y, r1_norm, r2_norm, ll, gamma, rho, sigma);
            double V_r1 = v_arr[0];
            double V_r2 = v_arr[1];
            double V_t1 = v_arr[2];
            double V_t2 = v_arr[3];

            Vector3D v1 = i_r1.scale(V_r1).add(i_t1.scale(V_t1));
            Vector3D v2 = i_r2.scale(V_r2).add(i_t2.scale(V_t2));
            results.add(new Vector3D[]{v1, v2});
        }
        return results;
    }

    static double[] reconstruct(double x, double y, double r1, double r2, double ll, double gamma, double rho, double sigma) {
        //Reconstruct solution velocity vectors.
        double V_r1 = gamma * ((ll * y - x) - rho * (ll * y + x)) / r1;
        double V_r2 = -gamma * ((ll * y - x) + rho * (ll * y + x)) / r2;
        double V_t1 = gamma * sigma * (y + ll * x) / r1;
        double V_t2 = gamma * sigma * (y + ll * x) / r2;
        return new double[]{V_r1, V_r2, V_t1, V_t2};
    }

    /**
     * Computes all x, y for given number of revolutions
     */
     static ArrayList<double[]> findxy(double ll, double T, double M, double numiter, double rtol) {
        if(!(Math.abs(ll) < 1)){  throw new IllegalArgumentException();}
        if(!(T > 0)){  throw new IllegalArgumentException();} // mistake original paper

        double M_max = Math.floor(T / Math.PI);
        double T_00 = Math.acos(ll) + ll * Math.sqrt(1 - ll * ll);
        double T_0 = T_00 + M_max * Math.PI;

        // refine maximum number of revolutions if necessary.
        if ((T < T_0) && (M_max > 0)) {
            double[] T_min_arr = compute_T_min(ll, M_max, numiter, rtol);
            double T_min = T_min_arr[1];

            if (T_min > T) {
                M_max = M_max - 1;
            }
        }

        // Checks if the a feasible solutions exists for the given number of revolutions.
        if(M > M_max) {
            throw new IllegalArgumentException("No feasible solution, try lower M");
        }

        ArrayList<double[]> results = new ArrayList<>();

        for (double x_0: initial_guess(T, ll, M)) {
            double x = householder(x_0, T, ll, M, rtol, numiter);
            double y = compute_y(x, ll);
            results.add(new double[]{x,y});
        }
        return results;
    }

    /**
     * TESTED
     * def _compute_y(x, ll):
     * computes y
     * @param x x value
     * @param ll lambda
     * @return y value
     */
    static double compute_y(double x, double ll) {
        return Math.sqrt(1 - ll * ll * (1 - x * x));
    }

    static double compute_psi(double x, double y, double ll){
        // Computes psi.
        // The auxiliary angle psi is computed using Eq.(17) by the appropriate
        // inverse function

        if ((-1 <= x) && (x < 1)){
            // Elliptic motion
            // Use arc cosine to avoid numerical errors
            return Math.acos(x * y + ll * (1 - x * x));
        }else if(x > 1){
            // Hyperbolic motion
            // The hyperbolic sine is bijective
            return MathUtil.asinh((y - x * ll) * Math.sqrt(x * x - 1));
        } else {
            // Parabolic motion
            return 0.0;
        }
    }

    // TESTED
    static double tof_equation(double x, double y, double T0, double ll, double M) {
        //Time of flight equation.
        double T_;
        if ((M == 0) && (Math.sqrt(0.6) < x) && (x < Math.sqrt(1.4))){
            double eta = y - ll * x;
            double S_1 = (1 - ll - x * eta) * 0.5;
            double Q = 4.0 / 3.0 * hyp2f1b(S_1, 1e-13);
            T_ = (eta * eta * eta * Q + 4.0 * ll * eta) * 0.5;
        }else {
            double psi = compute_psi(x, y, ll);
            double div_1 = (psi + M * Math.PI)/(Math.sqrt(Math.abs(1 - x * x)));
            double num_2 = div_1 - x + ll * y;
            T_ = num_2 / (1 - x * x);
        }
        return T_ - T0;
    }


    //TODO: check
    static double tof_equation_p(double x, double y, double T, double ll) {
        return (3 * T * x - 2 + 2 * ll * ll * ll * x / y) /(1 - x * x);
    }


    //TODO: check
    static double tof_equation_p2(double x, double y, double T, double dT, double ll) {
        return (3 * T + 5 * x * dT + 2 * (1 - ll * ll) * ll * ll * ll / (y * y * y)) /(1 - x * x);
    }


    //TODO: check
    static double tof_equation_p3(double x, double y, double T, double dT, double ddT, double ll){
        return (7 * x * ddT + 8 * dT - 6 * (1 - (ll * ll)) * Math.pow(ll, 5) * x / Math.pow(y, 5)) /
                (1 - x * x);
    }

    static double[] compute_T_min(double ll, double M, double numiter, double rtol) {
        //Compute minimum T.
        double x_T_min, T_min;
        if (ll == 1) {
            x_T_min = 0.0;
            T_min = tof_equation(x_T_min, compute_y(x_T_min, ll), 0.0, ll, M);
        }else {
            if (M == 0){
                x_T_min = Double.POSITIVE_INFINITY;
                T_min = 0.0;
            }else {
                //Set x_i >0 to avoid problems at ll = -1
                double x_i = 0.1;
                double y = compute_y(x_i, ll);
                double T_i = tof_equation(x_i, y, 0.0, ll, M);
                x_T_min = halley(0.1, T_i, ll, rtol, numiter);
                T_min = tof_equation(x_T_min, y, 0.0, ll, M);
            }
        }
        return new double[]{x_T_min, T_min};
    }


    static double[] initial_guess(double T, double ll, double M) {
        if (M == 0) {
            // Single revolution
            double T_0 = Math.acos(ll) + ll * Math.sqrt(1 - (ll * ll)) + M * Math.PI;  // Equation 19
            double T_1 = 2 * (1 - (ll * ll * ll)) / 3.0; // Equation 21
            double x_0;
            if (T >= T_0) {
                x_0 = Math.pow((T_0 / T), (2.0 / 3.0)) - 1;
            } else if (T < T_1) {
                x_0 = 5.0 / 2.0 * T_1 / T * (T_1 - T) / (1 - Math.pow(ll, 5)) + 1;
            } else {
                // This is the real condition, which is not exactly equivalent
                double log2_val = Math.log(T_1 / T_0) / Math.log(2);
                x_0 = Math.pow((T_0 / T), log2_val) - 1;
            }
            return new double[]{x_0};
        }else {
            double x_0l = (Math.pow((M * Math.PI + Math.PI) / (8.0 * T), (2.0 / 3.0)) - 1.0) /
                    (Math.pow((M * Math.PI + Math.PI) / (8 * T), (2.0 / 3.0)) + 1);
            double x_0r = (Math.pow((8 * T) / (M * Math.PI), (2.0 / 3.0)) - 1.0) /
                    (Math.pow((8.0 * T) / (M * Math.PI), (2.0 / 3.0)) + 1);
            return new double[]{x_0l, x_0r};
        }
    }


    static double halley(double p0, double T0, double ll, double tol, double maxiter){
        // Find a minimum of time of flight equation using the Halley method.
        for (int ii = 0; ii  < maxiter; ii++){
            double y = compute_y(p0, ll);
            double fder = tof_equation_p(p0, y, T0, ll);
            double fder2 = tof_equation_p2(p0, y, T0, fder, ll);
            if (fder2 == 0){
                throw new RuntimeException("Derivative was zero");
            }
            double fder3 = tof_equation_p3(p0, y, T0, fder, fder2, ll);

            // Halley step (cubic)
            double p = p0 - 2 * fder * fder2 / (2 * fder2 * fder2 - fder * fder3);

            if (Math.abs(p - p0) < tol){
                return p;
            }
            p0 = p;
        }
        throw new RuntimeException("Failed to converge");
    }

    static double householder(double p0, double T0, double ll, double M, double tol, double maxiter){
        //Find a zero of time of flight equation using the Householder method.
        for (int ii = 0; ii  < maxiter; ii++) {
            double y = compute_y(p0, ll);
            double fval = tof_equation(p0, y, T0, ll, M);
            double T = fval + T0;
            double fder = tof_equation_p(p0, y, T, ll);
            double fder2 = tof_equation_p2(p0, y, T, fder, ll);
            double fder3 = tof_equation_p3(p0, y, T, fder, fder2, ll);

            // Householder step (quartic)
            double p = p0 - fval * (
                    (fder * fder - fval * fder2 / 2)
                    / (fder * (fder * fder - fval * fder2) + fder3 * fval * fval / 6));

            if (Math.abs(p - p0) < tol){
                return p;
            }
            p0 = p;
        }
        throw new RuntimeException("Failed to converge");
    }


    /**
     * taken from pykep.
     */
    private static double hyp2f1b(double x, double tol) {
        if (x >= 1.0) {
            return Double.POSITIVE_INFINITY;
        }else{
            double res = 1.0;
            double term = 1.0;
            double ii = 0;
            while(true) {
                term = term * (3 + ii) * (1 + ii) / (5d / 2d + ii) * x / (ii + 1);
                double res_old = res;
                res += term;
                if (res_old == res){
                    return res;
                }
                ii += 1;
            }
        }
    }
//    /**
//     * taken from pykep.
//     */
//    private static double hyp2f1b(double z, double tol) {
//        double Sj = 1.0;
//        double Cj = 1.0;
//        double err = 1.0;
//        double j = 0;
//        double Cj1, Sj1;
//        while (err > tol) {
//            Cj1 = Cj * (3.0 + j) * (1.0 + j) / (2.5 + j) * z / (j + 1);
//            Sj1 = Sj + Cj1;
//            err = Math.abs(Cj1);
//            Sj = Sj1;
//            Cj = Cj1;
//            j = j + 1;
//        }
//        return Sj;
//    }

    public ArrayList<Vector3D[]> getVelocityVectors() {
        return velocityVectors;
    }

    //    private static double x2tof(double x, double lambda, double N) {
//        double series = 0.01;
//        double larange = 0.2;
//        double dist = Math.abs(x - 1);
//        double l2 = lambda * lambda;
//        if ((dist < larange) && (dist > series)) {
//            double a = 1.0 / (1.0 - x * x);
//            if (a > 0) {
//                double alpha = 2.0 * Math.acos(x);
//                double beta = 2.0 * Math.asin(Math.sqrt(l2/ a));
//                if (lambda < 0.0) beta = -beta;
//                return ((a * Math.sqrt(a) * ((alpha - Math.sin(alpha)) - (beta - Math.sin(beta)) + 2.0 * Math.PI * N)) / 2.0);
//            } else {
//                double alpha = 2.0 * MathUtil.acosh(x);
//                double beta = 2.0 * MathUtil.asinh(Math.sqrt(-l2/ a));
//                if (lambda < 0.0) beta = -beta;
//                return (-a * Math.sqrt(-a) * ((beta - Math.sinh(beta)) - (alpha - Math.sinh(alpha))) / 2.0);
//            }
//        }
//        double K = l2;
//        double E = x * x - 1.0;
//        double rho = Math.abs(E);
//        double z = Math.sqrt(1 + K * E);
//        if (dist < series) {
//            double eta = z - lambda * x;
//            double S1 = 0.5 * (1 - lambda - x * eta);
//            double Q = (4.0 /3.0) * F(S1, 1E-11);
//            return (eta * eta * eta * Q + 4.0 * lambda * eta) / 2.0 + N * Math.PI / Math.pow(rho,
//                    1.5);
//        } else {
//            double y = Math.sqrt(rho);
//            double g = x * z - lambda * E;
//            double d;
//            if (E < 0) {
//                double l = Math.acos(g);
//                d = N * Math.PI + l;
//            } else {
//                double f = y * (z - lambda * x);
//                d = Math.log(f + g);
//            }
//            return (x - lambda * z - d / y ) / E;
//        }
//    }








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

//    private static double[] derivatives(double T, double x, double lambda){
//        // set up shortcuts
//        double l2 = lambda * lambda;
//        double l3 = l2 * lambda;
//        double l5 = l2 * l3;
//        double y = Math.sqrt(1.0 - l2 * (1.0 - x * x));
//        double y2 = y * y;
//        double y3 = y2 * y;
//        double y5 = y2 * y3;
//        // calculate derivatives
//        double DT = (3.0 * T * x - 2.0 + 2.0 * l3 * x / y);
//        DT = DT / (1.0 - x * x);
//        double DDT = (3.0 * T +5.0 * x * DT + 2.0 * (1.0 - l2) * l3 / y3);
//        DDT = DDT / (1.0 - x * x);
//        double DDDT = (7.0 * x * DDT) + (8.0 * DT) - (6.0 * (1.0 - l2) * l5 * x / y5);
//        DDDT = DDDT / (1.0 - x * x);
//        return new double[]{DT, DDT, DDDT};
//    }
    //TODO: different compute_T_min
//    static private double halley(double T_min, double x0, double lambda, double M_max, double eps,
//                                        double iter_max) {
//        double x_old = x0;
//        double x_new = 0.0;
//        double err, DT, DDT, DDDT; double[] derivatives;
//        for (int i = 0; i < iter_max; i++){
//            derivatives = derivatives(T_min, x_old, lambda);
//            DT = derivatives[0];
//            DDT = derivatives[1];
//            DDDT = derivatives[2];
//            if(DT != 0) {
//                x_new = x_old - ((DT * DDT ) / ((DDT * DDT) - (DT * DDDT / 2.0)));
//            }
//            err = Math.abs(x_old - x_new);
//            if(err < eps) {
//                return T_min;
//            }
//            T_min = x2tof(x_new, lambda, M_max);
//            x_old = x_new;
//        }
//        throw new RuntimeException("Hayley itteration failed to converge");
//    }
}
