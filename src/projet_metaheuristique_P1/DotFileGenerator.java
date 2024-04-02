package projet_metaheuristique_P1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import projet_metaheuristique_P1.MultipleKnapsack.State;

public class DotFileGenerator {
    public static void generateDotFile(List<State> allStates, int width, int height) {
        try {
            FileWriter writer = new FileWriter("search_tree.dot");

            // Write the DOT file header
            writer.write("digraph G {\n");

            // Write node definitions
            for (State state : allStates) {
                int j = state.getNodeNumber(); // Assuming getNodeNumber() returns the node number
                writer.write("  Node_" + state.getId() + " [label=\"" +
                        "Node Number: " + j + "\\n" +
                        "Depth: " + state.itemIndex + "\\n" +
                        "Total Value: " + state.getTotalValue() + "\\n" +
                        "Items: " + state.getItemsAsString() + "\"];\n");
            }

            // Write edge definitions
            for (State state : allStates) {
                for (State childState : state.getChildren()) {
                    writer.write("  Node_" + state.getId() + " -> Node_" + childState.getId() + ";\n");
                }
            }

            // Write the DOT file footer
            writer.write("}\n");
            writer.close();

            // Generate the PNG image using Graphviz
            generatePngImage("search_tree.dot", width, height);

            System.out.println("DOT file with node numbers generated successfully.");

        } catch (IOException e) {
            System.err.println("Error while generating DOT file: " + e.getMessage());
        }
    }

    private static void generatePngImage(String dotFileName, int width, int height) {
        // Command to generate PNG image with specified dimensions
        String command = "dot -Tpng -Gdpi=300 -Gsize=" + width + "," + height +
                         " -o search_tree.png " + dotFileName;

        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.inheritIO(); // Redirects standard output and error streams to the current JVM's standard output and error streams
            Process process = pb.start();
            process.waitFor(); // Wait for the process to complete
            System.out.println("PNG image generated successfully.");
        } catch (IOException | InterruptedException e) {
            System.err.println("Error while generating PNG image: " + e.getMessage());
        }
    }
}
