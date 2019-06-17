package solarsystem;

import org.junit.Test;
import utils.MathUtil;
import utils.vector.Vector3D;
import utils.Date;

import java.io.IOException;
import java.util.Calendar;

import static org.junit.Assert.*;

/**
 *
 *
 */
public class KeplerToCartesianTest {
    private static boolean PRINT = true;
    private final double AU = MathUtil.AU;

    @Test
    public void Earth200020002() throws IOException {
        Date date = new Date(2000, 0, 1, 0, 0, 0);

        /*
        SolarSystem solarSystem = new SolarSystem();
        Planet saturn = solarSystem.getPlanets().getSaturn();
        Vector3D coordinate = saturn.getCentralPos(old_date);
        System.out.println("\nSaturn");
        System.out.println("X: " + coordinate.getX());
        System.out.println("Y: " + coordinate.getY());
        System.out.println("Z: " + coordinate.getZ());


        Planet venus = solarSystem.getPlanets().getVenus();
        coordinate = venus.getCentralPos(old_date);
        System.out.println("\nVenus");
        System.out.println("X: " + coordinate.getX());
        System.out.println("Y: " + coordinate.getY());
        System.out.println("Z: " + coordinate.getZ());
        */
        SolarSystem solarSystem = new SolarSystem();
        Planet earth = solarSystem.getPlanets().getEarth();
        Vector3D coordinate = earth.getCentralPosAtDate(new Date(2000, 0, 1, 0, 0,
                0));
        if(PRINT){
            printPosVel(earth.getName(), coordinate, date);
        }
        assertEquals( -0.16856286531223225,coordinate.getX()/AU, 1e-4);
        assertEquals(0.968758969709345,coordinate.getY()/AU, 1e-4);
        assertEquals(-2.558653782299318E-7, coordinate.getZ()/AU, 1e-4);


        coordinate = earth.getCentralPosAtDate(new Date(2001, 0, 1, 0, 0, 0));
        if(PRINT){
            printPosVel(earth.getName(), coordinate, date);
        }
        assertEquals(-0.18135913869197764, coordinate.getX()/AU,1e-4);
        assertEquals(0.9664354985130857, coordinate.getY()/AU,1e-4);
        assertEquals(-2.5252586100770815E-6, coordinate.getZ()/AU,1e-4);

        coordinate = earth.getCentralPosAtDate(new Date(2002, 0, 2, 0, 0, 0));
        if(PRINT){
            printPosVel(earth.getName(), coordinate, date);
        }
        assertEquals(-0.19412419171553183, coordinate.getX()/AU,1e-4);
        assertEquals( 0.9639457176122871, coordinate.getY()/AU,1e-4);
        assertEquals(4.775113569186536E-6, coordinate.getZ()/AU,1e-4);
    }

    @Test
    public void Saturn200020002() throws IOException {
        Date date = new Date(2000, 0, 1, 0, 0, 0);

        /*
        SolarSystem solarSystem = new SolarSystem();
        Planet saturn = solarSystem.getPlanets().getSaturn();
        Vector3D HEEPos = saturn.getCentralPos(old_date);
        System.out.println("\nSaturn");
        System.out.println("X: " + HEEPos.getX());
        System.out.println("Y: " + HEEPos.getY());
        System.out.println("Z: " + HEEPos.getZ());


        Planet venus = solarSystem.getPlanets().getVenus();
        HEEPos = venus.getCentralPos(old_date);
        System.out.println("\nVenus");
        System.out.println("X: " + HEEPos.getX());
        System.out.println("Y: " + HEEPos.getY());
        System.out.println("Z: " + HEEPos.getZ());
        */
        SolarSystem solarSystem = new SolarSystem();
        Planet saturn = solarSystem.getPlanets().getSaturn();

        Vector3D HEEPos = saturn.getCentralPosAtDate(date);
        Vector3D HEEVel = saturn.getCentralVelAtDate(date);
        if(PRINT){
            printPosVel(saturn.getName(), HEEPos, HEEVel, date);
        }
        //assertEquals(HEEPos.getX(), -0.16856286531223225, 1e-4);
        //assertEquals(HEEPos.getY(), 0.968758969709345, 1e-4);
        //assertEquals(HEEPos.getZ(), -2.558653782299318E-7, 1e-4);

        date.add(Calendar.YEAR, 1);
        HEEPos = saturn.getCentralPosAtDate(date);
        HEEVel = saturn.getCentralVelAtDate(date);
        if(PRINT){
            printPosVel(saturn.getName(), HEEPos, HEEVel, date);
        }
        //assertEquals(HEEPos.getX(), 0.19012537726450277, 1e-4);
        //assertEquals(HEEPos.getY(), -0.9987660639452812, 1e-4);
        //assertEquals(HEEPos.getZ(), 2.5252586100770815E-6, 1e-4);

        date.add(Calendar.YEAR, 1);
        HEEPos = saturn.getCentralPosAtDate(date);
        HEEVel = saturn.getCentralVelAtDate(date);
        if(PRINT){
            printPosVel(saturn.getName(), HEEPos, HEEVel, date);
        }
        //assertEquals(HEEPos.getX(), 0.202471824312362, 1e-4);
        //assertEquals(HEEPos.getY(), -0.9963429946683185, 1e-4);
        //assertEquals(HEEPos.getZ(), 4.775113569186536E-6, 1e-4);

        date.add(Calendar.YEAR, 5);
        HEEPos = saturn.getCentralPosAtDate(date);
        HEEVel = saturn.getCentralVelAtDate(date);
        if(PRINT){
            printPosVel(saturn.getName(), HEEPos, HEEVel, date);
        }
        //assertEquals(HEEPos.getX(), 0.202471824312362, 1e-4);
        //assertEquals(HEEPos.getY(), -0.9963429946683185, 1e-4);
        //assertEquals(HEEPos.getZ(), 4.775113569186536E-6, 1e-4);

        date.add(Calendar.YEAR, 5);
        HEEPos = saturn.getCentralPosAtDate(date);
        HEEVel = saturn.getCentralVelAtDate(date);
        if(PRINT){
            printPosVel(saturn.getName(), HEEPos, HEEVel, date);
        }
        //assertEquals(HEEPos.getX(), 0.202471824312362, 1e-4);
        //assertEquals(HEEPos.getY(), -0.9963429946683185, 1e-4);
        //assertEquals(HEEPos.getZ(), 4.775113569186536E-6, 1e-4);
    }


