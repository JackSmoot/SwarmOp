package algorithms;

import core.Algorithm;
import core.Problem;
import core.Solution;
import core.SearchSpace;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm implements Algorithm {
    private Problem problem;
    private SearchSpace searchSpace;
    private List<Solution> population;
    private Solution bestSolution;
    private List<Solution> currentSolutions;
    private int iteration;
    private int maxIterations;
    private int populationSize;
    private double mutationRate;
    private double crossoverRate;
    private int tournamentSize;
    private Random random;
    
    public GeneticAlgorithm(int maxIterations, int populationSize, 
                           double mutationRate, double crossoverRate, 
                           int tournamentSize) {
        this.maxIterations = maxIterations;
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.tournamentSize = tournamentSize;
        this.population = new ArrayList<>();
        this.currentSolutions = new ArrayList<>();
        this.iteration = 0;
        this.random = new Random();
    }
    
    public GeneticAlgorithm(int maxIterations, int populationSize) {
        // Default GA parameters
        this(maxIterations, populationSize, 0.1, 0.8, 3);
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
        this.population.clear();
        this.currentSolutions.clear();
        
        // Initialize random population
        for (int i = 0; i < populationSize; i++) {
            Solution individual = searchSpace.getRandomSolution();
            individual.setFitness(problem.evaluate(individual.getPosition()));
            population.add(individual);
        }
        
        // Find initial best
        bestSolution = new Solution(population.get(0));
        for (Solution ind : population) {
            if (ind.getFitness() < bestSolution.getFitness()) {
                bestSolution = new Solution(ind);
            }
        }
    }
    
    @Override
    public boolean step() {
        if (iteration >= maxIterations) {
            return false;
        }
        
        List<Solution> newPopulation = new ArrayList<>();
        
        // Elitism - keep best solution
        newPopulation.add(new Solution(bestSolution));
        
        // Generate rest of new population
        while (newPopulation.size() < populationSize) {
            // Selection
            Solution parent1 = tournamentSelection();
            Solution parent2 = tournamentSelection();
            
            // Crossover
            Solution offspring;
            if (random.nextDouble() < crossoverRate) {
                offspring = crossover(parent1, parent2);
            } else {
                offspring = new Solution(parent1);
            }
            
            // Mutation
            mutate(offspring);
            
            // Evaluate
            offspring.setFitness(problem.evaluate(offspring.getPosition()));
            newPopulation.add(offspring);
            
            // Update best
            if (offspring.getFitness() < bestSolution.getFitness()) {
                bestSolution = new Solution(offspring);
            }
        }
        
        population = newPopulation;
        currentSolutions = new ArrayList<>(population);
        
        iteration++;
        return true;
    }
    
    private Solution tournamentSelection() {
        Solution best = null;
        for (int i = 0; i < tournamentSize; i++) {
            Solution candidate = population.get(random.nextInt(population.size()));
            if (best == null || candidate.getFitness() < best.getFitness()) {
                best = candidate;
            }
        }
        return best;
    }
    
    private Solution crossover(Solution parent1, Solution parent2) {
        double[] pos1 = parent1.getPosition();
        double[] pos2 = parent2.getPosition();
        double[] childPos = new double[pos1.length];
        
        // Uniform crossover
        for (int i = 0; i < childPos.length; i++) {
            childPos[i] = random.nextBoolean() ? pos1[i] : pos2[i];
        }
        
        return new Solution(childPos);
    }
    
    private void mutate(Solution solution) {
        double[] pos = solution.getPosition();
        double range = problem.getUpperBound() - problem.getLowerBound();
        
        for (int i = 0; i < pos.length; i++) {
            if (random.nextDouble() < mutationRate) {
                // Gaussian mutation
                pos[i] += random.nextGaussian() * range * 0.1;
            }
        }
        
        searchSpace.clamp(pos);
        solution.setPosition(pos);
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
        population.clear();
        currentSolutions.clear();
        if (problem != null) {
            initialize(problem);
        }
    }
    
    @Override
    public String getName() {
        return "Genetic Algorithm";
    }
    
    @Override
    public int getIteration() {
        return iteration;
    }
}