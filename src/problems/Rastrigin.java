package problems;

import core.Problem;

/**
 * Rastrigin function: f(x) = 10n + sum(x_i^2 - 10*cos(2*pi*x_i))
 * Global minimum: f(0,...,0) = 0
 * Highly multimodal - many local minima
 */
public class Rastrigin implements Problem {
    private int dimensions;
    private double lowerBound;
    private double upperBound;
    
    public Rastrigin(int dimensions) {
        this.dimensions = dimensions;
        this.lowerBound = -5.12;
        this.upperBound = 5.12;
    }
    
    @Override
    public double evaluate(double[] position) {
        double sum = 10.0 * dimensions;
        for (double x : position) {
            sum += x * x - 10.0 * Math.cos(2.0 * Math.PI * x);
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
        return "Rastrigin Function";
    }
}