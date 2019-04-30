package jGntx;

import solarsystem.Projectile;

import java.util.ArrayList;

public abstract class Evolutionary<T> {
    private Population localPopulation;
    private int maxFitness;
    private int minFitness = 0; //By default, the minimum fitness value is 0. This depends on the application.
    private int cycleIterationCount = 1;
    public Evolutionary (Population newPop) {
        localPopulation = newPop;
    }

    //Called when cycle complete, simply restart your demo system thru this method.
    public abstract void onCycleOver();

    //Use this to trigger cycle over.
    public void triggerCycleOver() {
        String cycleMsg = "";
        cycleMsg += this.toString();
        cycleMsg += "\nRestarting cycle...";
        onCycleOver();
        cycleMsg += "\nSystem variables restarted. Mutations & CrossingOver done.";
        System.out.println(cycleMsg);
        cycleIterationCount++;
    }
    public Population getPopulation() { return localPopulation; }


    public abstract void crossOver ();


    public String toString () {
        String tmp = "";
        tmp += "Evolutionary Tree: {\n" +
                "\tCycle Count: " + cycleIterationCount + ",\n" +
                "\tPopulation Count: " + this.getPopulation().getPopulation().length + ",\n" +
                "\tIndividuals (Population): [\n";
        for (int i = 0; i < localPopulation.population.length; i++) {
            tmp += "\t\tIndividual #" + localPopulation.population[i].getId() + ": {\n";
            tmp += "\t\t\tFitness: " + localPopulation.population[i].getFitness() + ",\n";
            tmp += "\t\t\t" + localPopulation.population[i].getChromosome().toString();
            tmp += "\t\t}\n";
        }
        tmp += "\t}\n}";

        return tmp;
    }
}
