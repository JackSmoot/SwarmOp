import core.Algorithm;
import core.Problem;
import core.Solution;
import algorithms.*;
import problems.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Swarm Algorithm Visualizer - Console Tests\n");
        
        // Test problem
        Problem problem = new Rastrigin(2);
        
        // Create algorithms
        Algorithm[] algorithms = {
            new RandomSearch(100, 20),
            new HillClimber(100, 1.0),
            new SimulatedAnnealing(100, 100.0, 0.95, 1.0),
            new StochasticLocalSearch(100, 10, 1.0)
        };
        
        // Test each algorithm
        for (Algorithm algo : algorithms) {
            testAlgorithm(algo, problem);
        }
    }
    
    private static void testAlgorithm(Algorithm algorithm, Problem problem) {
        System.out.println("=".repeat(70));
        System.out.println("Algorithm: " + algorithm.getName());
        System.out.println("Problem: " + problem.getName());
        System.out.println("=".repeat(70));
        
        algorithm.initialize(problem);
        
        while (algorithm.step()) {
            if (algorithm.getIteration() % 20 == 0) {
                Solution best = algorithm.getBestSolution();
                System.out.printf("Iteration %3d: Best Fitness = %.6f\n", 
                    algorithm.getIteration(), best.getFitness());
            }
        }
        
        Solution finalBest = algorithm.getBestSolution();
        System.out.println("\nFinal Result:");
        System.out.println(finalBest);
        System.out.println();
    }
}