package projet_metaheuristique_P1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import javax.swing.JTextArea;

public class MultipleKnapsackBFS {

    static class Item {
        int weight;
        int value;

        public Item(int weight, int value) {
            this.weight = weight;
            this.value = value;
        }

        @Override
        public String toString() {
            return "(" + weight + ", " + value + ")";
        }
    }

    static class State {
        List<List<Item>> sacks;
        int totalWeight;
        int itemIndex;

        public State(List<List<Item>> sacks, int totalWeight, int itemIndex) {
            this.sacks = sacks;
            this.totalWeight = totalWeight;
            this.itemIndex = itemIndex;
        }

        public int getTotalValue() {
            int totalValue = 0;
            for (List<Item> sack : sacks) {
                totalValue += calculateTotalValue(sack);
            }
            return totalValue;
        }
    }

    public static void bfs(List<Integer> capacities, List<Item> items, JTextArea resultsArea, JTextArea metricsArea, int maximumDepth) {

        int numSacks = capacities.size();
        boolean insufficientCapacity = false;
        boolean maximumDepthReached = false;
        int totalWeightOfItems = calculateTotalWeightOfItems(items);
        int totalCapacityOfSacks = calculateTotalCapacityOfSacks(capacities);
        insufficientCapacity = totalWeightOfItems > totalCapacityOfSacks;

        if (insufficientCapacity) {
            resultsArea.append("Total capacity of the sacks is insufficient to carry all the items.\n");
        }

        Queue<State> queue = new LinkedList<>(); // Change to Queue
        List<List<Item>> initialSacks = new ArrayList<>();
        for (int i = 0; i < numSacks; i++) {
            initialSacks.add(new ArrayList<>());
        }
        queue.offer(new State(initialSacks, 0, 0)); // Change to offer

        int j = 0;
        int maxDepth = 0; // Initialize max depth
        List<State> allSacks = new ArrayList<>();

        while (!queue.isEmpty()) {
            State state = queue.poll(); // Change to poll
            j++;

            List<List<Item>> sacks = state.sacks;

            int totalWeight = state.totalWeight;
            int itemIndex = state.itemIndex;

            // Update max depth
            maxDepth = Math.max(maxDepth, itemIndex);
            
            if(maxDepth>maximumDepth) {
                maximumDepthReached = true;
                resultsArea.append("Maximum depth in the graph search reached. Best result at this depth: \n");
                break;
            }

            int totalValue = 0;
            if (itemIndex < items.size()) {
                resultsArea.append("Sacks:\n");
                int sackNumber = 1;
                for (List<Item> sack : sacks) {
                    resultsArea.append("Sack " + sackNumber + " " + sack.toString() + "\n");
                    totalValue += calculateTotalValue(sack);
                    sackNumber++;
                }

                resultsArea.append("Total Value: " + totalValue + "\n\n");
            }
            allSacks.add(new State(sacks, totalWeight, itemIndex));

            if (!insufficientCapacity) {

                if (itemIndex >= items.size()) {
                    for (List<Item> sack : sacks) {
                        totalValue += calculateTotalValue(sack);
                    }
                    printSacks(sacks, resultsArea);
                    continue;
                }
            }

            boolean canFitAnySack = false;
            for (int i = 0; i < numSacks; i++) {
                if (canFit(capacities.get(i), sacks.get(i), items.get(itemIndex))) {
                    List<List<Item>> newSacks = copySacks(sacks);
                    newSacks.get(i).add(items.get(itemIndex));
                    queue.offer(new State(newSacks, totalWeight + items.get(itemIndex).weight, itemIndex + 1)); // Change to offer
                    canFitAnySack = true;
                }
            }
            allSacks.sort(Comparator.comparingInt(State::getTotalValue).reversed());

        }
        if (insufficientCapacity || maximumDepthReached) {

            bestState(allSacks, resultsArea);
        }

        metricsArea.append("The number of nodes in the search tree: " + j + "\n");
        metricsArea.append("The depth of the search tree: " + maxDepth + "\n");
        
        

    }

    private static void bestState(List<State> allSacks, JTextArea resultsArea) {
        if (!allSacks.isEmpty()) {
            State firstState = allSacks.get(0);
            resultsArea.append("Best:\n");
            int totalValueFirstState = 0;
            for (int i = 0; i < firstState.sacks.size(); i++) {
                List<Item> sack = firstState.sacks.get(i);
                resultsArea.append("Sack " + (i + 1) + ": " + sack.toString() + "\n");
                totalValueFirstState += calculateTotalValue(sack);
            }
            resultsArea.append("Total value of the first state: " + totalValueFirstState + "\n");
        } else {
            resultsArea.append("The allSacks list is empty.\n");
        }
    }


    private static boolean canFit(int capacity, List<Item> sack, Item item) {
        if (sack == null) return true;
        int sackWeight = calculateSackWeight(sack);
        return sackWeight + item.weight <= capacity;
    }

    private static int calculateSackWeight(List<Item> sack) {
        int weight = 0;
        for (Item item : sack) {
            weight += item.weight;
        }
        return weight;
    }

    private static int calculateTotalValue(List<Item> sack) {
        int totalValue = 0;
        for (Item item : sack) {
            totalValue += item.value;
            
        }
        return totalValue;
    }

    private static int calculateTotalWeightOfItems(List<Item> items) {
        int totalWeight = 0;
        for (Item item : items) {
            totalWeight += item.weight;
        }
        return totalWeight;
    }

    private static List<List<Item>> copySacks(List<List<Item>> sacks) {
        List<List<Item>> copy = new ArrayList<>();
        for (List<Item> sack : sacks) {
            copy.add(new ArrayList<>(sack));
        }
        return copy;
    }
    
    private static int calculateTotalCapacityOfSacks(List<Integer> capacities) {
        int totalCapacity = 0;
        for (int capacity : capacities) {
            totalCapacity += capacity;
        }
        System.out.println(totalCapacity);
        return totalCapacity;
    }

    private static void printSacks(List<List<Item>> sacks, JTextArea resultsArea) {
        resultsArea.append("Sacks: Optimum Result\n");
        int sackNumber = 1;
        int totalValue = 0;
        // Initialize totalValue
        for (List<Item> sack : sacks) {
            resultsArea.append("Sack " + sackNumber + " " + sack.toString() + "\n");
            totalValue += calculateTotalValue(sack);
            sackNumber++;
        }

        resultsArea.append("Total Value: " + totalValue + "\n\n");
    }
}


