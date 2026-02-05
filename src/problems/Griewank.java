package problems;

import core.Problem;

/**
 * Griewank function - many widespread local minima
 * The product term introduces interdependence between variables
 * GA's crossover can effectively combine good building blocks
 * 
 * Global minimum: f(0,...,0) = 0
 * Search domain: [-600, 600]
 */
public class Griewank implements Problem {
    private int dimensions;
    private double lowerBound;
    private double upperBound;
    
    public Griewank(int dimensions) {
        this.dimensions = dimensions;
        this.lowerBound = -600.0;
        this.upperBound = 600.0;
    }
    
    @Override
    public double evaluate(double[] position) {
        double sum = 0.0;
        double product = 1.0;
        
        for (int i = 0; i < position.length; i++) {
            double x = position[i];
            sum += (x * x) / 4000.0;
            product *= Math.cos(x / Math.sqrt(i + 1));
        }
        
        return sum - product + 1.0;
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
        return "Griewank Function";
    }
}