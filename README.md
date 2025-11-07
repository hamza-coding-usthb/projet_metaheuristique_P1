# Knapsack Problem Solver Suite

This project provides a Java-based desktop application with a graphical user interface (GUI) for solving the Multiple Knapsack Problem (MKP). It allows users to test, visualize, and compare the performance of various search and metaheuristic algorithms.

## Table of Contents

- [Features](#features)
- [Algorithms Implemented](#algorithms-implemented)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [How to Use](#how-to-use)
  - [1. Generate or Load Data](#1-generate-or-load-data)
  - [2. Configure Parameters](#2-configure-parameters)
  - [3. Run an Algorithm](#3-run-an-algorithm)
  - [4. Analyze Results](#4-analyze-results)
  - [5. Visualize](#5-visualize)
- [Project Structure](#project-structure)
- [Dependencies](#dependencies)

## Features

-   **Graphical User Interface**: An intuitive Swing-based UI to configure and run experiments.
-   **Multiple Algorithm Support**: Implements and compares several algorithms for the MKP.
-   **Data Generation**: Includes a tool to generate CSV files with random item weights and values for testing.
-   **Data Loading**: Supports loading multiple item sets from CSV files to run batch tests.
-   **Detailed Metrics**: Gathers and displays key performance indicators for each algorithm run, such as execution time, nodes traversed, search depth, and solution quality (satisfaction rate).
-   **Results Export**: Saves detailed run data and summary statistics to CSV files for further analysis.
-   **Visualization Tools**:
    -   Generates search tree visualizations using Graphviz.
    -   Creates statistical performance curves (e.g., execution time vs. number of items) using JFreeChart.

## Algorithms Implemented

The application allows you to solve the Multiple Knapsack Problem using the following algorithms:

1.  **Depth-First Search (DFS)**: A classic uninformed search algorithm that explores the search space by going as deep as possible along each branch before backtracking.
2.  **Breadth-First Search (BFS)**: Another uninformed search algorithm that explores the search space layer by layer, guaranteeing finding the shallowest solution.
3.  **A\* Search**: An informed search algorithm that uses a heuristic function to guide its path. The heuristic estimates the potential future value, prioritizing more promising states to find the optimal solution more efficiently than uninformed searches.
4.  **Genetic Algorithm (GA)**: A metaheuristic inspired by natural selection. It evolves a population of potential solutions over several generations using operators like rank-based selection, uniform crossover, and mutation to find high-quality solutions.
5.  **Bee Swarm Optimization (BSO)**: A metaheuristic that mimics the foraging behavior of honey bees. It uses a population of "bees" to explore the solution space through a combination of global and local search, referencing a "taboo" list to ensure diversity.

## Getting Started

### Prerequisites

-   **Java Development Kit (JDK)**: Version 21 or higher.
-   **Apache Maven**: To manage dependencies and build the project.
-   **Graphviz**: Required for generating and viewing the search tree graphs. Make sure the `dot` command is available in your system's PATH.

### Installation

1.  **Clone the repository:**
    ```sh
    git clone <your-repository-url>
    cd projet_metaheuristique_P1
    ```

2.  **Build the project with Maven:**
    This command will download the required dependencies (GraphStream for graphs, JFreeChart for curves) and compile the source code.
    ```sh
    mvn clean install
    ```

3.  **Run the application:**
    Execute the main class `KnapsackInterface`.
    ```sh
    java -cp target/projet_metaheuristique_P1-0.0.1-SNAPSHOT.jar projet_metaheuristique_P1.KnapsackInterface
    ```

## How to Use

The main window is divided into a "Parameters" panel on the left, a "Results" panel on the right, and control buttons at the bottom.

### 1. Generate or Load Data

-   **Generate Data**: Click the **Generate CSV** button. A dialog will appear where you can specify the number of files to create, the initial number of items, how many items to add for each subsequent file, and the min/max range for item weights and values. This is useful for creating a scaled test set.
-   **Load Data**: Click the **Open** button. Select one or more `.csv` files. The application expects each line in the CSV to contain two integers separated by a comma: `weight,value`. The files will be processed in numerical order based on their filenames (e.g., `sample_1.csv`, `sample_2.csv`).

### 2. Configure Parameters

-   **Algorithm**: Select the desired algorithm from the dropdown menu (DFS, BFS, A\*, Genetic, BSO).
-   **Max Depth**: For tree-based searches (DFS, BFS, A\*), you can set a maximum depth to limit the search and prevent excessively long runtimes.
-   **Number of Sacks**: Enter the number of knapsacks available.
-   **Algorithm-Specific Parameters**: When you click "Run Algorithm", a dialog will appear asking for the sack capacities and any parameters specific to the chosen algorithm (e.g., population size for GA, number of bees for BSO).

### 3. Run an Algorithm

-   Click the **Run Algorithm** button.
-   Enter the required capacities and parameters in the dialog that appears.
-   The algorithm will run on the loaded dataset(s). The `Results` area will show the step-by-step exploration (for search algorithms) or generation evolution (for metaheuristics), while the `Metrics` area will display performance data.

### 4. Analyze Results

-   **Results Area**: Shows the contents of the sacks and the total value for states explored during the run. The best solution found is displayed at the end.
-   **Metrics Area**: Displays the execution time, number of nodes traversed, final search depth, and other relevant metrics.
-   **CSV Output**: The application automatically generates CSV files in the project's root directory (e.g., `DFSData.csv`, `GAData.csv`) containing detailed metrics from each run. This data can be used for external analysis and plotting.

### 5. Visualize

-   **Generate Graph**: After running a tree-based search (DFS, BFS, A\*), a `.dot` file (e.g., `search_treeASTAR.dot`) is created. Click the **Generate Graph** button, select the `.dot` file, and the application will use Graphviz to render a PDF of the search tree and open it.
-   **Generate Curve**: To plot performance, click **Generate Curve**. You will be prompted to select two CSV files: the first containing raw data (e.g., `DFSData.csv`) and the second containing summary statistics (e.g., `DFSMetrics.csv`). A chart will be displayed showing the trend (e.g., time vs. items) along with the calculated average and standard deviation.

## Project Structure

The project is organized into several classes, each with a specific role:

-   `KnapsackInterface.java`: The main class, responsible for the entire GUI and coordinating the application flow.
-   **Algorithm Classes**:
    -   `MultipleKnapsack.java` (DFS)
    -   `MultipleKnapsackBFS.java` (BFS)
    -   `AStarAlgo.java` (A\*)
    -   `AStarHeuristic.java` (Heuristic for A\*)
    -   `GeneticAlgo.java` (Genetic Algorithm)
    -   `BeeSwarmOptimizationMKP2.java` (Bee Swarm Optimization)
-   **Data and State Classes**: Each algorithm uses nested classes like `Item` and `State` (or `InitialSolution` for BSO) to model the problem's components.
-   **Dialog Classes**:
    -   `CapacityInputDialog.java`, `CapacityInputDialogGA.java`, `CapacityInputDialogBSO.java`: Custom dialogs for entering algorithm parameters.
    -   `GenerateCSVDialog.java`: UI for the CSV data generator.
-   **Utility Classes**:
    -   `CSVGenerator.java`: Logic for generating random item data.
    -   `DataSaved.java`, `DataSavedMeta.java`: Data structures for storing and saving metrics.
    -   `DotFileGenerator*.java`: Classes that create `.dot` files from the search tree.
    -   `GraphvizExecutor.java`: A wrapper to execute Graphviz command-line tools.
    -   `StatCurve.java`: Uses JFreeChart to generate and display statistical curves.

## Dependencies

This project relies on the following external libraries, which are managed by Maven via the `pom.xml` file:

-   **GraphStream (gs-core)**: For creating, handling, and visualizing graph structures.
-   **JFreeChart**: For creating a wide variety of professional-looking charts and plots.
