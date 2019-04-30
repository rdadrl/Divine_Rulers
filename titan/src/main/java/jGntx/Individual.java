package jGntx;

public abstract class Individual<T> {
    private double fitness = Double.MAX_VALUE;
    private T chromosome;
    private int id;
    private static int lastId = 1;
    public Individual(T data) {
        this.chromosome = data;
        this.fitness = 0;
        this.id = lastId;
        lastId++;
    }

    public int getId() { return id; }

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


