package visualization;

import core.Algorithm;
import core.Problem;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame implements ControlPanel.ControlListener {
    private VisualizationPanel visualizationPanel;
    private ControlPanel controlPanel;
    private Timer timer;
    private boolean running = false;
    private Algorithm currentAlgorithm;
    private Problem currentProblem;
    
    public MainFrame() {
        setTitle("Swarm Algorithm Visualizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Create panels
        visualizationPanel = new VisualizationPanel();
        controlPanel = new ControlPanel();
        controlPanel.setControlListener(this);
        
        // Add panels
        add(visualizationPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);
        
        // Setup timer
        timer = new Timer(50, e -> step());
        
        pack();
        setLocationRelativeTo(null);
    }
    
    @Override
    public void onStart() {
        if (currentAlgorithm == null || currentProblem == null) {
            return;
        }
        
        // Initialize if not already done
        if (currentAlgorithm.getIteration() == 0) {
            currentAlgorithm.initialize(currentProblem);
        }
        
        running = true;
        timer.start();
        controlPanel.setRunning(true);
    }
    
    @Override
    public void onPause() {
        running = false;
        timer.stop();
        controlPanel.setRunning(false);
    }
    
    @Override
    public void onReset() {
        onPause();
        if (currentAlgorithm != null && currentProblem != null) {
            currentAlgorithm.reset();
            currentAlgorithm.initialize(currentProblem);
            visualizationPanel.repaint();
        }
    }
    
    @Override
    public void onStep() {
        if (currentAlgorithm == null || currentProblem == null) {
            return;
        }
        
        // Initialize if not already done
        if (currentAlgorithm.getIteration() == 0) {
            currentAlgorithm.initialize(currentProblem);
        }
        
        // Perform one step
        currentAlgorithm.step();
        visualizationPanel.repaint();
    }
    
    @Override
    public void onAlgorithmChanged(Algorithm algorithm) {
        onPause();
        currentAlgorithm = algorithm;
        visualizationPanel.setAlgorithm(algorithm);
        
        if (currentProblem != null) {
            currentAlgorithm.initialize(currentProblem);
        }
        
        visualizationPanel.repaint();
    }
    
    @Override
    public void onProblemChanged(Problem problem) {
        onPause();
        currentProblem = problem;
        visualizationPanel.setProblem(problem);
        
        if (currentAlgorithm != null) {
            currentAlgorithm.initialize(currentProblem);
        }
        
        visualizationPanel.repaint();
    }
    
    @Override
    public void onSpeedChanged(int speed) {
        int delay = 101 - speed;
        timer.setDelay(delay);
    }
    
    @Override
    public void onHeatmapToggled(boolean show) {
        visualizationPanel.setShowHeatmap(show);
    }
    
    private void step() {
        if (currentAlgorithm != null) {
            boolean continuing = currentAlgorithm.step();
            visualizationPanel.repaint();
            
            // Stop if algorithm is done
            if (!continuing) {
                onPause();
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}