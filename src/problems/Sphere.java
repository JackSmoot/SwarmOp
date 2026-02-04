package problems;

import core.Problem;

/**
 * Sphere function: f(x) = sum(x_i^2)
 * Global minimum: f(0,...,0) = 0
 * Simple convex function - good for testing
 */
public class Sphere implements Problem {
    private int dimensions;
    private double lowerBound;
    private double upperBound;
    
    public Sphere(int dimensions) {
        this.dimensions = dimensions;
        this.lowerBound = -100.0;
        this.upperBound = 100.0;
    }
    
    @Override
    public double evaluate(double[] position) {
        double sum = 0.0;
        for (double x : position) {
            sum += x * x;
        }
        return sum;
    }
    
    @Override
    public int getDimensions() {
        return dimensions;
    }
    
    @Override
    public double getLowerBound() {
        return lowerBound;
    }
    
    @Override
    public double getUpperBound() {
        return upperBound;
    }
    
    @Override
    public String getName() {
        return "Sphere Function";
    }
}