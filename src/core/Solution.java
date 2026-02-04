package core;

public class Solution {
    private double[] position;
    private double fitness;
    
    public Solution(int dimensions) {
        this.position = new double[dimensions];
        this.fitness = Double.MAX_VALUE;
    }
    
    public Solution(double[] position) {
        this.position = position.clone();
        this.fitness = Double.MAX_VALUE;
    }
    
    public Solution(Solution other) {
        this.position = other.position.clone();
        this.fitness = other.fitness;
    }
    
    public double[] getPosition() {
        return position;
    }
    
    public void setPosition(double[] position) {
        this.position = position.clone();
    }
    
    public double getFitness() {
        return fitness;
    }
    
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
    
    public int getDimensions() {
        return position.length;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Position: [");
        for (int i = 0; i < position.length; i++) {
            sb.append(String.format("%.4f", position[i]));
            if (i < position.length - 1) sb.append(", ");
        }
        sb.append("], Fitness: ").append(String.format("%.4f", fitness));
        return sb.toString();
    }
} 
