package xperiment;

import physics.KeplerToCartesian;
import solarsystem.Planet;
import solarsystem.SolarSystem;
import utils.vector.Vector3D;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class KeplerParser {
    private static Date startingDate;
    private static Date endingDate;

    private static boolean outputToConsole = false;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static void main(String[] args) {
        //parse commandline parameters
        for (int i  = 0; i < args.length; i++) {
            switch (args[i].toLowerCase()) {
                case "--console-out": case "-co":
                    outputToConsole = true;
                break;

                case "--starting-date": case "-sd":
                    try {
                        startingDate = DATE_FORMAT.parse(args[i+1]);
                    }
                    catch (ParseException e) {
                        System.out.println("Unable to parse starting date \"" + args[i+1] + "\"");
                    }
                break;

                case "--ending-date": case "-ed":
                    try {
                        endingDate = DATE_FORMAT.parse(args[i+1]);
                    }
                    catch (ParseException e) {
                        System.out.println("Unable to parse starting date \"" + args[i+1] + "\"");
                    }
                break;

                case "--help": case "-h":
                    System.out.println("Kepler Timestamp JSON Generator\n");
                    System.out.println("\t--help, -h, default: Displays the current help screen.");
                    System.out.println("\t--starting-date <date>, -sd <date>: Sets the starting date, expects date in dd/MM/yyyy format.");
                    System.out.println("\t--ending-date <date>, -ed <date>: Sets the ending date, expects date in dd/MM/yyyy format.");
            }
        }

        if (!startingDate.equals(null)) {//Starting date is a prerequisite
            //I decided not to make ending date a prerequisite but an option
            if (endingDate.equals(null)) endingDate = new Date();

            //Header
            if (outputToConsole) {
                SolarSystem solarSystem = null;
                try {
                    solarSystem = new SolarSystem();
                } catch (IOException e) {
                    System.out.println("Unable to read planetary JSON data!");
                }

                if (!solarSystem.equals(null)) {
                    Planet earth = solarSystem.getPlanets().getEarth();
                    boolean firstMem = true;

                    System.out.println("{\n\t\"dates\": [");
                    while (startingDate.before(endingDate)) {
                        Vector3D[] cartesian = KeplerToCartesian.getCartesianCoordinates(earth, new utils.Date(startingDate));

                        if (firstMem) {
                            System.out.printf("\n\t\t{\n");
                            firstMem = false;
                        }
                        else System.out.printf(",\n\t\t{\n");

                        System.out.println("\t\t\t\"date\": \"" + startingDate.toString() + "\",");
                        System.out.println("\t\t\t\"orbital_pos\": " + Vector3DtoJSONObj(cartesian[0]) + ",");
                        System.out.println("\t\t\t\"orbital_vel\": " + Vector3DtoJSONObj(cartesian[1]) + ",");
                        System.out.println("\t\t\t\"central_pos\": " + Vector3DtoJSONObj(cartesian[2]) + ",");
                        System.out.println("\t\t\t\"central_vel\": " + Vector3DtoJSONObj(cartesian[3]));

                        System.out.printf("\t\t}");
                        //Update starting date below
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(startingDate);
                        calendar.add(Calendar.DATE, 1);
                        startingDate = calendar.getTime();
                    }
                    System.out.println("\t]\n}");
                }
            }
        }
        else System.out.println("Unable to generate JSON! Imput a starting date.");
    }

    public static String Vector3DtoJSONObj(Vector3D vec) {
        return "{\"x\": " + vec.getX() + ", \"y\": " + vec.getY() + ", \"z\": " + vec.getZ() + "}";
    }
}
