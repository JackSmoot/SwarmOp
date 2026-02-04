package algorithms;

import core.Algorithm;
import core.Problem;
import core.Solution;
import core.SearchSpace;
import java.util.ArrayList;
import java.util.List;

public class RandomSearch implements Algorithm {
    private Problem problem;
    private SearchSpace searchSpace;
    private Solution bestSolution;
    private List<Solution> currentSolutions;
    private int iteration;
    private int maxIterations;
    private int populationSize;
    
    public RandomSearch(int maxIterations, int populationSize) {
        this.maxIterations = maxIterations;
        this.populationSize = populationSize;
        this.currentSolutions = new ArrayList<>();
        this.iteration = 0;
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
        
        // Initialize best solution
        this.bestSolution = searchSpace.getRandomSolution();
        this.bestSolution.setFitness(problem.evaluate(bestSolution.getPosition()));
    }
    
    @Override
    public boolean step() {
        if (iteration >= maxIterations) {
            return false;
        }
        
        currentSolutions.clear();
        
        // Generate random solutions
        for (int i = 0; i < populationSize; i++) {
            Solution solution = searchSpace.getRandomSolution();
            solution.setFitness(problem.evaluate(solution.getPosition()));
            currentSolutions.add(solution);
            
            // Update best if better
            if (solution.getFitness() < bestSolution.getFitness()) {
                bestSolution = new Solution(solution);
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
        return "Random Search";
    }
    
    @Override
    public int getIteration() {
        return iteration;
    }
}