package solarsystem;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * A class which is a placeholder for all the planets in the universe. That way the solarsystem
 * class doesn't become clogged.  The objects are created by a json file to keep it organized
 */
public class Planets {
    @JsonProperty("Sun")
    private Planet sun;
    @JsonProperty("Mercury")
    private Planet mercury;
    @JsonProperty("Venus")
    private Planet venus;
    @JsonProperty("Earth")
    private Planet earth;
    @JsonProperty("Mars")
    private Planet mars;
    @JsonProperty("Jupiter")
    private Planet jupiter;
    @JsonProperty("Saturn")
    private Planet saturn;
    @JsonProperty("Titan")
    private Planet titan;
    @JsonProperty("Uranus")
    private Planet uranus;
    @JsonProperty("Neptune")
    private Planet neptune;
    @JsonProperty("All")
    private ArrayList<Planet> all;

    /**
     * @return sun object
     */
    public Planet getSun() {
        return sun;
    }

    /**
     * @return mercury object
     */
    public Planet getMercury() {
        return mercury;
    }

    /**
     * @return venus object
     */
    public Planet getVenus() {
        return venus;
    }

    /**
     * @return earth object
     */
    public Planet getEarth() {
        return earth;
    }

    /**
     * @return mars object
     */
    public Planet getMars() {
        return mars;
    }

    /**
     * @return jupiter object
     */
    public Planet getJupiter() {
        return jupiter;
    }

    /**
     * @return saturn object
     */
    public Planet getSaturn() {
        return saturn;
    }

    /**
     * @return uranus object
     */
    public Planet getUranus() {
        return uranus;
    }

    /**
     * @return neptune object
     */
    public Planet getNeptune() {
        return neptune;
    }

    /**
     * @return titan object
     */
    public Planet getTitan() {
        return titan;
    }

    /**
     * @return all the planets
     */
    public ArrayList<Planet> getAll(){
        return all;
    }
}
