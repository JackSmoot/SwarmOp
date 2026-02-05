package problems;

import core.Problem;

/**
 * Schwefel function - highly deceptive!
 * Global minimum is far from the next best local minima
 * The surface is composed of a great number of peaks and valleys
 * GA's crossover can help "jump" between distant good regions
 * 
 * Global minimum: f(420.9687,...,420.9687) = 0
 * Search domain: [-500, 500]
 */
public class Schwefel implements Problem {
    private int dimensions;
    private double lowerBound;
    private double upperBound;
    
    public Schwefel(int dimensions) {
        this.dimensions = dimensions;
        this.lowerBound = -500.0;
        this.upperBound = 500.0;
    }
    
    @Override
    public double evaluate(double[] position) {
        double sum = 0.0;
        for (double x : position) {
            sum += x * Math.sin(Math.sqrt(Math.abs(x)));
        }
        return 418.9829 * dimensions - sum;
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
        return "Schwefel Function";
    }
}