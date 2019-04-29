package jGntx;
import java.util.Random;

public class Test {
    //Simply, a target to be reached. If this is a distance to a planet, simply it would be as low as possible (radius of the planet maybe...)
    public static final String TARGET = "Hello World!";

    //a worst case fitness. it doesn't actually matter. you can do ga without this.
    public static final int WORST_CASE_FITNESS = 100;

    public static void main (String args[]) {
        //Create our population list
        //Individual[] indList = new Individual[20];
        //for (int i = 0; i < 20; i++) indList[i] = new Individual<String>()
        /*Create a new population, and set individual count to a number.
        Population monkeys = new Population() {
            //Pretty important. Set the abstract method updateFitnessValue to set their fitness value calculation algorithm
            @Override
            public void updateFitnessValue(Individual ind) {
                int fitness = 0;

                for (int i = 0; i < ((String) ind.getChromosome()).length(); i++) {
                    fitness += Math.pow(((int) TARGET.charAt(i)) - ((int) ((String) ind.getChromosome()).charAt(i)), 2);
                }

                ind.setFitness(Math.abs(100 - ((double) fitness / (WORST_CASE_FITNESS / 100))));
            }
        };

        //craete an evolutionary ga loop. feed our population as the parameter.
        Evolutionary typingMonkey = new Evolutionary(monkeys);
    }

    public String generateString(Random random, int length) {
        String characters
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(random.nextInt(characters.length()));
        }
        return new String(text);*/
    }

}
