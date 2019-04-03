package solarsystem;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Class including the planets of the solar system. This class is a placeholder object to keep
 * the planets of the solarsystem organized an to decluter the solarsystem object
 * The objects are created by a json file to keep it organized
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

    public Planet getSun() {
        return sun;
    }

    public Planet getMercury() {
        return mercury;
    }

    public Planet getVenus() {
        return venus;
    }

    public Planet getEarth() {
        return earth;
    }

    public Planet getMars() {
        return mars;
    }

    public Planet getJupiter() {
        return jupiter;
    }

    public Planet getSaturn() {
        return saturn;
    }

    public Planet getUranus() {
        return uranus;
    }

    public Planet getNeptune() {
        return neptune;
    }

    public Planet getTitan() {
        return titan;
    }

    public ArrayList<Planet> getAll(){
        return all;
    }
}
