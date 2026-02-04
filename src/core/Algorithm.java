package core;

import java.util.List;

public interface Algorithm {
    /**
     * Initialize the algorithm with a problem
     */
    void initialize(Problem problem);
    
    /**
     * Perform one iteration/step of the algorithm
     * Returns true if the algorithm should continue, false if done
     */
    boolean step();
    
    /**
     * Get the current best solution found
     */
    Solution getBestSolution();
    
    /**
     * Get all current solutions (for visualization)
     */
    List<Solution> getCurrentSolutions();
    
    /**
     * Reset the algorithm to initial state
     */
    void reset();
    
    /**
     * Get the name of this algorithm
     */
    String getName();
    
    /**
     * Get the current iteration number
     */
    int getIteration();
}