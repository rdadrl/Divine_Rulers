package xperiment;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Horizons2JSON {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat DATE_TO_READ_FORMAT = new SimpleDateFormat("yyyy-MMM-dd", Locale.US);

    public static void main (String args[]) throws IOException, ParseException {
        File horizonsData = null;

        if (args.length == 1) {
            try {
                horizonsData = new File(args[0]);
            }
            catch (Exception e) {
                System.out.println("Please input a valid path for the Horizons Text Output!");
            }

            if (!horizonsData.equals(null)) {
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(horizonsData));
                } catch (FileNotFoundException e) {
                    System.out.println("Unable to read file contents.");
                }
                if (reader != null) {
                    String line = reader.readLine();
                    boolean reading = false;
                    boolean firstMem = true;

                    System.out.println("{\n\t\"dates\": [");
                    while (line != null) {
                        if (line.equals("$$EOE")) reading = false;

                        if (reading) {
                            //2451544.500000000 = A.D. 2000-Jan-01 00:00:00.0000 TDB
                            String date = line.split("=")[1].trim().split(" ")[1].trim();
                            date = DATE_FORMAT.format(DATE_TO_READ_FORMAT.parse(date));
                            String central_pos = reader.readLine();
                            String central_vel = reader.readLine();
                            //Skip last line
                            line = reader.readLine();

                            if (firstMem) {
                                System.out.printf("\n\t\t{\n");
                                firstMem = false;
                            }
                            else System.out.printf(",\n\t\t{\n");

                            /*System.out.println("\t\t\t\"date\": \"" + date + "\",");
                            System.out.println("\t\t\t\"central_pos\": " + central_pos + ",");
                            System.out.println("\t\t\t\"central_vel\": " + central_vel);
*/
                            String[] central_pos_arr = central_pos.split("=");
                            String cp_x = central_pos_arr[1].split("Y")[0].trim();
                            String cp_y = central_pos_arr[2].split("Z")[0].trim();
                            String cp_z = central_pos_arr[3].trim();

                            String[] central_vel_arr = central_vel.split("=");
                            String cv_x = central_vel_arr[1].split("VY")[0].trim();
                            String cv_y = central_vel_arr[2].split("VZ")[0].trim();
                            String cv_z = central_vel_arr[3].trim();

                            System.out.println("\t\t\t\"date\": \"" + date + "\",");
                            System.out.println("\t\t\t\"central_pos\": {\"x\": " + cp_x + ", \"y\": " + cp_y + ", \"z\": " + cp_z + "},");
                            System.out.println("\t\t\t\"central_vel\": {\"x\": " + cv_x + ", \"y\": " + cv_y + ", \"z\": " + cv_z + "}");

                            System.out.printf("\t\t}");

                            //System.out.println("Date: " + date + ", centralPos: " + central_pos + ", central_vel: " + central_vel);
                        }
                        if (line.equals("$$SOE")) reading = true;
                        line = reader.readLine();
                    }
                    System.out.println("\n\t]\n}");
                    reader.close();
                }
            }
            else System.out.println("Unable to open file.");
        }
        else System.out.println("Please input file of the Horizons Text Output as a parameter,\n\tHorizons2Json <filepath>");
    }
}
