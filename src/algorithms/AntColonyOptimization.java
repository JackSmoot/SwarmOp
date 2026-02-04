package algorithms;

import core.Algorithm;
import core.Problem;
import core.Solution;
import core.SearchSpace;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Continuous ACO (ACO_R - ACO for continuous domains)
 * Based on Gaussian kernels around solution archive
 */
public class AntColonyOptimization implements Algorithm {
    private Problem problem;
    private SearchSpace searchSpace;
    private List<Solution> solutionArchive;
    private Solution bestSolution;
    private List<Solution> currentSolutions;
    private int iteration;
    private int maxIterations;
    private int numAnts;
    private int archiveSize;
    private double exploitationFactor;
    private Random random;
    
    public AntColonyOptimization(int maxIterations, int numAnts, 
                                int archiveSize, double exploitationFactor) {
        this.maxIterations = maxIterations;
        this.numAnts = numAnts;
        this.archiveSize = archiveSize;
        this.exploitationFactor = exploitationFactor;
        this.solutionArchive = new ArrayList<>();
        this.currentSolutions = new ArrayList<>();
        this.iteration = 0;
        this.random = new Random();
    }
    
    public AntColonyOptimization(int maxIterations, int numAnts) {
        // Default ACO parameters
        this(maxIterations, numAnts, 10, 0.85);
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
        this.solutionArchive.clear();
        this.currentSolutions.clear();
        
        // Initialize archive with random solutions
        for (int i = 0; i < archiveSize; i++) {
            Solution sol = searchSpace.getRandomSolution();
            sol.setFitness(problem.evaluate(sol.getPosition()));
            solutionArchive.add(sol);
        }
        
        // Sort archive by fitness
        solutionArchive.sort((a, b) -> Double.compare(a.getFitness(), b.getFitness()));
        bestSolution = new Solution(solutionArchive.get(0));
    }
    
    @Override
    public boolean step() {
        if (iteration >= maxIterations) {
            return false;
        }
        
        currentSolutions.clear();
        List<Solution> newSolutions = new ArrayList<>();
        
        // Generate ants
        for (int i = 0; i < numAnts; i++) {
            Solution ant = generateAnt();
            ant.setFitness(problem.evaluate(ant.getPosition()));
            newSolutions.add(ant);
            currentSolutions.add(new Solution(ant));
            
            // Update best
            if (ant.getFitness() < bestSolution.getFitness()) {
                bestSolution = new Solution(ant);
            }
        }
        
        // Update archive
        updateArchive(newSolutions);
        
        iteration++;
        return true;
    }
    
    private Solution generateAnt() {
        double[] position = new double[problem.getDimensions()];
        
        // Select solution from archive based on weights
        Solution selected = selectFromArchive();
        
        // Generate new solution using Gaussian distribution around selected
        double sigma = exploitationFactor * (problem.getUpperBound() - problem.getLowerBound()) / 
                      (2.0 * archiveSize);
        
        for (int i = 0; i < position.length; i++) {
            position[i] = selected.getPosition()[i] + random.nextGaussian() * sigma;
        }
        
        searchSpace.clamp(position);
        return new Solution(position);
    }
    
    private Solution selectFromArchive() {
        // Rank-based selection - better solutions more likely to be selected
        double[] weights = new double[solutionArchive.size()];
        double totalWeight = 0.0;
        
        for (int i = 0; i < solutionArchive.size(); i++) {
            weights[i] = 1.0 / (i + 1); // Rank weight
            totalWeight += weights[i];
        }
        
        double rand = random.nextDouble() * totalWeight;
        double cumulative = 0.0;
        
        for (int i = 0; i < solutionArchive.size(); i++) {
            cumulative += weights[i];
            if (rand <= cumulative) {
                return solutionArchive.get(i);
            }
        }
        
        return solutionArchive.get(0);
    }
    
    private void updateArchive(List<Solution> newSolutions) {
        // Merge new solutions with archive
        solutionArchive.addAll(newSolutions);
        
        // Sort by fitness
        solutionArchive.sort((a, b) -> Double.compare(a.getFitness(), b.getFitness()));
        
        // Keep only best archiveSize solutions
        if (solutionArchive.size() > archiveSize) {
            solutionArchive = new ArrayList<>(solutionArchive.subList(0, archiveSize));
        }
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
        solutionArchive.clear();
        currentSolutions.clear();
        if (problem != null) {
            initialize(problem);
        }
    }
    
    @Override
    public String getName() {
        return "Ant Colony Optimization";
    }
    
    @Override
    public int getIteration() {
        return iteration;
    }
}