package problems;

import core.Problem;

/**
 * Michalewicz function - steep ridges and drops
 * n! local minima, very multimodal
 * GA's population diversity helps explore multiple ridges
 * 
 * Global minimum depends on dimensions
 * For 2D: approximately -1.8013 at (2.20, 1.57)
 * Search domain: [0, π]
 */
public class Michalewicz implements Problem {
    private int dimensions;
    private double lowerBound;
    private double upperBound;
    private int m; // steepness parameter
    
    public Michalewicz(int dimensions) {
        this.dimensions = dimensions;
        this.lowerBound = 0.0;
        this.upperBound = Math.PI;
        this.m = 10; // higher m = steeper valleys
    }
    
    @Override
    public double evaluate(double[] position) {
        double sum = 0.0;
        
        for (int i = 0; i < position.length; i++) {
            double x = position[i];
            sum += Math.sin(x) * Math.pow(Math.sin((i + 1) * x * x / Math.PI), 2 * m);
        }
        
        return -sum; // negate because we want to minimize
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
        return "Michalewicz Function";
    }
}