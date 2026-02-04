package core;

import java.util.Random;

public class SearchSpace {
    private int dimensions;
    private double lowerBound;
    private double upperBound;
    private Random random;
    
    public SearchSpace(int dimensions, double lowerBound, double upperBound) {
        this.dimensions = dimensions;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.random = new Random();
    }
    
    public Solution getRandomSolution() {
        double[] position = new double[dimensions];
        for (int i = 0; i < dimensions; i++) {
            position[i] = lowerBound + random.nextDouble() * (upperBound - lowerBound);
        }
        return new Solution(position);
    }
    
    public void clamp(double[] position) {
        for (int i = 0; i < position.length; i++) {
            position[i] = Math.max(lowerBound, Math.min(upperBound, position[i]));
        }
    }
    
    public boolean isInBounds(double[] position) {
        for (double p : position) {
            if (p < lowerBound || p > upperBound) {
                return false;
            }
        }
        return true;
    }
    
    public int getDimensions() {
        return dimensions;
    }
    
    public double getLowerBound() {
        return lowerBound;
    }
    
    public double getUpperBound() {
        return upperBound;
    }
}