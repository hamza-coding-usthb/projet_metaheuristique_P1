package projet_metaheuristique_P1;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import projet_metaheuristique_P1.AStarAlgo.Item;
import projet_metaheuristique_P1.AStarAlgo.State;

public class HeuristicBIS {
	
	static class Sack {
	    private int capacity;
	    private List<Item> content;
	    private int totalValue;
	    private int totalWeight;

	    public int calculateTotalValue() {
	        totalValue = 0;
	        for (Item item : content) {
	            totalValue += item.getValue();
	        }
	        return totalValue;
	    }

	    public int calculateTotalWeight() {
	        totalWeight = 0;
	        for (Item item : content) {
	            totalWeight += item.getWeight();
	        }
	        return totalWeight;
	    }

	    public int calculateRemainingCapacity() {
	        return capacity - calculateTotalWeight();
	    }

	    public void setCapacity(int capa) {
	        capacity = capa;
	    }

	    public void setContent(List<Item> c) {
	        content = c;
	    }
	    public List<Item> getContent() {
	        return content;
	    }
	    public int getCapacity() {
	        return capacity;
	    }

	    // Add getters for totalValue, totalWeight, and capacity if needed
	}


	public static double g(State state) {
	    double totalValue = 0;
	    List<List<Item>> sackContents = state.getSacks();

	    for (List<Item> sackItems : sackContents) {
	        for (Item item : sackItems) {
	            totalValue += item.getValue();
	        }
	    }

	    return totalValue;
	}

	public static double h(State state, List<Item> items, List<Integer> capacities) {
	    double remainingValue = 0;
	    for (Item item : items) {
	        remainingValue += item.getValue();
	    }

	    int remainingCapacity = 0;
	    for (int capacity : capacities) {
	        remainingCapacity += capacity;
	    }

	    double maxValue = 0;
	    List<Item> sortedItems = sortItemsByValue(items);
	    List<List<Item>> sackContents = state.getSacks(); // Retrieve the sacks from the State object
	    for (int i = 0; i < sackContents.size(); i++) {
	        int sackCapacity = capacities.get(i);
	        int remainingSpace = sackCapacity - sackContents.get(i).stream().mapToInt(Item::getWeight).sum();
	        for (int j = 0; j < sortedItems.size(); j++) {
	            Item item = sortedItems.get(j);
	            if (item.getWeight() <= remainingSpace) {
	                maxValue += item.getValue();
	                remainingSpace -= item.getWeight();
	            } else {
	                double fraction = (double) remainingSpace / item.getWeight();
	                maxValue += fraction * item.getValue();
	                break;
	            }
	        }
	    }

	    return Math.max(remainingValue, maxValue);
	}


	private static List<Item> sortItemsByValue(List<Item> items) {
	    List<Item> sortedItems = new ArrayList<>(items);
	    sortedItems.sort(Comparator.comparingInt(Item::getValue).reversed());
	    return sortedItems;
	}



    
}

