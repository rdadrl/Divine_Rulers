package jGntx;

import solarsystem.Projectile;

import java.util.ArrayList;

public abstract class Evolutionary<T> {
    private Population<T> localPopulation;
    private int maxFitness;
    private int minFitness = 0; //By default, the minimum fitness value is 0. This depends on the application.
    private int cycleIterationCount = 1;
    public Evolutionary (Population<T> newPop) {
        localPopulation = newPop;
    }

    //Called when cycle complete, simply restart your demo system thru this method.
    public abstract void onCycleOver();

    //Use this to trigger cycle over.
    public void triggerCycleOver() {
        String cycleMsg = " - Cycle Iteration: " + cycleIterationCount + " -\n";
        localPopulation.sortPopulationByFitness();
        cycleMsg += "Best 3 individuals by their fitness values:\n";
        Projectile currToP = (Projectile) localPopulation.getPopulation()[0].getChromosome();
        cycleMsg += "1:\n\tInit. Inclination: " + currToP.getDepartureInclination() + "\n\tInit. Velocity: " + currToP.getDepartureVelocity() + "\n";
        currToP = (Projectile) localPopulation.getPopulation()[1].getChromosome();
        cycleMsg += "2:\n\tInit. Inclination: " + currToP.getDepartureInclination() + "\n\tInit. Velocity: " + currToP.getDepartureVelocity() + "\n";
        currToP = (Projectile) localPopulation.getPopulation()[2].getChromosome();
        cycleMsg += "3:\n\tInit. Inclination: " + currToP.getDepartureInclination() + "\n\tInit. Velocity: " + currToP.getDepartureVelocity() + "\n";
        cycleMsg += "\nRestarting cycle...";
        onCycleOver();
        cycleMsg += "\nSystem variables restarted. Mutations & CrossingOver done.";
        System.out.println(cycleMsg);
        cycleIterationCount++;
    }
    public Population<T> getPopulation() { return localPopulation; }


    public abstract void crossOver ();
}
