package projet_metaheuristique_P1;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;



import javax.swing.JTextArea;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.time.Duration; // Import Duration for time tracking
import java.time.Instant;

public class MultipleKnapsack {

	static class Item {
	    private static int itemCount = 0; // Static counter to generate unique identifiers
	    private int id;
	    private int weight;
	    private int value;

	    public Item(int weight, int value) {
	        this.id = itemCount++; // Assign unique identifier and then increment the counter
	        this.weight = weight;
	        this.value = value;
	    }

	    public int getId() {
	        return id;
	    }

	    @Override
	    public String toString() {
	        return "(" + id + ", " + weight + ", " + value + ")";
	    }
	    public static void resetItemCount() {
	        itemCount = 0;
	    }
	}


    static class State {
    	static int stateCount = 0;
        List<List<Item>> sacks;
        int id;
        int totalWeight;
        int itemIndex;
        int nodeNumber;
        boolean visited;
        Duration visitDuration; // Add timestamp field
        List<State> children; // Add a list to hold child states

        public State(List<List<Item>> sacks, int totalWeight, int itemIndex) {
            this.sacks = sacks;
            this.id = stateCount++;
            this.totalWeight = totalWeight;
            this.itemIndex = itemIndex;
            this.children = new ArrayList<>(); // Initialize the list
            this.visited = false;
            this.visitDuration = null; // Initialize visit duration
        }

        public int getTotalValue() {
            int totalValue = 0;
            for (List<Item> sack : sacks) {
                totalValue += calculateTotalValue(sack);
            }
            return totalValue;
        }

        // Method to add child states
        public void addChild(State child) {
            children.add(child);
        }

        // Method to retrieve child states
        public List<State> getChildren() {
            return children;
        }
        public int getId() {
            return id;
        }
        public int getNodeNumber() {
            return nodeNumber;
        }
        public void setVisited(boolean bol) {
        	this.visited = bol;
        }
        public boolean getVisited() {
        	return visited;
        }
        public void setVisitDuration(Duration visitDuration) {
            this.visitDuration = visitDuration;
        }

        // Method to get visit duration
        public Duration getVisitDuration() {
            return visitDuration;
        }
        public boolean containsItem(int itemId) {
            for (List<Item> sack : sacks) {
                for (Item item : sack) {
                    if (item.getId() == itemId) {
                        return true;
                    }
                }
            }
            return false;
        }
        public String getItemsAsString() {
            StringBuilder sb = new StringBuilder();
            for (List<Item> sack : sacks) {
                sb.append("[");
                for (Item item : sack) {
                    sb.append(item.toString()).append(", ");
                }
                sb.append("], ");
            }
            return sb.toString();
        }
    }


