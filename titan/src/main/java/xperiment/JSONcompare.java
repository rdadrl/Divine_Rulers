package xperiment;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class JSONcompare {
    public static final Vector ZERO_VEC = new Vector(0,0,0);

    public static void main(String[] args) throws IOException {
        ArrayList<VectorAnalysis> analysis = new ArrayList<>();
        VectorAnalysis bestFittingPosition = new VectorAnalysis();
        VectorAnalysis bestFittingVelocity = new VectorAnalysis();
        VectorAnalysis bestFittingOverall = new VectorAnalysis();

        JSONObject horizons;
        JSONObject keplers;

        String hs = new String(Files.readAllBytes(Paths.get("src/main/resources/temp_titan/horizons_output.json")), StandardCharsets.UTF_8);
        String ks = new String(Files.readAllBytes(Paths.get("src/main/resources/temp_titan/last_output.json")), StandardCharsets.UTF_8);

        horizons = new JSONObject(hs);
        keplers = new JSONObject(ks);

        JSONArray horizonList = horizons.getJSONArray("dates");
        JSONArray keplerList = keplers.getJSONArray("dates");

        int iMax = Math.min(horizonList.length(), keplerList.length());

        for (int i = 0; i < iMax; i++) {
            JSONObject horizonResults = (JSONObject) horizonList.get(i);
            JSONObject keplersResults = (JSONObject) keplerList.get(i);

            if (horizonResults.get("date").equals(keplersResults.get("date"))) {
                System.out.println("Comparing day " + analysis.size() + 1);
                VectorAnalysis va = new VectorAnalysis();
                va.date = (String) horizonResults.get("date");

                JSONObject horizonPositionObj = (JSONObject) horizonResults.get("central_pos");
                JSONObject horizonVelocityObj = (JSONObject) horizonResults.get("central_vel");
                JSONObject keplerPositionObj = (JSONObject) keplersResults.get("central_pos");
                JSONObject keplerVelocityObj = (JSONObject) keplersResults.get("central_vel");

                Vector horizonPosition = new Vector(
                        (double) horizonPositionObj.get("x") * 1000,
                        (double) horizonPositionObj.get("y") * 1000,
                        (double) horizonPositionObj.get("z") * 1000
                );
                Vector horizonVelocity = new Vector(
                        (double) horizonVelocityObj.get("x") * 1000,
                        (double) horizonVelocityObj.get("y") * 1000,
                        (double) horizonVelocityObj.get("z") * 1000
                );
                Vector keplerPosition = new Vector(
                        (double) keplerPositionObj.get("x"),
                        (double) keplerPositionObj.get("y"),
                        (double) keplerPositionObj.get("z")
                );
                Vector keplerVelocity = new Vector(
                        (double) keplerVelocityObj.get("x"),
                        (double) keplerVelocityObj.get("y"),
                        (double) keplerVelocityObj.get("z")
                );
                va.horizonPosition = horizonPosition;
                va.horizonVelocity = horizonVelocity;
                va.keplerPosition = keplerPosition;
                va.keplerVelocity = keplerVelocity;

                va.positionDifferance = calculateRelativeErrorAverage(keplerPosition, horizonPosition);
                va.velocityDifferance = calculateRelativeErrorAverage(keplerVelocity, horizonVelocity);

                if (bestFittingPosition.positionDifferance >= va.positionDifferance) bestFittingPosition = va;
                if (bestFittingVelocity.velocityDifferance >= va.velocityDifferance) bestFittingVelocity = va;
                if (bestFittingOverall.averageDifferance() >= va.averageDifferance()) bestFittingOverall = va;

                analysis.add(va);
            }
        }

        System.out.println("DONE!");
        System.out.println("Best fitting in terms of position:\n" + bestFittingPosition.toString());
        System.out.println("Best fitting in terms of velocity:\n" + bestFittingVelocity.toString());
        System.out.println("Best fitting in general:\n" + bestFittingOverall.toString());
    }

    public static double calculateRelativeErrorAverage (Vector a, Vector b) {
        double xRE = Math.abs((a.x - b.x) / b.x);
        double yRE = Math.abs((a.y - b.y) / b.y);
        double zRE = Math.abs((a.z - b.z) / b.z);


        return (xRE + yRE + zRE) / 3;
    }
}

class VectorAnalysis {
    public String date;

    public Vector horizonPosition;
    public Vector horizonVelocity;

    public Vector keplerPosition;
    public Vector keplerVelocity;

    //Relative Error rates:
    public double positionDifferance = Double.MAX_VALUE;
    public double velocityDifferance = Double.MAX_VALUE;
    public double averageDifferance() { return (positionDifferance + velocityDifferance) / 2; }

    @Override
    public String toString() {
        return "VectorAnalysis {\n\tdate: " + date +",\n\t" +
                "HorizonPosition: " + horizonPosition.toString() + ",\n\t" +
                "KeplerPosition: " + keplerPosition.toString() + ",\n\t" +
                "HorizonVelocity: " + horizonVelocity.toString() + ",\n\t" +
                "KeplerVelocity: " + keplerVelocity.toString() + ",\n\t" +
                "Delta Position: " + positionDifferance + ",\n\t" +
                "Delta Velocity: " + velocityDifferance + ",\n\t" +
                "Delta Average: " + averageDifferance() + "\n}";
    }
}
class Vector {
    public double x;
    public double y;
    public double z;

    public double calculateAverageDistanceTo(Vector next) {
        return (Math.abs(this.x - next.x) + Math.abs(this.y - next.y) + Math.abs(this.z - next.z)) / 3;
    }

    public Vector (double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "Vector { x: " + x + ", y: " + y + ", z: " + z + " }";
    }

    public Vector substract(Vector next) {
        return new Vector(this.x - next.x, this.y - next.y, this.z - next.z);
    }
}