package projet_metaheuristique_P1;

import java.util.List;

public class AStarHeuristic {
	
	public static double g(AStarAlgo.State state) {
        return state.getTotalValue();
    }

    public static double h(AStarAlgo.State state, List<AStarAlgo.Item> remainingItems, List<Integer> remainingCapacities, int totalCapacity) {
        double maxValueDensity = 0.0;
        for (AStarAlgo.Item item : remainingItems) {
            double valueDensity = (double) item.value / item.weight;
            if (valueDensity > maxValueDensity) {
                maxValueDensity = valueDensity;
            }
        }

        double minRemainingCapacityRatio = Double.MAX_VALUE;
        for (int capacity : remainingCapacities) {
            double remainingCapacityRatio = (double) capacity / totalCapacity;
            if (remainingCapacityRatio < minRemainingCapacityRatio) {
                minRemainingCapacityRatio = remainingCapacityRatio;
            }
        }

        return (maxValueDensity * minRemainingCapacityRatio);
    }
}
