package problems;

import core.Problem;

/**
 * Deceptive Trap function - designed to mislead gradient-based methods
 * Has a strong local attractor away from the global optimum
 * GA's crossover can help escape the deceptive region
 * 
 * This creates multiple "traps" where local search gets stuck
 * Global minimum: at boundaries
 */
public class DeceptiveTrap implements Problem {
    private int dimensions;
    private double lowerBound;
    private double upperBound;
    
    public DeceptiveTrap(int dimensions) {
        this.dimensions = dimensions;
        this.lowerBound = -5.0;
        this.upperBound = 5.0;
    }
    
    @Override
    public double evaluate(double[] position) {
        double fitness = 0.0;
        
        for (double x : position) {
            // Each dimension has a trap
            if (Math.abs(x) < 1.0) {
                // Deceptive region - looks good but isn't optimal
                fitness += 1.0 - Math.abs(x);
            } else {
                // Actually better region
                fitness += Math.abs(x) / 5.0;
            }
        }
        
        return fitness;
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
        return "Deceptive Trap";
    }
}