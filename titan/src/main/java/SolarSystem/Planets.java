package SolarSystem;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class including the planets of the solar system. The objects are created by
 * a json file to keep it organized
 */
public class Planets {
    @JsonProperty("Sun")
    private CelestialObjects sun;
    @JsonProperty("Mercury")
    private CelestialObjects mercury;
    @JsonProperty("Venus")
    private CelestialObjects venus;
    @JsonProperty("Earth")
    private CelestialObjects earth;
    @JsonProperty("Mars")
    private CelestialObjects mars;
    @JsonProperty("Jupiter")
    private CelestialObjects jupiter;
    @JsonProperty("Saturn")
    private CelestialObjects saturn;
    @JsonProperty("Uranus")
    private CelestialObjects uranus;
    @JsonProperty("Neptune")
    private CelestialObjects neptune;

    public CelestialObjects getSun() {
        return sun;
    }

    public CelestialObjects getMercury() {
        return mercury;
    }

    public CelestialObjects getVenus() {
        return venus;
    }

    public CelestialObjects getEarth() {
        return earth;
    }

    public CelestialObjects getMars() {
        return mars;
    }

    public CelestialObjects getJupiter() {
        return jupiter;
    }

    public CelestialObjects getSaturn() {
        return saturn;
    }

    public CelestialObjects getUranus() {
        return uranus;
    }

    public CelestialObjects getNeptune() {
        return neptune;
    }
}
