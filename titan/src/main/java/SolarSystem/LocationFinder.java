package SolarSystem;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 *
 *
 */
public class LocationFinder {
    private static final double AU = 149597870700.0; // AU in m

    @JsonProperty("a")
    private double distance;             // Mean distance (AU)
    @JsonProperty("a_Cnt")
    private double distanceCnt;          // Change distance per century (AU)
    @JsonProperty("e")
    private double eccentricity;         // Eccentricity of orbit
    @JsonProperty("e_Cnt")
    private double eccentricityCnt;      // Change of eccentricity of orbit per century
    @JsonProperty("I")
    private double inclination;          // Inclination of orbit
    @JsonProperty("I_Cnt")
    private double inclinationCnt;       // Change of inclination of orbit per century
    @JsonProperty("L")
    private double meanLongitude;        // Mean longitude
    @JsonProperty("L_Cnt")
    private double meanLongitudeCnt;     // Change of longitude per century
    @JsonProperty("w")
    private double perihelion;           // Longitude of perihelion
    @JsonProperty("w_Cnt")
    private double perihelionCnt;        // Change of perihelion longitude per century
    @JsonProperty("node")
    private double ascendingNode;        // Longitude of ascending node
    @JsonProperty("node_Cnt")
    private double ascendingNodeCnt;     // Change of longitude of ascending node per century

    private double[] r = new double[3];
    private double[] relc = new double[3];
    private double[] eq = new double[3];

    public void initializeLocation(Date date){



    }



    @Override
    public String toString() {
        return "LocationFinder{" +
                "distance=" + distance +
                ", distanceCnt=" + distanceCnt +
                ", eccentricity=" + eccentricity +
                ", eccentricityCnt=" + eccentricityCnt +
                ", inclination=" + inclination +
                ", inclinationCnt=" + inclinationCnt +
                ", meanLongitude=" + meanLongitude +
                ", meanLongitudeCnt=" + meanLongitudeCnt +
                ", perihelion=" + perihelion +
                ", perihelionCnt=" + perihelionCnt +
                ", ascendingNode=" + ascendingNode +
                ", ascendingNodeCnt=" + ascendingNodeCnt +
                '}';
    }
}
