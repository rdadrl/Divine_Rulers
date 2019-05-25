package utils;

import sun.awt.image.ImageWatched;

import java.util.*;

/**
 * Linked list of with maximum capacity n, returns the average value of the list. If an item is added,
 * it is removed from the other end
 *
 */
public class AverageQueue {
    private LinkedList<Double> averageQueue;
    private int maxCapacity;
    private double sum;
    private double average;

    public AverageQueue(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        averageQueue = new LinkedList<>();
    }

    public void push(double newItem) {
        averageQueue.addFirst(newItem);
        sum += newItem;
        if(averageQueue.size() > maxCapacity) {
            double removedItem =  averageQueue.removeLast();
            sum -= removedItem;
        }
        average = sum/averageQueue.size();
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public double getSum() {
        return sum;
    }

    public double average(){
        return average;
    }

}
