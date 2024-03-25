package projet_metaheuristique_P1;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;

public class KnapsackInterface extends JFrame {
    private static final long serialVersionUID = 1L;
    private JComboBox<String> algorithmComboBox;
    private JTextField numberOfSacksField;
    private List<JTextField> capacityFields;
    private JTextArea metricsArea;
    private JTextArea resultsArea;
    private List<Integer> weightsFromFile;
    private List<Integer> valuesFromFile;


    public KnapsackInterface() {
    	
    	
        setTitle("Knapsack Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Left Panel
        JPanel leftPanel = new JPanel(new BorderLayout());

        // Upper Left Panel
        JPanel upperLeftPanel = new JPanel(new GridLayout(4, 2));
        upperLeftPanel.setBorder(BorderFactory.createTitledBorder("Parameters"));

        JLabel algorithmLabel = new JLabel("Algorithm:");
        algorithmComboBox = new JComboBox<>(new String[]{"DFS", "BFS", "A*"});
        JLabel numberOfSacksLabel = new JLabel("Number of Sacks:");
        numberOfSacksField = new JTextField();
        JLabel capacityLabel = new JLabel("Capacity:");
        capacityFields = new ArrayList<>();

        upperLeftPanel.add(algorithmLabel);
        upperLeftPanel.add(algorithmComboBox);
        upperLeftPanel.add(numberOfSacksLabel);
        upperLeftPanel.add(numberOfSacksField);
        
        // Add capacity fields dynamically based on the number of sacks
        numberOfSacksField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCapacityFields();
            }
        });
        upperLeftPanel.add(capacityLabel);
        for (int i = 0; i < 10; i++) {
            JTextField capacityField = new JTextField();
            capacityFields.add(capacityField);
            upperLeftPanel.add(capacityField);
            capacityField.setVisible(false);
        }

        leftPanel.add(upperLeftPanel, BorderLayout.NORTH);

        // Lower Left Panel
        JPanel lowerLeftPanel = new JPanel(new BorderLayout());
        lowerLeftPanel.setBorder(BorderFactory.createTitledBorder("Metrics"));
        metricsArea = new JTextArea();
        metricsArea.setEditable(false);
        lowerLeftPanel.add(new JScrollPane(metricsArea));

        leftPanel.add(lowerLeftPanel, BorderLayout.CENTER);

        // Right Panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Results"));
        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        rightPanel.add(new JScrollPane(resultsArea));

        // Add panels to the main frame
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
        
        
        // Panel for buttons
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearResultsAndMetrics();
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Align buttons to the right
        JButton openButton = new JButton("Open");
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle opening file explorer here
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
                fileChooser.setFileFilter(filter);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    // Load and validate the CSV file
                    boolean valid = loadAndValidateCSV(selectedFile);
                    if (valid) {
                        // If CSV file is valid, display success message or perform any other action
                        JOptionPane.showMessageDialog(null, "CSV file loaded successfully!");
                    } else {
                        // If CSV file is invalid, display error message or perform any other action
                        JOptionPane.showMessageDialog(null, "Invalid CSV file format!");
                    }
                }
            }
        });
        buttonPanel.add(openButton);
        buttonPanel.add(clearButton);
        // Run Algorithm Button (unchanged)
        JButton runButton = new JButton("Run Algorithm");
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runAlgorithm();
            }
        });
        buttonPanel.add(runButton); // Add the runButton to the button panel

        // Add button panel to the main frame
        add(buttonPanel, BorderLayout.SOUTH);

        // Set size and visibility
        setSize(800, 600);
        setVisible(true);
    }
    private void clearResultsAndMetrics() {
        resultsArea.setText("");
        metricsArea.setText("");
    }

    private boolean loadAndValidateCSV(File file) {
        weightsFromFile = new ArrayList<>();
        valuesFromFile = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 2) {
                    return false; // Invalid format
                }
                try {
                    int weight = Integer.parseInt(parts[0]);
                    int value = Integer.parseInt(parts[1]);
                    weightsFromFile.add(weight);
                    valuesFromFile.add(value);
                } catch (NumberFormatException e) {
                    return false; // Unable to parse weight or value
                }
            }
            return true; // CSV file is valid
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Error reading file
        }
    }

    private void runAlgorithm() {
    	Font font = new Font("Arial", Font.BOLD, 16); // Example: Arial, bold, size 16
    	resultsArea.setFont(font);

    	// Change text color
    	resultsArea.setForeground(Color.RED);
        String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
        
        String numberOfSacksText = numberOfSacksField.getText();
        if (numberOfSacksText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the number of sacks.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int numberOfSacks; 
        try {
            numberOfSacks = Integer.parseInt(numberOfSacksText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Number of sacks must be a valid integer.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (numberOfSacks <= 0) {
            JOptionPane.showMessageDialog(this, "Number of sacks must be a positive integer.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
     // Check if the capacity fields are empty or contain invalid input
        for (int i = 0; i < numberOfSacks; i++) {
            JTextField capacityField = capacityFields.get(i);
            String capacityText = capacityField.getText();
            if (capacityText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the capacity for sack " + (i + 1) + ".", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Check if the entered capacity is a valid integer
            try {
                int capacity = Integer.parseInt(capacityText);
                // Check if the entered capacity is non-negative
                if (capacity < 0) {
                    JOptionPane.showMessageDialog(this, "Capacity for sack " + (i + 1) + " must be a non-negative integer.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Capacity for sack " + (i + 1) + " must be a valid integer.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // Prepare items
        int[] weights = {5, 10, 30}; // Example weights
        int[] values = {60, 100, 120}; // Example values
        List<MultipleKnapsack.Item> items = new ArrayList<>();
        for (int i = 0; i < weights.length; i++) {
            items.add(new MultipleKnapsack.Item(weights[i], values[i]));
        }

        // Retrieve capacities
        List<Integer> capacities = new ArrayList<>();
        for (int i = 0; i < numberOfSacks; i++) {
            JTextField capacityField = capacityFields.get(i);
            int capacity = Integer.parseInt(capacityField.getText());
            capacities.add(capacity);
        }

        // Call the appropriate algorithm method
        
        switch (selectedAlgorithm) {
            case "DFS":
            	
                
            	
			// Call the dfs algorithm method
            	resultsArea.append("DFS Algorithm Results(All Nodes):\n\n");
            	long startTime = System.currentTimeMillis();
                MultipleKnapsack.dfs(capacities, items, resultsArea, metricsArea);
                long endTime = System.currentTimeMillis();
                long durationMillis = endTime - startTime;

             // Convert to seconds
                double durationSeconds = durationMillis / 1000.0;
                metricsArea.append("DFS Algorithm Duration: " + durationMillis + " milliseconds (" + durationSeconds + " seconds)\n");
                
                break;
            case "BFS":
                // Call BFS algorithm method
                break;
            case "A*":
                // Call A* algorithm method
                break;
        }

        // Update metrics (if any)
        // metricsArea.setText("Metrics: ...");
    }

    private void updateCapacityFields() {
        // Update the number of capacity fields based on the selected number of sacks
        int numberOfSacks = Integer.parseInt(numberOfSacksField.getText());
        for (int i = 0; i < capacityFields.size(); i++) {
            JTextField capacityField = capacityFields.get(i);
            capacityField.setVisible(i < numberOfSacks);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new KnapsackInterface();
            }
        });
    }
}
