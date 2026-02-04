package problems;

import core.Problem;

/**
 * Ackley function
 * Global minimum: f(0,...,0) = 0
 * Highly multimodal with a nearly flat outer region
 */
public class Ackley implements Problem {
    private int dimensions;
    private double lowerBound;
    private double upperBound;
    
    public Ackley(int dimensions) {
        this.dimensions = dimensions;
        this.lowerBound = -32.768;
        this.upperBound = 32.768;
    }
    
    @Override
    public double evaluate(double[] position) {
        double sum1 = 0.0;
        double sum2 = 0.0;
        
        for (double x : position) {
            sum1 += x * x;
            sum2 += Math.cos(2.0 * Math.PI * x);
        }
        
        return -20.0 * Math.exp(-0.2 * Math.sqrt(sum1 / dimensions)) 
               - Math.exp(sum2 / dimensions) 
               + 20.0 + Math.E;
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
        return "Ackley Function";
    }
}