    @Test
    public void Saturn1875_2050() throws IOException {
        Date date = new Date(2000, 0, 1, 0, 0, 0);
        System.out.println(date);
        SolarSystem solarSystem = new SolarSystem();
        Planet planet = solarSystem.getPlanets().getEarth();
        for(int i = 1; i<=12; i++) {
            Vector3D HEEPos = planet.getCentralPosAtDate(date);
            Vector3D OrbPos = planet.getOrbitalPosAtDate(date);
            if(PRINT){
                //printXYZ(planet.getName(), OrbPos, old_date);
                printXYZ(planet.getName(), HEEPos, date);
                //System.out.println();
            }
            date.add(Calendar.MONTH, 1);
        }

    }

    @Test
    public void Jupiter() throws IOException{
        Date date = new Date(2000, 0, 0, 0, 0, 0);
        SolarSystem solarSystem = new SolarSystem();
        Planet planet = solarSystem.getPlanets().getJupiter();
        Vector3D HEEPos = planet.getCentralPosAtDate(date);
        Vector3D OrbPos = planet.getOrbitalPosAtDate(date);
        if(PRINT){
            //printXYZ(planet.getName(), OrbPos, old_date);
            printXYZ(planet.getName(), HEEPos, date);
            //System.out.println();
        }
    }

    @Test
    public void Titan() throws IOException{
        Date date = new Date(2000, 0, 1, 12, 0, 0);
        System.out.println(date);
        SolarSystem solarSystem = new SolarSystem();
        for(Planet planet:solarSystem.getPlanets().getAll()){
            printXYZ(planet.getName(), planet.getCentralPosAtDate(date), date);
            printXYZ(planet.getName(), planet.getCentralVelAtDate(date), date);
            System.out.println();
        }
    }

    public void printXYZ(String name, Vector3D posCoord, Date date){
        System.out.print(name + ": ");
        System.out.print(date);
        System.out.print("\t\tX: " + posCoord.getX());
        System.out.print("\t\tY: " + posCoord.getY());
        System.out.print("\t\tZ: " + posCoord.getZ() + "\n");
    }

    private void printPosVel(String name, Vector3D posCoord, Date date){
        System.out.println("\n" + name);
        System.out.println(date);
        System.out.println("\tPosition");
        System.out.println("\t\tX: " + posCoord.getX());
        System.out.println("\t\tY: " + posCoord.getY());
        System.out.println("\t\tZ: " + posCoord.getZ());
    }

    private void printPosVel(String name, Vector3D posCoord,
                             Vector3D velCoord, Date date){
        System.out.println("\n" + name);
        System.out.println(date);
        System.out.println("\tPosition");
        System.out.println("\t\tX: " + posCoord.getX());
        System.out.println("\t\tY: " + posCoord.getY());
        System.out.println("\t\tZ: " + posCoord.getZ());
        System.out.println("\n\tVelocity");
        System.out.println("\t\tX: " + velCoord.getX());
        System.out.println("\t\tY: " + velCoord.getY());
        System.out.println("\t\tZ: " + velCoord.getZ());
    }
}