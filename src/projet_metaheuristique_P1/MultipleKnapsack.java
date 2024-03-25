package projet_metaheuristique_P1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import javax.swing.JTextArea;

public class MultipleKnapsack {

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

    public static void dfs(List<Integer> capacities, List<Item> items, JTextArea resultsArea, JTextArea metricsArea) {
    	
    	int numSacks = capacities.size();
    	boolean insufficientCapacity = false;
        int totalWeightOfItems = calculateTotalWeightOfItems(items);
        int totalCapacityOfSacks = calculateTotalCapacityOfSacks(capacities);
        insufficientCapacity = totalWeightOfItems > totalCapacityOfSacks;
        
        if (insufficientCapacity) {
            resultsArea.append("Total capacity of the sacks is insufficient to carry all the items.\n");
        }

        
        Stack<State> stack = new Stack<>();
        List<List<Item>> initialSacks = new ArrayList<>();
        for (int i = 0; i < numSacks; i++) {
            initialSacks.add(new ArrayList<>());
        }
        stack.push(new State(initialSacks, 0, 0));

        int j = 0;
        List<State> allSacks = new ArrayList<>();
        
        while (!stack.isEmpty()) {
            State state = stack.pop();
            j++;

            List<List<Item>> sacks = state.sacks;

            int totalWeight = state.totalWeight;
            int itemIndex = state.itemIndex;

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
            
            if(!insufficientCapacity) {
            
            if (itemIndex >= items.size()) {
                for (List<Item> sack : sacks) {
                    totalValue += calculateTotalValue(sack);
                }
                printSacks(sacks, resultsArea, totalValue);
                continue;
            }
            }
            	
            	 
            		
            	
          	 
            
            

            boolean canFitAnySack = false;
            for (int i = 0; i < numSacks; i++) {
                if (canFit(capacities.get(i), sacks.get(i), items.get(itemIndex))) {
                    List<List<Item>> newSacks = copySacks(sacks);
                    newSacks.get(i).add(items.get(itemIndex));
                    stack.push(new State(newSacks, totalWeight + items.get(itemIndex).weight, itemIndex + 1));
                    canFitAnySack = true;
                }
            }
            allSacks.sort(Comparator.comparingInt(State::getTotalValue).reversed());
           

            
        }
        if(insufficientCapacity) {
        
        bestState(allSacks, resultsArea);
        }

       

       
        metricsArea.append("The number of nodes in the search tree: " + j + "\n");
        
        
        
    }
    
    private static void bestState(List<State> allSacks, JTextArea resultsArea) {
        if (!allSacks.isEmpty()) {
            State firstState = allSacks.get(0);
            resultsArea.append("Best distribution for this situation is:\n");
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
        return totalCapacity;
    }

    private static void printSacks(List<List<Item>> sacks, JTextArea resultsArea, int totalValue) {
        resultsArea.append("Sacks: Optimum Result\n");
        int sackNumber = 1;
        for (List<Item> sack : sacks) {
            resultsArea.append("Sack " + sackNumber + " " + sack.toString() + "\n");
            totalValue += calculateTotalValue(sack);
            sackNumber++;
        }

        resultsArea.append("Total Value: " + totalValue + "\n\n");
    }
}