    public static DataSaved dfs(List<Integer> capacities, List<Item> items, JTextArea resultsArea, JTextArea metricsArea, int maximumDepth, Graph graph) {

        int numSacks = capacities.size();
        int numItems = items.size();
        DataSaved data = new DataSaved();
        boolean insufficientCapacity = false;
        boolean maximumDepthReached = false;
        boolean targetValueReached = false;
        
        int totalWeightOfItems = calculateTotalWeightOfItems(items);
        int totalCapacityOfSacks = calculateTotalCapacityOfSacks(capacities);
        insufficientCapacity = totalWeightOfItems > totalCapacityOfSacks;
        
     
        
        
        
        int targetVal = calculateMaxVal(items);
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
        
        int maxDepth = 0; // Initialize max depth
        List<State> allStates = new ArrayList<>();
        List<State> allSacks = new ArrayList<>();
        
        long startTime = System.currentTimeMillis(); //measure time begins
        Instant startingTime = Instant.now();
        
        //Beginning of dfs loop
        while (!stack.isEmpty() && j<=65000) {
            State state = stack.pop();
           
            
            Instant stateStartTime = Instant.now();
            
            // Calculate visit duration for state
            Duration visitDuration = Duration.between(startingTime, stateStartTime);
            
            state.setVisitDuration(visitDuration); // Set visit duration
            
            allStates.add(state);
           
            
            
            List<List<Item>> sacks = state.sacks;
            
            int totalWeight = state.totalWeight;
            int itemIndex = state.itemIndex;
            
            targetValueReached = targetVal <= calculateCurrentVal(sacks);
            
            // Update max depth
            maxDepth = Math.max(maxDepth, itemIndex);
           
            maximumDepthReached = itemIndex > maximumDepth;
            
            
            if(maximumDepthReached) {
            	
            	continue;
            }
            
            
            

            int totalValue = 0;
                    
           /* 
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
            */
            
            state.nodeNumber = j;
            state.setVisited(true);
            
            j++;
           
            
            
            if(targetValueReached) {
            	
            	data.setSatisfiable(true);
            	resultsArea.append("Sacks When Target Reached:\n");
                int sackNumber = 1;
                for (List<Item> sack : sacks) {
                    resultsArea.append("Sack " + sackNumber + " " + sack.toString() + "\n");
                    totalValue += calculateTotalValue(sack);
                    sackNumber++;
                }
                resultsArea.append("Total Value: " + totalValue + "Duration to reach this state: "+ state.getVisitDuration() +"\n\n");
            	
            	break;
            }
            
            
            State newSack = new State(sacks, totalWeight, itemIndex);
            newSack.setVisitDuration(visitDuration);
            allSacks.add(newSack);
            
            if (!insufficientCapacity) {

                if (itemIndex >= items.size()) {
                    for (List<Item> sack : sacks) {
                        totalValue += calculateTotalValue(sack);
                    }
                    printSacks(sacks, resultsArea);
                    continue;
                }
            }
            
         
            
            

            ;
            for (int i = 0; i < numSacks; i++) {
            	
                
                List<List<Item>> parentSacks = copySacks(sacks);
                totalWeight = state.totalWeight;

                for (int j1 = 0; j1 < numItems; j1++) {
                	
                	
                    if (canFit(capacities.get(i), parentSacks.get(i), items.get(j1)) && !state.containsItem(j1)) {
                    	List<List<Item>> newSacks = copySacks(sacks);
                        newSacks.get(i).add(items.get(j1));
                        totalWeight += items.get(j1).weight;
                        
                        State childState = new State(newSacks, totalWeight, itemIndex + 1);
                       if(!isSimilarState(childState, allSacks)) {
                        state.addChild(childState);
                        stack.push(childState);
                        }
                    }
                    
                }

                // Create a new child state with the updated sacks
                
            }
            
            allSacks.sort(Comparator.comparingInt(State::getTotalValue).reversed());
            
         // Create a unique node ID based on the current state
            

            // Add the current state as a node to the graph
            
           
            

            
        }
        
        
        Item.resetItemCount();
        long endTime = System.currentTimeMillis();
        long durationMillis = endTime - startTime;
        double durationSeconds = durationMillis / 1000.0;
        metricsArea.append("DFS Algorithm Duration: " + durationMillis + " milliseconds (" + durationSeconds + " seconds)\n");

     // Build the graph based on the parent-child relationships
        for (State parentState : allStates) {
            for (State childState : parentState.getChildren()) {    	
                // Add nodes if they don't exist already
                if (graph.getNode("Node_" + parentState.getId()) == null) {
                    Node parentNode = graph.addNode("Node_" + parentState.getId());
                    // Set the label for the parent node with the total value
                    parentNode.setAttribute("label", "Depth: " + parentState.itemIndex + "\nTotal Value: " + parentState.getTotalValue());
                }
                if(childState.getVisited()) {
                if (graph.getNode("Node_" + childState.getId()) == null) {
                    Node childNode = graph.addNode("Node_" + childState.getId());
                    // Set the label for the child node with the total value
                    childNode.setAttribute("label", "Depth: " + childState.itemIndex + "\nTotal Value: " + childState.getTotalValue());
                }
            }
                if(childState.getVisited()) {
                // Add edges between parent and child states
                graph.addEdge("Edge_" + parentState.getId() + "_" + childState.getId(),
                              "Node_" + parentState.getId(),
                              "Node_" + childState.getId());
                }
            }
        }
        graph.setAttribute("layout.algorithm", "tree");

        

        
        if (insufficientCapacity || maximumDepthReached || ((maxDepth < numItems)&&(stack.isEmpty())) || j==25000) {
        	if(maximumDepthReached) {
        		resultsArea.append("Maximum depth in the graph search reached. Best result at this depth: \n");
        	} else if(insufficientCapacity || ((maxDepth < numItems)&&(stack.isEmpty())) || j==25000) {
        		resultsArea.append("Best result possible: \n");
        	}
            bestState(allSacks, resultsArea);
        }
        //DotFileGenerator.generateDotFile(allStates, 6000, 6000);

        metricsArea.append("The number of nodes in the search tree: " + j + "\n");
        metricsArea.append("The depth of the search tree: " + maxDepth + "\n");
        data.setnumItems(numItems);
        data.setDuration(durationSeconds);
        data.setMaximumDepth(maxDepth);
        data.setSatRate(calculateCurrentVal(allSacks.get(0).sacks)/targetVal);
        data.setNodesTraversed(j);
        
        return(data);

    }

