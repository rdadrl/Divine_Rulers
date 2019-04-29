package jGntx;

public class quickSort {
    private static int partition(Individual[] array, int begin, int end) {
        // pivot = end
        int counter = begin;

        for (int i = begin; i < end; i++) {
            if (array[i].getFitness() < array[end].getFitness()) {
                Individual temp = array[counter];
                array[counter] = array[i];
                array[i] = temp;
                counter++;
            }
        }
        Individual temp = array[end];
        array[end] = array[counter];
        array[counter] = temp;

        return counter;
    }

    public static void sort(Individual[] array, int begin, int end) {
        if (end <= begin) return;
        int pivot = partition(array, begin, end);
        sort(array, begin, pivot-1);
        sort(array, pivot+1, end);
    }
}
