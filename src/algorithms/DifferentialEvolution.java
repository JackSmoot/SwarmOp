package algorithms;

import core.Algorithm;
import core.Problem;
import core.Solution;
import core.SearchSpace;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DifferentialEvolution implements Algorithm {
    private Problem problem;
    private SearchSpace searchSpace;
    private List<Solution> population;
    private Solution bestSolution;
    private List<Solution> currentSolutions;
    private int iteration;
    private int maxIterations;
    private int populationSize;
    private double mutationFactor;
    private double crossoverRate;
    private Random random;
    
    public DifferentialEvolution(int maxIterations, int populationSize, 
                                double mutationFactor, double crossoverRate) {
        this.maxIterations = maxIterations;
        this.populationSize = populationSize;
        this.mutationFactor = mutationFactor;
        this.crossoverRate = crossoverRate;
        this.population = new ArrayList<>();
        this.currentSolutions = new ArrayList<>();
        this.iteration = 0;
        this.random = new Random();
    }
    
    public DifferentialEvolution(int maxIterations, int populationSize) {
        // Default DE parameters (DE/rand/1/bin)
        this(maxIterations, populationSize, 0.8, 0.9);
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
        
        for (int i = 0; i < populationSize; i++) {
            // Select three random distinct individuals
            int[] indices = getThreeRandomIndices(i);
            Solution a = population.get(indices[0]);
            Solution b = population.get(indices[1]);
            Solution c = population.get(indices[2]);
            
            // Mutation: v = a + F * (b - c)
            double[] mutant = new double[problem.getDimensions()];
            for (int j = 0; j < mutant.length; j++) {
                mutant[j] = a.getPosition()[j] + 
                           mutationFactor * (b.getPosition()[j] - c.getPosition()[j]);
            }
            searchSpace.clamp(mutant);
            
            // Crossover
            double[] trial = new double[problem.getDimensions()];
            int jRand = random.nextInt(problem.getDimensions());
            
            for (int j = 0; j < trial.length; j++) {
                if (random.nextDouble() < crossoverRate || j == jRand) {
                    trial[j] = mutant[j];
                } else {
                    trial[j] = population.get(i).getPosition()[j];
                }
            }
            
            // Selection
            Solution trialSolution = new Solution(trial);
            trialSolution.setFitness(problem.evaluate(trial));
            
            if (trialSolution.getFitness() < population.get(i).getFitness()) {
                newPopulation.add(trialSolution);
                
                // Update best
                if (trialSolution.getFitness() < bestSolution.getFitness()) {
                    bestSolution = new Solution(trialSolution);
                }
            } else {
                newPopulation.add(new Solution(population.get(i)));
            }
        }
        
        population = newPopulation;
        currentSolutions = new ArrayList<>(population);
        
        iteration++;
        return true;
    }
    
    private int[] getThreeRandomIndices(int exclude) {
        int[] indices = new int[3];
        for (int i = 0; i < 3; i++) {
            int index;
            do {
                index = random.nextInt(populationSize);
            } while (index == exclude || contains(indices, i, index));
            indices[i] = index;
        }
        return indices;
    }
    
    private boolean contains(int[] array, int length, int value) {
        for (int i = 0; i < length; i++) {
            if (array[i] == value) return true;
        }
        return false;
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
        return "Differential Evolution";
    }
    
    @Override
    public int getIteration() {
        return iteration;
    }
}