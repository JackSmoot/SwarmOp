package core;

public interface Problem {
    /**
     * Evaluate the fitness of a solution
     * Lower values are better (minimization)
     */
    double evaluate(double[] position);
    
    /**
     * Get the number of dimensions for this problem
     */
    int getDimensions();
    
    /**
     * Get the lower bound for each dimension
     */
    double getLowerBound();
    
    /**
     * Get the upper bound for each dimension
     */
    double getUpperBound();
    
    /**
     * Get the name of this problem
     */
    String getName();
}
