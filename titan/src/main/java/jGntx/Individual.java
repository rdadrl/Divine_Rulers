package jGntx;

public abstract class Individual<T> {
    private double fitness;
    private T chromosome;

    public Individual(T data) {
        this.chromosome = data;
        this.fitness = 0;
    }


    public T getChromosome() {
        return chromosome;
    }

    public void setChromosome(T chromosome) {
        this.chromosome = chromosome;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public abstract T mutate (T source);

    /*
    removed, don't know the T type. make abstract?
    public Individual clone() {
        char[] chromClone = new char[chromosome.length];
        for(int i = 0; i < chromClone.length; i++) {
            chromClone[i] = chromosome[i];
        }
        return new Individual(chromClone);
    }*/
}


