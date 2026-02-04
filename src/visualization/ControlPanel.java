package visualization;

import core.Algorithm;
import core.Problem;
import algorithms.*;
import problems.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ControlPanel extends JPanel {
    private JComboBox<String> algorithmCombo;
    private JComboBox<String> problemCombo;
    private JButton startButton;
    private JButton pauseButton;
    private JButton resetButton;
    private JButton stepButton;
    private JSlider speedSlider;
    private JCheckBox heatmapCheckbox;
    private JLabel speedLabel;
    
    private Algorithm currentAlgorithm;
    private Problem currentProblem;
    private ControlListener listener;
    
    public interface ControlListener {
        void onStart();
        void onPause();
        void onReset();
        void onStep();
        void onAlgorithmChanged(Algorithm algorithm);
        void onProblemChanged(Problem problem);
        void onSpeedChanged(int speed);
        void onHeatmapToggled(boolean show);
    }
    
    public ControlPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(250, 800));
        setBackground(new Color(30, 30, 30));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        createComponents();
    }
    
    private void createComponents() {
        // Title
        JLabel titleLabel = new JLabel("Swarm Visualizer");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Algorithm selection
        add(createLabel("Algorithm:"));
        String[] algorithms = {
            "Random Search",
            "Hill Climber",
            "Simulated Annealing",
            "Stochastic Local Search",
            "Particle Swarm Optimization",
            "Genetic Algorithm",
            "Differential Evolution",
            "Ant Colony Optimization"
        };
        algorithmCombo = createComboBox(algorithms);
        algorithmCombo.addActionListener(e -> updateAlgorithm());
        add(algorithmCombo);
        add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Problem selection
        add(createLabel("Problem:"));
        String[] problems = {
            "Sphere",
            "Rastrigin",
            "Ackley"
        };
        problemCombo = createComboBox(problems);
        problemCombo.addActionListener(e -> updateProblem());
        add(problemCombo);
        add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Control buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2, 5, 5));
        buttonPanel.setMaximumSize(new Dimension(230, 80));
        buttonPanel.setOpaque(false);
        
        startButton = createButton("Start");
        startButton.addActionListener(e -> {
            if (listener != null) listener.onStart();
        });
        
        pauseButton = createButton("Pause");
        pauseButton.addActionListener(e -> {
            if (listener != null) listener.onPause();
        });
        pauseButton.setEnabled(false);
        
        stepButton = createButton("Step");
        stepButton.addActionListener(e -> {
            if (listener != null) listener.onStep();
        });
        
        resetButton = createButton("Reset");
        resetButton.addActionListener(e -> {
            if (listener != null) listener.onReset();
        });
        
        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(stepButton);
        buttonPanel.add(resetButton);
        add(buttonPanel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Speed control
        add(createLabel("Speed:"));
        speedSlider = new JSlider(1, 100, 50);
        speedSlider.setOpaque(false);
        speedSlider.setForeground(Color.WHITE);
        speedSlider.addChangeListener(e -> {
            if (listener != null) listener.onSpeedChanged(speedSlider.getValue());
            speedLabel.setText("Delay: " + (101 - speedSlider.getValue()) + " ms");
        });
        add(speedSlider);
        
        speedLabel = createLabel("Delay: 51 ms");
        add(speedLabel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Options
        add(createLabel("Options:"));
        heatmapCheckbox = new JCheckBox("Show Heatmap");
        heatmapCheckbox.setSelected(true);
        heatmapCheckbox.setForeground(Color.WHITE);
        heatmapCheckbox.setOpaque(false);
        heatmapCheckbox.addActionListener(e -> {
            if (listener != null) listener.onHeatmapToggled(heatmapCheckbox.isSelected());
        });
        add(heatmapCheckbox);
        
        add(Box.createVerticalGlue());
        
        // Info
        JTextArea infoArea = new JTextArea();
        infoArea.setText("Controls:\n\n" +
                        "• Select algorithm and problem\n" +
                        "• Click Start to run\n" +
                        "• Use Step for manual control\n" +
                        "• Adjust speed with slider\n\n" +
                        "Yellow dot = Best solution\n" +
                        "Cyan dots = Current population");
        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setForeground(Color.LIGHT_GRAY);
        infoArea.setBackground(new Color(20, 20, 20));
        infoArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(infoArea);
        
        // Initialize with defaults
        updateAlgorithm();
        updateProblem();
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    private JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setMaximumSize(new Dimension(230, 30));
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        return combo;
    }
    
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        return button;
    }
    
    private void updateAlgorithm() {
        String selected = (String) algorithmCombo.getSelectedItem();
        
        switch (selected) {
            case "Random Search":
                currentAlgorithm = new RandomSearch(1000, 30);
                break;
            case "Hill Climber":
                currentAlgorithm = new HillClimber(1000, 1.0);
                break;
            case "Simulated Annealing":
                currentAlgorithm = new SimulatedAnnealing(1000, 100.0, 0.99, 1.0);
                break;
            case "Stochastic Local Search":
                currentAlgorithm = new StochasticLocalSearch(1000, 10, 1.0);
                break;
            case "Particle Swarm Optimization":
                currentAlgorithm = new ParticleSwarmOptimization(1000, 30);
                break;
            case "Genetic Algorithm":
                currentAlgorithm = new GeneticAlgorithm(1000, 40);
                break;
            case "Differential Evolution":
                currentAlgorithm = new DifferentialEvolution(1000, 40);
                break;
            case "Ant Colony Optimization":
                currentAlgorithm = new AntColonyOptimization(1000, 30);
                break;
        }
        
        if (listener != null) {
            listener.onAlgorithmChanged(currentAlgorithm);
        }
    }
    
    private void updateProblem() {
        String selected = (String) problemCombo.getSelectedItem();
        
        switch (selected) {
            case "Sphere":
                currentProblem = new Sphere(2);
                break;
            case "Rastrigin":
                currentProblem = new Rastrigin(2);
                break;
            case "Ackley":
                currentProblem = new Ackley(2);
                break;
        }
        
        if (listener != null) {
            listener.onProblemChanged(currentProblem);
        }
    }
    
    public void setControlListener(ControlListener listener) {
        this.listener = listener;
    }
    
    public void setRunning(boolean running) {
        startButton.setEnabled(!running);
        pauseButton.setEnabled(running);
    }
}