package visualization;

import core.Algorithm;
import core.Problem;
import core.Solution;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.List;

public class VisualizationPanel extends JPanel {
    private Algorithm algorithm;
    private Problem problem;
    private double[][] heatmapCache;
    private int resolution = 100;
    private boolean showHeatmap = true;
    private boolean showTrails = false;
    
    public VisualizationPanel() {
        setPreferredSize(new Dimension(800, 800));
        setBackground(Color.BLACK);
    }
    
    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }
    
    public void setProblem(Problem problem) {
        this.problem = problem;
        generateHeatmap();
    }
    
    public void setShowHeatmap(boolean show) {
        this.showHeatmap = show;
        repaint();
    }
    
    public void setShowTrails(boolean show) {
        this.showTrails = show;
        repaint();
    }
    
    private void generateHeatmap() {
        if (problem == null) return;
        
        heatmapCache = new double[resolution][resolution];
        double minFitness = Double.MAX_VALUE;
        double maxFitness = Double.MIN_VALUE;
        
        // Calculate fitness for each point
        for (int i = 0; i < resolution; i++) {
            for (int j = 0; j < resolution; j++) {
                double x = problem.getLowerBound() + 
                          (problem.getUpperBound() - problem.getLowerBound()) * i / (resolution - 1);
                double y = problem.getLowerBound() + 
                          (problem.getUpperBound() - problem.getLowerBound()) * j / (resolution - 1);
                
                double fitness = problem.evaluate(new double[]{x, y});
                heatmapCache[i][j] = fitness;
                
                minFitness = Math.min(minFitness, fitness);
                maxFitness = Math.max(maxFitness, fitness);
            }
        }
        
        // Normalize
        for (int i = 0; i < resolution; i++) {
            for (int j = 0; j < resolution; j++) {
                heatmapCache[i][j] = (heatmapCache[i][j] - minFitness) / (maxFitness - minFitness);
            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (problem == null || algorithm == null) {
            drawWelcomeMessage(g2d);
            return;
        }
        
        // Draw heatmap
        if (showHeatmap && heatmapCache != null) {
            drawHeatmap(g2d);
        }
        
       // Draw global optimum marker
        drawGlobalOptimum(g2d);
        
        // Draw solutions
        drawSolutions(g2d);
        
        // Draw best solution
        drawBestSolution(g2d);
        
        // Draw info
        drawInfo(g2d);
    }
    
    private void drawWelcomeMessage(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        String msg = "Select Algorithm and Problem to Start";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(msg)) / 2;
        int y = getHeight() / 2;
        g2d.drawString(msg, x, y);
    }
    
    private void drawHeatmap(Graphics2D g2d) {
        int cellWidth = getWidth() / resolution;
        int cellHeight = getHeight() / resolution;
        
        for (int i = 0; i < resolution; i++) {
            for (int j = 0; j < resolution; j++) {
                float intensity = (float) (1.0 - heatmapCache[i][j]);
                Color color = new Color(intensity * 0.3f, intensity * 0.1f, intensity * 0.5f);
                g2d.setColor(color);
                g2d.fillRect(i * cellWidth, (resolution - 1 - j) * cellHeight, 
                           cellWidth + 1, cellHeight + 1);
            }
        }
    }
    
    private void drawSolutions(Graphics2D g2d) {
        List<Solution> solutions = algorithm.getCurrentSolutions();
        if (solutions == null || solutions.isEmpty()) return;
        
        for (Solution sol : solutions) {
            Point2D.Double point = toScreenCoordinates(sol.getPosition());
            
            // Draw solution point
            g2d.setColor(new Color(0, 255, 200, 180));
            int size = 4;
            g2d.fillOval((int)point.x - size/2, (int)point.y - size/2, size, size);
            
            // Draw glow
            g2d.setColor(new Color(0, 255, 200, 50));
            g2d.fillOval((int)point.x - size, (int)point.y - size, size*2, size*2);
        }
    }
    
    private void drawBestSolution(Graphics2D g2d) {
        Solution best = algorithm.getBestSolution();
        if (best == null) return;
        
        Point2D.Double point = toScreenCoordinates(best.getPosition());
        
        // Draw pulsing ring
        int size = 16;
        g2d.setColor(new Color(255, 255, 0, 200));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval((int)point.x - size/2, (int)point.y - size/2, size, size);
        
        // Draw center
        g2d.setColor(new Color(255, 255, 0, 255));
        g2d.fillOval((int)point.x - 6, (int)point.y - 6, 12, 12);
        
        // Draw glow
        g2d.setColor(new Color(255, 255, 0, 80));
        g2d.fillOval((int)point.x - size, (int)point.y - size, size*2, size*2);
    }

    private void drawGlobalOptimum(Graphics2D g2d) {
        // Get the known global optimum position for this problem
        double[] optimumPos = getGlobalOptimumPosition();
        if (optimumPos == null) return;
        
        Point2D.Double point = toScreenCoordinates(optimumPos);
        
        // Draw a green crosshair marker
        g2d.setColor(new Color(0, 255, 0, 150));
        g2d.setStroke(new BasicStroke(2));
        
        int size = 12;
        // Horizontal line
        g2d.drawLine((int)point.x - size, (int)point.y, (int)point.x + size, (int)point.y);
        // Vertical line
        g2d.drawLine((int)point.x, (int)point.y - size, (int)point.x, (int)point.y + size);
        
        // Draw circle around it
        g2d.setColor(new Color(0, 255, 0, 100));
        g2d.drawOval((int)point.x - 8, (int)point.y - 8, 16, 16);
    }
    
    private double[] getGlobalOptimumPosition() {
        if (problem == null) return null;
        
        String problemName = problem.getName();
        switch (problemName) {
            case "Sphere Function":
            case "Rastrigin Function":
            case "Ackley Function":
            case "Griewank Function":
                return new double[]{0.0, 0.0};
            case "Schwefel Function":
                return new double[]{420.9687, 420.9687};
            case "Michalewicz Function":
                return new double[]{2.20, 1.57};
            case "Levy Function":
                return new double[]{1.0, 1.0};
            case "Deceptive Trap":
                // For deceptive trap, the optimum is at the boundary
                // Let's show one of them
                return new double[]{5.0, 5.0};
            default:
                return null;
        }
    }
    
    private void drawInfo(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 14));
        
        int y = 25;
        int x = 15;
        
        g2d.drawString("Algorithm: " + algorithm.getName(), x, y);
        y += 20;
        g2d.drawString("Problem: " + problem.getName(), x, y);
        y += 20;
        g2d.drawString("Iteration: " + algorithm.getIteration(), x, y);
        y += 20;
        
        Solution best = algorithm.getBestSolution();
        if (best != null) {
            g2d.drawString(String.format("Best Fitness: %.6f", best.getFitness()), x, y);
            y += 20;
            g2d.drawString(String.format("Position: [%.3f, %.3f]", 
                best.getPosition()[0], best.getPosition()[1]), x, y);
        }
        
        // Draw problem-specific optimum info
        y += 30;
        g2d.setFont(new Font("Monospaced", Font.PLAIN, 12));
        g2d.setColor(new Color(100, 255, 100));
        
        String problemName = problem.getName();
        switch (problemName) {
            case "Sphere Function":
                g2d.drawString("Global Optimum: (0.0, 0.0)", x, y);
                y += 15;
                g2d.drawString("Target Fitness: 0.0", x, y);
                break;
            case "Rastrigin Function":
                g2d.drawString("Global Optimum: (0.0, 0.0)", x, y);
                y += 15;
                g2d.drawString("Target Fitness: 0.0", x, y);
                break;
            case "Ackley Function":
                g2d.drawString("Global Optimum: (0.0, 0.0)", x, y);
                y += 15;
                g2d.drawString("Target Fitness: 0.0", x, y);
                break;
            case "Schwefel Function":
                g2d.drawString("Global Optimum: (420.97, 420.97)", x, y);
                y += 15;
                g2d.drawString("Target Fitness: 0.0", x, y);
                break;
            case "Griewank Function":
                g2d.drawString("Global Optimum: (0.0, 0.0)", x, y);
                y += 15;
                g2d.drawString("Target Fitness: 0.0", x, y);
                break;
            case "Michalewicz Function":
                g2d.drawString("Global Optimum: ~(2.20, 1.57)", x, y);
                y += 15;
                g2d.drawString("Target Fitness: ~-1.80", x, y);
                break;
            case "Levy Function":
                g2d.drawString("Global Optimum: (1.0, 1.0)", x, y);
                y += 15;
                g2d.drawString("Target Fitness: 0.0", x, y);
                break;
            case "Deceptive Trap":
                g2d.drawString("Global Optimum: at boundaries", x, y);
                y += 15;
                g2d.drawString("(±5.0, ±5.0)", x, y);
                break;
            default:
                g2d.drawString("See problem definition", x, y);
                break;
        }
    }
    private Point2D.Double toScreenCoordinates(double[] position) {
        if (position.length < 2) return new Point2D.Double(0, 0);
        
        double x = position[0];
        double y = position[1];
        
        // Map from problem space to screen space
        double screenX = (x - problem.getLowerBound()) / 
                        (problem.getUpperBound() - problem.getLowerBound()) * getWidth();
        double screenY = getHeight() - (y - problem.getLowerBound()) / 
                        (problem.getUpperBound() - problem.getLowerBound()) * getHeight();
        
        return new Point2D.Double(screenX, screenY);
    }
}