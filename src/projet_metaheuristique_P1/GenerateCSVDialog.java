package projet_metaheuristique_P1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GenerateCSVDialog extends JDialog {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField numberOfFilesField;
	private JTextField numberOfItemsField;
	private JTextField incrementItemField;
    private JTextField maxWeightField;
    private JTextField minWeightField;
    private JTextField maxValueField;
    private JButton generateButton;
    
    private int numberOfFiles;
    private int numberOfItems;
    private int incrementItem;
    private int maxWeight;
    private int minWeight;
    private int maxValue;

    public GenerateCSVDialog(JFrame parent) {
        super(parent, "Generate CSV", true);
        setSize(300, 200);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(7, 3));
        
        JLabel numberOfFilesLabel = new JLabel("Number of files:");
        numberOfFilesField = new JTextField();
        
        JLabel numberOfItemsLabel = new JLabel("Number of Items:");
        numberOfItemsField = new JTextField();
        
        JLabel incrementItemLabel = new JLabel("Increrement of Items:");
        incrementItemField = new JTextField();
        JLabel maxWeightLabel = new JLabel("Max Weight:");
        maxWeightField = new JTextField();
        JLabel minWeightLabel = new JLabel("Min Weight:");
        minWeightField = new JTextField();
        
        JLabel maxValueLabel = new JLabel("Max Value:");
        maxValueField = new JTextField();

        
        panel.add(numberOfFilesLabel);
        panel.add(numberOfFilesField);
        panel.add(numberOfItemsLabel);
        panel.add(numberOfItemsField);
        panel.add(incrementItemLabel);
        panel.add(incrementItemField);
        panel.add(maxWeightLabel);
        panel.add(maxWeightField);
        panel.add(minWeightLabel);
        panel.add(minWeightField);
        panel.add(maxValueLabel);
        panel.add(maxValueField);

        generateButton = new JButton("Generate");
        generateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleGenerateButtonClick();
            }
        });

        panel.add(generateButton);

        getContentPane().add(panel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void handleGenerateButtonClick() {
        try {
        	numberOfFiles=Integer.parseInt(numberOfFilesField.getText());
            numberOfItems = Integer.parseInt(numberOfItemsField.getText());
            incrementItem=Integer.parseInt(incrementItemField.getText());
            maxWeight = Integer.parseInt(maxWeightField.getText());
            minWeight = Integer.parseInt(maxWeightField.getText());
            maxValue = Integer.parseInt(maxValueField.getText());
            dispose(); // Close the dialog after retrieving input
            setVisible(false); // Hide the dialog after closing
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid integer values.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    public int getNumberOfFiles() {
        return numberOfFiles;
    }
    public int getNumberOfItems() {
        return numberOfItems;
    }
    public int getMaxValue() {
        return maxValue;
    }

    public int getMaxWeight() {
        return maxWeight;
    }
    public int getMinWeight() {
        return minWeight;
    }

    public int getIncrement() {
        return incrementItem;
    }

    
}
