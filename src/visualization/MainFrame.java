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
        if (visualizationPanel != null) {
            visualizationPanel.repaint();
        }
    }
    
    @Override
    public void onStep() {
        step();
    }
    
    @Override
    public void onAlgorithmChanged(Algorithm algorithm) {
        visualizationPanel.setAlgorithm(algorithm);
        visualizationPanel.repaint();
    }
    
    @Override
    public void onProblemChanged(Problem problem) {
        visualizationPanel.setProblem(problem);
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
        visualizationPanel.repaint();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}