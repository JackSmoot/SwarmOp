import core.Algorithm;
import core.Problem;
import core.Solution;

import javax.swing.SwingUtilities;

import algorithms.*;
import problems.*;
import visualization.MainFrame;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });

        System.out.println("╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║          Swarm Algorithm Visualizer - Console Tests               ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝\n");
        
        // Test on Rastrigin (good for showing swarm behavior)
        Problem problem = new Rastrigin(2);
        
        System.out.println("Testing Problem: " + problem.getName());
        System.out.println("Global Optimum: f(0, 0) = 0\n");
        
        // Create all algorithms
        Algorithm[] algorithms = {
            new RandomSearch(150, 30),
            new HillClimber(150, 1.0),
            new SimulatedAnnealing(150, 100.0, 0.95, 1.0),
            new StochasticLocalSearch(150, 10, 1.0),
            new ParticleSwarmOptimization(150, 30),
            new GeneticAlgorithm(150, 40),
            new DifferentialEvolution(150, 40),
            new AntColonyOptimization(150, 30)
        };
        
        // Test each algorithm
        for (Algorithm algo : algorithms) {
            testAlgorithm(algo, problem);
        }
        
        System.out.println("╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                      All Tests Complete!                          ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");
    }
    
    private static void testAlgorithm(Algorithm algorithm, Problem problem) {
        System.out.println("┌" + "─".repeat(68) + "┐");
        System.out.println("│ " + String.format("%-66s", algorithm.getName()) + " │");
        System.out.println("└" + "─".repeat(68) + "┘");
        
        algorithm.initialize(problem);
        
        int reportInterval = 30;
        while (algorithm.step()) {
            if (algorithm.getIteration() % reportInterval == 0) {
                Solution best = algorithm.getBestSolution();
                System.out.printf("  Iter %3d: Fitness = %.8f\n", 
                    algorithm.getIteration(), best.getFitness());
            }
        }
        
        Solution finalBest = algorithm.getBestSolution();
        System.out.println("\n  ✓ Final Result:");
        System.out.printf("    Position: [%.6f, %.6f]\n", 
            finalBest.getPosition()[0], finalBest.getPosition()[1]);
        System.out.printf("    Fitness:  %.8f\n", finalBest.getFitness());
        System.out.println();
    }
}