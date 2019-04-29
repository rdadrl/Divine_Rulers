package jGntx;

public abstract class Population<T> {
    protected Individual<T>[] population;

    public Population (Individual<T>[] individualList) {
        population = individualList;
    }
    public void updateFitnessValues () { for (Individual<T> ind : population) updateFitnessValue(ind); }
    public abstract void updateFitnessValue(Individual<T> ind);

    public void sortPopulationByFitness () {
        quickSort.sort(population, 0, population.length - 1);
    }
    public Individual[] getPopulation() { return population; }
}