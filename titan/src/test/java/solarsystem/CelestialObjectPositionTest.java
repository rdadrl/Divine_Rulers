package solarsystem;

import org.junit.Test;
import utils.Coordinate;
import utils.Date;

import java.io.IOException;
import java.util.Calendar;

import static org.junit.Assert.*;

/**
 *
 *
 */
public class CelestialObjectPositionTest {
    private static boolean PRINT = true;

    @Test
    public void Earth200020002() throws IOException {
        Date date = new Date(2000, 0, 1, 0, 0, 0);

        /*
        SolarSystem solarSystem = new SolarSystem();
        CelestialObjects saturn = solarSystem.getPlanets().getSaturn();
        Coordinate coordinate = saturn.getHEEpos(date);
        System.out.println("\nSaturn");
        System.out.println("X: " + coordinate.getX());
        System.out.println("Y: " + coordinate.getY());
        System.out.println("Z: " + coordinate.getZ());


        CelestialObjects venus = solarSystem.getPlanets().getVenus();
        coordinate = venus.getHEEpos(date);
        System.out.println("\nVenus");
        System.out.println("X: " + coordinate.getX());
        System.out.println("Y: " + coordinate.getY());
        System.out.println("Z: " + coordinate.getZ());
        */
        SolarSystem solarSystem = new SolarSystem();
        CelestialObjects earth = solarSystem.getPlanets().getEarth();
        Coordinate coordinate = earth.getHEEpos(new Date(2000, 0, 1, 0, 0,
                0));
        if(PRINT){
            printXYZ(earth.getName(), coordinate, date);
        }
        assertEquals(coordinate.getX(), -0.16856286531223225, 1e-4);
        assertEquals(coordinate.getY(), 0.968758969709345, 1e-4);
        assertEquals(coordinate.getZ(), -2.558653782299318E-7, 1e-4);


        coordinate = earth.getHEEpos(new Date(2001, 0, 1, 0, 0, 0));
        if(PRINT){
            printXYZ(earth.getName(), coordinate, date);
        }
        assertEquals(coordinate.getX(), 0.19012537726450277, 1e-4);
        assertEquals(coordinate.getY(), -0.9987660639452812, 1e-4);
        assertEquals(coordinate.getZ(), 2.5252586100770815E-6, 1e-4);

        coordinate = earth.getHEEpos(new Date(2002, 0, 2, 0, 0, 0));
        if(PRINT){
            printXYZ(earth.getName(), coordinate, date);
        }
        assertEquals(coordinate.getX(), 0.202471824312362, 1e-4);
        assertEquals(coordinate.getY(), -0.9963429946683185, 1e-4);
        assertEquals(coordinate.getZ(), 4.775113569186536E-6, 1e-4);


    }

    @Test
    public void Saturn200020002() throws IOException {
        Date date = new Date(2000, 1, 1, 0, 0, 0);

        /*
        SolarSystem solarSystem = new SolarSystem();
        CelestialObjects saturn = solarSystem.getPlanets().getSaturn();
        Coordinate HEEPos = saturn.getHEEpos(date);
        System.out.println("\nSaturn");
        System.out.println("X: " + HEEPos.getX());
        System.out.println("Y: " + HEEPos.getY());
        System.out.println("Z: " + HEEPos.getZ());


        CelestialObjects venus = solarSystem.getPlanets().getVenus();
        HEEPos = venus.getHEEpos(date);
        System.out.println("\nVenus");
        System.out.println("X: " + HEEPos.getX());
        System.out.println("Y: " + HEEPos.getY());
        System.out.println("Z: " + HEEPos.getZ());
        */
        SolarSystem solarSystem = new SolarSystem();
        CelestialObjects saturn = solarSystem.getPlanets().getSaturn();

        Coordinate HEEPos = saturn.getHEEpos(date);
        Coordinate HEEVel = saturn.getHEEvel(date);
        if(PRINT){
            printXYZ(saturn.getName(), HEEPos, HEEVel, date);
        }
        //assertEquals(HEEPos.getX(), -0.16856286531223225, 1e-4);
        //assertEquals(HEEPos.getY(), 0.968758969709345, 1e-4);
        //assertEquals(HEEPos.getZ(), -2.558653782299318E-7, 1e-4);

        date.add(Calendar.YEAR, 1);
        HEEPos = saturn.getHEEpos(date);
        HEEVel = saturn.getHEEvel(date);
        if(PRINT){
            printXYZ(saturn.getName(), HEEPos, HEEVel, date);
        }
        //assertEquals(HEEPos.getX(), 0.19012537726450277, 1e-4);
        //assertEquals(HEEPos.getY(), -0.9987660639452812, 1e-4);
        //assertEquals(HEEPos.getZ(), 2.5252586100770815E-6, 1e-4);

        date.add(Calendar.YEAR, 1);
        HEEPos = saturn.getHEEpos(date);
        HEEVel = saturn.getHEEvel(date);
        if(PRINT){
            printXYZ(saturn.getName(), HEEPos, HEEVel, date);
        }
        //assertEquals(HEEPos.getX(), 0.202471824312362, 1e-4);
        //assertEquals(HEEPos.getY(), -0.9963429946683185, 1e-4);
        //assertEquals(HEEPos.getZ(), 4.775113569186536E-6, 1e-4);

        date.add(Calendar.YEAR, 5);
        HEEPos = saturn.getHEEpos(date);
        HEEVel = saturn.getHEEvel(date);
        if(PRINT){
            printXYZ(saturn.getName(), HEEPos, HEEVel, date);
        }
        //assertEquals(HEEPos.getX(), 0.202471824312362, 1e-4);
        //assertEquals(HEEPos.getY(), -0.9963429946683185, 1e-4);
        //assertEquals(HEEPos.getZ(), 4.775113569186536E-6, 1e-4);

        date.add(Calendar.YEAR, 5);
        HEEPos = saturn.getHEEpos(date);
        HEEVel = saturn.getHEEvel(date);
        if(PRINT){
            printXYZ(saturn.getName(), HEEPos, HEEVel, date);
        }
        //assertEquals(HEEPos.getX(), 0.202471824312362, 1e-4);
        //assertEquals(HEEPos.getY(), -0.9963429946683185, 1e-4);
        //assertEquals(HEEPos.getZ(), 4.775113569186536E-6, 1e-4);
    }


    @Test
    public void Saturn1875_2050() throws IOException {
        Date date = new Date(1875, 0, 1, 0, 0, 0);
        SolarSystem solarSystem = new SolarSystem();
        CelestialObjects saturn = solarSystem.getPlanets().getSaturn();
        for(int i = 1875; i<2051; i++) {
            date.add(Calendar.YEAR, 1);
            Coordinate HEEPos = saturn.getHEEpos(date);
        }

    }

    private void printXYZ(String name, Coordinate posCoord, Date date){
        System.out.println("\n" + name);
        System.out.println(date);
        System.out.println("\tPosition");
        System.out.println("\t\tX: " + posCoord.getX());
        System.out.println("\t\tY: " + posCoord.getY());
        System.out.println("\t\tZ: " + posCoord.getZ());
    }

    private void printXYZ(String name, Coordinate posCoord,
                          Coordinate velCoord, Date date){
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