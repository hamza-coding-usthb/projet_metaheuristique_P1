package projet_metaheuristique_P1;

import javax.swing.JOptionPane;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class CSVGenerator {
	
	

    public static void generateCSV(String baseFileName, int itemCount, int maxWeight, int maxValue,int minWeight, int nFiles, int inc) {
        try {
            StringBuilder successMessage = new StringBuilder("CSV files generated successfully:\n");

            // Iterate over the number of files
            for (int fileNumber = 1; fileNumber <= nFiles; fileNumber++) {
                String fileName = baseFileName + "_" + fileNumber + ".csv"; // Append file number to base filename
                FileWriter writer = new FileWriter(fileName);

                // Write CSV header
                // Optional: Add CSV header if needed

                Random random = new Random();

                // Generate random weight and value for each item and write to CSV
                for (int i = 0; i < itemCount + (inc * (fileNumber - 1)); i++) { // Increment item count based on file number
                	int weight = random.nextInt(maxWeight - minWeight + 1) + minWeight;; // Generate random weight between 1 and maxWeight
                    int value = random.nextInt(maxValue) + 1; // Generate random value between 1 and maxValue
                    writer.append(weight + "," + value + "\n");
                }

                writer.flush();
                writer.close();

                successMessage.append(fileName).append("\n");
            }

            // Display success message using a dialog box
            JOptionPane.showMessageDialog(null, successMessage.toString(), "CSV Files Generated", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            // Display error message using a dialog box
            JOptionPane.showMessageDialog(null, "Error while generating CSV files: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

 
}
