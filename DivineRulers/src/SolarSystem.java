import java.awt.Color;
import java.util.List;

public class SolarSystem {
    private CelestialObjects sun;
    private CelestialObjects mercury;
    private CelestialObjects venus;
    private CelestialObjects earth;
    private CelestialObjects mars;
    private CelestialObjects jupiter;
    private CelestialObjects saturn;
    private CelestialObjects uranus;
    private CelestialObjects neptune;
    CelestialObjects[] objects;


    public SolarSystem() {
        createSolarSystem();
    }
    
    public CelestialObjects[] getObjects() {
    	return objects;
    }
        

    public void createSolarSystem(){
        sun = new CelestialObjects("Sun", 1.989e30, 695.8e3);
        sun.setColor(Color.yellow);
        sun.setPosition(0, 0, 0);

        mercury = new CelestialObjects("Mercury", 0.33011e24, 2439.7);
        mercury.setOrbitingParent(sun);
        mercury.setEccentricity(0.2056);
        mercury.setPeriod(87.969);
        mercury.setSemiMajor(0.38709893);
        mercury.setDistance(0.466);

        venus = new CelestialObjects("Venus", 4.8675e24, 6051.8);
        venus.setOrbitingParent(sun);
        venus.setEccentricity(0.0067);
        venus.setPeriod(224.701);
        venus.setSemiMajor(0.72333199);
        venus.setDistance(0.720);

        earth = new CelestialObjects("Earth", 5.9723e24, 6371.0);
        earth.setOrbitingParent(sun);
        earth.setEccentricity(0.0167);
        earth.setPeriod(365.256);
        earth.setSemiMajor(1.00000011);
        earth.setDistance(0.983);

        mars = new CelestialObjects("Mars", 0.64171e24, 3389.5);
        mars.setOrbitingParent(sun);
        mars.setEccentricity(0.0935);
        mars.setPeriod(686.980);
        mars.setSemiMajor(1.52366231);
        mars.setDistance(1.391);

        jupiter = new CelestialObjects("Jupiter", 1898.19e24, 69911.0);
        jupiter.setOrbitingParent(sun);
        jupiter.setEccentricity(0.0489);
        jupiter.setPeriod(4332.589);
        jupiter.setSemiMajor(5.20336301);
        jupiter.setDistance(4.967);

        saturn = new CelestialObjects("Saturn", 568.34e24, 58232.0);
        saturn.setOrbitingParent(sun);
        saturn.setEccentricity(0.0565);
        saturn.setPeriod(10759.22);
        saturn.setSemiMajor(9.53707032);
        saturn.setDistance(9.139);
        
        saturn.setPosition(351254886446.44037, -1345585527058.3767, -570922577597.4592);

        uranus = new CelestialObjects("Uranus", 86.813e24, 25362.0);
        uranus.setOrbitingParent(sun);
        uranus.setEccentricity(0.0457);
        uranus.setPeriod(30685.4);
        uranus.setSemiMajor(19.19126393);
        uranus.setDistance(19.907);

        neptune = new CelestialObjects("Neptune", 102.413e24, 24622.0);
        neptune.setOrbitingParent(sun);
        neptune.setEccentricity(0.0113);
        neptune.setPeriod(60189);
        neptune.setSemiMajor(30.06896348);
        neptune.setDistance(30.087);
        
        objects = new CelestialObjects[]{sun, mercury, venus, earth, mars, jupiter, saturn, uranus, neptune};
    }

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
