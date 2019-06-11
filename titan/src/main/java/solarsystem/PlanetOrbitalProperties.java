package solarsystem;

import com.fasterxml.jackson.annotation.JsonProperty;
import utils.Date;
import utils.MathUtil;

/**
 * A class which will be a place holder for the keplerian orbital properties of a planet
 */
public class PlanetOrbitalProperties {
    @JsonProperty("planet")
    private Planet planet;      // Planet of which the orbital properties belong to.
    @JsonProperty("a_J2000")
    private double a_J2000;     // Mean semi-major axis (AU) at J2000
    @JsonProperty("a_Cnt")
    private double a_Cnt;       // Change semi-major per century (AU)
    @JsonProperty("e_J2000")
    private double e_J2000;     // Eccentricity of orbit at J2000
    @JsonProperty("e_Cnt")
    private double e_Cnt;       // Change of eccentricity of orbit per century
    @JsonProperty("i_J2000")
    private double i_J2000;     // Inclination of orbit at J2000
    @JsonProperty("i_Cnt")
    private double i_Cnt;       // Change of departureInclination of orbit per century
    @JsonProperty("l_J2000")
    private double l_J2000;     // Mean longitude at J2000
    @JsonProperty("l_Cnt")
    private double l_Cnt;       // Change of longitude per century
    @JsonProperty("w_J2000")
    private double w_J2000;     // Longitude of perihelion (degrees) at J2000
    @JsonProperty("w_Cnt")
    private double w_Cnt;       // Change of perihelion l per century
    @JsonProperty("node_J2000")
    private double o_J2000;     // Longitude of ascending node at J2000
    @JsonProperty("node_Cnt")
    private double o_Cnt;       // Change of longitude of ascending node per

    private double JT = utils.Date.J2000; //initialize to J2000

    private double a = a_J2000;           // semi major axis;
    private double e = e_J2000;           // eccentricity
    private double i = i_J2000;           // departureInclination
    private double l = l_J2000;           // longitude
    private double w = w_J2000;           // periphelon
    private double o = o_J2000;           // change of ascending node
    private double w_a = w_J2000 - o_J2000; // argument of the periphelion

    /**
     * Initialize the orbital properties for a specific old_date
     * @param date old_date
     */
    public void setOrbitalProperties(Date date){
        if(JT == date.dateToJulian()){
            return;
        }

        JT = date.dateToJulian(); // in centuries

        // centuries since julian time
        double T = (JT - utils.Date.J2000)/36525.0;

        // Calculate orbital properties at a specific old_date.
        a = (a_J2000 + a_Cnt * T) * MathUtil.AU; // convert to meters
        e = e_J2000 + e_Cnt * T;
        i = i_J2000 + i_Cnt * T;
        l = l_J2000 + l_Cnt * T;
        w = w_J2000 + w_Cnt * T;
        o = o_J2000 + o_Cnt * T;

        w_a = w - o;
    }

    /**
     * @param date old_date
     * @return array of orbital properties. In the order of semi-major axis (a), eccentricity (e)
     * , departureInclination (i), longitude (l), periapsis (w), ascending node (o), and gravetational
     * parementer (mu)
     */
    public double[] getOrbitalProperties(Date date){
        setOrbitalProperties(date);
        return new double[]{a, e, i, l, w, o, w_a};
    }

    /**
     * @param date old_date
     * @return semi-major axis on old_date
     */
    public double getSemiMajorAxis(Date date) {
        setOrbitalProperties(date);
        return a;
    }

    /**
     * @return semi major axis for J2000
     */
    public double getSemiMajorAxisJ2000(){
        return a_J2000;
    }

    /**
     * @param date old_date
     * @return eccentricity on old_date
     */
    public double getEccentricity(Date date) {
        setOrbitalProperties(date);
        return e;
    }

    /**
     * @param date old_date
     * @return departureInclination on old_date
     */
    public double getInclination(Date date) {
        setOrbitalProperties(date);
        return i;
    }

    /**
     * @param date old_date
     * @return longitude on old_date
     */
    public double getLongitude(Date date) {
        setOrbitalProperties(date);
        return l;
    }

    /**
     * @param date old_date
     * @return periphelon on old_date
     */
    public double getPeriphelon(Date date) {
        setOrbitalProperties(date);
        return w;
    }

    /**
     * @param date old_date
     * @return argument of the periphelon at the given old_date
     */
    public double getPeriphelonArgument(Date date){
        setOrbitalProperties(date);
        return w_a;
    }

    /**
     * @param date old_date
     * @return acending node on old_date
     */
    public double getAscendingNode(Date date) {
        setOrbitalProperties(date);
        return o;
    }
}
