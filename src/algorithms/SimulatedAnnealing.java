package algorithms;

import core.Algorithm;
import core.Problem;
import core.Solution;
import core.SearchSpace;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulatedAnnealing implements Algorithm {
    private Problem problem;
    private SearchSpace searchSpace;
    private Solution currentSolution;
    private Solution bestSolution;
    private List<Solution> currentSolutions;
    private int iteration;
    private int maxIterations;
    private double initialTemperature;
    private double currentTemperature;
    private double coolingRate;
    private double stepSize;
    private Random random;
    
    public SimulatedAnnealing(int maxIterations, double initialTemperature, 
                              double coolingRate, double stepSize) {
        this.maxIterations = maxIterations;
        this.initialTemperature = initialTemperature;
        this.currentTemperature = initialTemperature;
        this.coolingRate = coolingRate;
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
        this.currentTemperature = initialTemperature;
        
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
        
        // Generate neighbor
        double[] newPosition = currentSolution.getPosition().clone();
        for (int i = 0; i < newPosition.length; i++) {
            newPosition[i] += (random.nextDouble() - 0.5) * 2 * stepSize;
        }
        
        // Clamp to bounds
        searchSpace.clamp(newPosition);
        
        Solution neighbor = new Solution(newPosition);
        neighbor.setFitness(problem.evaluate(neighbor.getPosition()));
        currentSolutions.add(neighbor);
        
        // Calculate acceptance probability
        double delta = neighbor.getFitness() - currentSolution.getFitness();
        
        // Accept if better OR with probability based on temperature
        if (delta < 0 || random.nextDouble() < Math.exp(-delta / currentTemperature)) {
            currentSolution = new Solution(neighbor);
            
            // Update global best
            if (currentSolution.getFitness() < bestSolution.getFitness()) {
                bestSolution = new Solution(currentSolution);
            }
        }
        
        // Cool down
        currentTemperature *= coolingRate;
        
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
        currentTemperature = initialTemperature;
        if (problem != null) {
            initialize(problem);
        }
    }
    
    @Override
    public String getName() {
        return "Simulated Annealing";
    }
    
    @Override
    public int getIteration() {
        return iteration;
    }
    
    public double getCurrentTemperature() {
        return currentTemperature;
    }
}
