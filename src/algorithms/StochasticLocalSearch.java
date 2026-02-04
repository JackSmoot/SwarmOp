package algorithms;

import core.Algorithm;
import core.Problem;
import core.Solution;
import core.SearchSpace;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StochasticLocalSearch implements Algorithm {
    private Problem problem;
    private SearchSpace searchSpace;
    private Solution currentSolution;
    private Solution bestSolution;
    private List<Solution> currentSolutions;
    private int iteration;
    private int maxIterations;
    private int numNeighbors;
    private double stepSize;
    private Random random;
    
    public StochasticLocalSearch(int maxIterations, int numNeighbors, double stepSize) {
        this.maxIterations = maxIterations;
        this.numNeighbors = numNeighbors;
        this.stepSize = stepSize;
        this.currentSolutions = new ArrayList<>();
        this.iteration = 0;
        this.random = new Random();
    }
    
    @Override
    public void initialize(Problem problem) {
        this.problem = problem;
        this.searchSpace = new SearchSpace(
            problem.getDimensions(),
            problem.getLowerBound(),
            problem.getUpperBound()
        );
        this.iteration = 0;
        this.currentSolutions.clear();
        
        // Start with random solution
        this.currentSolution = searchSpace.getRandomSolution();
        this.currentSolution.setFitness(problem.evaluate(currentSolution.getPosition()));
        this.bestSolution = new Solution(currentSolution);
    }
    
    @Override
    public boolean step() {
        if (iteration >= maxIterations) {
            return false;
        }
        
        currentSolutions.clear();
        currentSolutions.add(new Solution(currentSolution));
        
        Solution bestNeighbor = null;
        
        // Generate multiple neighbors and pick the best one
        for (int i = 0; i < numNeighbors; i++) {
            double[] newPosition = currentSolution.getPosition().clone();
            for (int j = 0; j < newPosition.length; j++) {
                newPosition[j] += (random.nextDouble() - 0.5) * 2 * stepSize;
            }
            
            searchSpace.clamp(newPosition);
            
            Solution neighbor = new Solution(newPosition);
            neighbor.setFitness(problem.evaluate(neighbor.getPosition()));
            currentSolutions.add(neighbor);
            
            if (bestNeighbor == null || neighbor.getFitness() < bestNeighbor.getFitness()) {
                bestNeighbor = new Solution(neighbor);
            }
        }
        
        // Move to best neighbor if it's better than current
        if (bestNeighbor != null && bestNeighbor.getFitness() < currentSolution.getFitness()) {
            currentSolution = new Solution(bestNeighbor);
            
            // Update global best
            if (currentSolution.getFitness() < bestSolution.getFitness()) {
                bestSolution = new Solution(currentSolution);
            }
        }
        
        iteration++;
        return true;
    }
    
    @Override
    public Solution getBestSolution() {
        return bestSolution;
    }
    
    @Override
    public List<Solution> getCurrentSolutions() {
        return new ArrayList<>(currentSolutions);
    }
    
    @Override
    public void reset() {
        iteration = 0;
        currentSolutions.clear();
        if (problem != null) {
            initialize(problem);
        }
    }
    
    @Override
    public String getName() {
        return "Stochastic Local Search";
    }
    
    @Override
    public int getIteration() {
        return iteration;
    }
}