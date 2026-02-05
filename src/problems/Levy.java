package problems;

import core.Problem;

/**
 * Levy function - multimodal with deep local minima
 * The function has many local minima arranged in a complex pattern
 * GA's crossover helps escape local minima by combining solutions
 * 
 * Global minimum: f(1,...,1) = 0
 * Search domain: [-10, 10]
 */
public class Levy implements Problem {
    private int dimensions;
    private double lowerBound;
    private double upperBound;
    
    public Levy(int dimensions) {
        this.dimensions = dimensions;
        this.lowerBound = -10.0;
        this.upperBound = 10.0;
    }
    
    @Override
    public double evaluate(double[] position) {
        double[] w = new double[position.length];
        for (int i = 0; i < position.length; i++) {
            w[i] = 1.0 + (position[i] - 1.0) / 4.0;
        }
        
        double term1 = Math.pow(Math.sin(Math.PI * w[0]), 2);
        
        double sum = 0.0;
        for (int i = 0; i < position.length - 1; i++) {
            sum += Math.pow(w[i] - 1, 2) * (1 + 10 * Math.pow(Math.sin(Math.PI * w[i] + 1), 2));
        }
        
        double term3 = Math.pow(w[position.length - 1] - 1, 2) * 
                      (1 + Math.pow(Math.sin(2 * Math.PI * w[position.length - 1]), 2));
        
        return term1 + sum + term3;
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
        return "Levy Function";
    }
}