    private static void bestState(List<State> allSacks, JTextArea resultsArea) {
        if (!allSacks.isEmpty()) {
            State firstState = allSacks.get(0);
            
            resultsArea.append("Best:\n");
            int totalValueFirstState = 0;
            Duration visitDuration = firstState.getVisitDuration();

         // Convert duration to string and remove the "PT" prefix
            	String dur = visitDuration.toString().substring(2);
            for (int i = 0; i < firstState.sacks.size(); i++) {
                List<Item> sack = firstState.sacks.get(i);
                resultsArea.append("Sack " + (i + 1) + ": " + sack.toString() + "\n");
                totalValueFirstState += calculateTotalValue(sack);
            }
            resultsArea.append("Total value of the first state: " + totalValueFirstState + " Duration to reach this state: "+ dur +"\n");
        } else {
            resultsArea.append("The allSacks list is empty.\n");
        }
    }


    private static boolean canFit(int capacity, List<Item> sack, Item item) {
        if (sack == null) return true;
        int sackWeight = calculateSackWeight(sack);
        ;
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
    /*
    private static String getSacksString(List<List<Item>> sacks) {
        StringBuilder sb = new StringBuilder();
        for (List<Item> sack : sacks) {
            sb.append(sack.toString()).append(", ");
        }
        return sb.toString();
    }
    
    */

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

    private static void printSacks(List<List<Item>> sacks, JTextArea resultsArea) {
        resultsArea.append("Sacks: Optimum Result\n");
        int sackNumber = 1;
        int totalValue = 0; // Initialize totalValue
        for (List<Item> sack : sacks) {
            resultsArea.append("Sack " + sackNumber + " " + sack.toString() + "\n");
            totalValue += calculateTotalValue(sack); // Accumulate totalValue
            sackNumber++;
        }

        resultsArea.append("Total Value: " + totalValue + "\n\n");
    }
    private static int calculateMaxVal(List<Item> items) {
    	int maxVal = 0;
    	for(Item item: items) {
    		maxVal += item.value;
    	}
    	return maxVal;
    }
    private static int calculateCurrentVal(List<List<Item>> sacks) {
    	int currentValue = 0;
    	for (List<Item> sack : sacks) {
            
    		currentValue += calculateTotalValue(sack);
            
        }
    	return currentValue;
    }
    public static boolean isSimilarState(State childState, List<State> allStates) {
        for (State state : allStates) {
            if (areStatesSimilar(childState, state)) {
                return true;
            }
        }
        return false;
    }

    private static boolean areStatesSimilar(State state1, State state2) {
        List<List<Item>> sacks1 = state1.sacks;
        List<List<Item>> sacks2 = state2.sacks;

        // Check if the number of sacks is the same
        if (sacks1.size() != sacks2.size()) {
            return false;
        }

        // Check if each sack in state1 has a corresponding similar sack in state2
        for (int i = 0; i < sacks1.size(); i++) {
            List<Item> sack1 = sacks1.get(i);
            List<Item> sack2 = sacks2.get(i);

            // Check if the number of items in the sacks is the same
            if (sack1.size() != sack2.size()) {
                return false;
            }

            // Check if each item in sack1 has a corresponding similar item in sack2
            for (Item item : sack1) {
                boolean foundSimilarItem = false;
                for (Item item2 : sack2) {
                    if (item.getId() == item2.getId()) {
                        foundSimilarItem = true;
                        break;
                    }
                }
                if (!foundSimilarItem) {
                    return false;
                }
            }
        }

        return true;
    }
}
