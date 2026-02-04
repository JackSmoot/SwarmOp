package algorithms;

import core.Algorithm;
import core.Problem;
import core.Solution;
import core.SearchSpace;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleSwarmOptimization implements Algorithm {
    private Problem problem;
    private SearchSpace searchSpace;
    private List<Particle> particles;
    private Solution globalBest;
    private List<Solution> currentSolutions;
    private int iteration;
    private int maxIterations;
    private int swarmSize;
    private double inertiaWeight;
    private double cognitiveWeight;
    private double socialWeight;
    private Random random;
    
    // Inner class to represent a particle
    private class Particle {
        Solution position;
        double[] velocity;
        Solution personalBest;
        
        Particle(int dimensions) {
            position = new Solution(dimensions);
            velocity = new double[dimensions];
            personalBest = new Solution(dimensions);
        }
    }
    
    public ParticleSwarmOptimization(int maxIterations, int swarmSize, 
                                     double inertiaWeight, double cognitiveWeight, 
                                     double socialWeight) {
        this.maxIterations = maxIterations;
        this.swarmSize = swarmSize;
        this.inertiaWeight = inertiaWeight;
        this.cognitiveWeight = cognitiveWeight;
        this.socialWeight = socialWeight;
        this.particles = new ArrayList<>();
        this.currentSolutions = new ArrayList<>();
        this.iteration = 0;
        this.random = new Random();
    }
    
    public ParticleSwarmOptimization(int maxIterations, int swarmSize) {
        // Default PSO parameters
        this(maxIterations, swarmSize, 0.7298, 1.49618, 1.49618);
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
        this.particles.clear();
        this.currentSolutions.clear();
        
        double range = problem.getUpperBound() - problem.getLowerBound();
        
        // Initialize swarm
        for (int i = 0; i < swarmSize; i++) {
            Particle particle = new Particle(problem.getDimensions());
            
            // Random position
            Solution randomPos = searchSpace.getRandomSolution();
            particle.position = randomPos;
            particle.position.setFitness(problem.evaluate(particle.position.getPosition()));
            
            // Random velocity
            for (int j = 0; j < problem.getDimensions(); j++) {
                particle.velocity[j] = (random.nextDouble() - 0.5) * range * 0.1;
            }
            
            // Set personal best to initial position
            particle.personalBest = new Solution(particle.position);
            
            particles.add(particle);
        }
        
        // Initialize global best
        globalBest = new Solution(particles.get(0).position);
        for (Particle p : particles) {
            if (p.position.getFitness() < globalBest.getFitness()) {
                globalBest = new Solution(p.position);
            }
        }
    }
    
    @Override
    public boolean step() {
        if (iteration >= maxIterations) {
            return false;
        }
        
        currentSolutions.clear();
        
        // Update each particle
        for (Particle particle : particles) {
            updateVelocity(particle);
            updatePosition(particle);
            
            // Evaluate new position
            particle.position.setFitness(problem.evaluate(particle.position.getPosition()));
            currentSolutions.add(new Solution(particle.position));
            
            // Update personal best
            if (particle.position.getFitness() < particle.personalBest.getFitness()) {
                particle.personalBest = new Solution(particle.position);
            }
            
            // Update global best
            if (particle.position.getFitness() < globalBest.getFitness()) {
                globalBest = new Solution(particle.position);
            }
        }
        
        iteration++;
        return true;
    }
    
    private void updateVelocity(Particle particle) {
        double[] pos = particle.position.getPosition();
        double[] pBest = particle.personalBest.getPosition();
        double[] gBest = globalBest.getPosition();
        
        for (int i = 0; i < particle.velocity.length; i++) {
            double r1 = random.nextDouble();
            double r2 = random.nextDouble();
            
            // PSO velocity update equation
            particle.velocity[i] = inertiaWeight * particle.velocity[i]
                                 + cognitiveWeight * r1 * (pBest[i] - pos[i])
                                 + socialWeight * r2 * (gBest[i] - pos[i]);
            
            // Velocity clamping (optional but helps stability)
            double maxVelocity = (problem.getUpperBound() - problem.getLowerBound()) * 0.2;
            particle.velocity[i] = Math.max(-maxVelocity, Math.min(maxVelocity, particle.velocity[i]));
        }
    }
    
    private void updatePosition(Particle particle) {
        double[] pos = particle.position.getPosition();
        
        for (int i = 0; i < pos.length; i++) {
            pos[i] += particle.velocity[i];
        }
        
        // Clamp to search space bounds
        searchSpace.clamp(pos);
        particle.position.setPosition(pos);
    }
    
    @Override
    public Solution getBestSolution() {
        return globalBest;
    }
    
    @Override
    public List<Solution> getCurrentSolutions() {
        return new ArrayList<>(currentSolutions);
    }
    
    @Override
    public void reset() {
        iteration = 0;
        particles.clear();
        currentSolutions.clear();
        if (problem != null) {
            initialize(problem);
        }
    }
    
    @Override
    public String getName() {
        return "Particle Swarm Optimization";
    }
    
    @Override
    public int getIteration() {
        return iteration;
    }
    
    // Getter methods for visualization
    public List<Particle> getParticles() {
        return particles;
    }
    
    public double[] getVelocity(int particleIndex) {
        if (particleIndex >= 0 && particleIndex < particles.size()) {
            return particles.get(particleIndex).velocity.clone();
        }
        return null;
    }
}