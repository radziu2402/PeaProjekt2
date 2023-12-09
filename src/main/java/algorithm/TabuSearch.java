package algorithm;

import data.Matrix;

import java.util.Random;

public class TabuSearch {
    private long bestSolutionTime;
    private final Matrix graph;
    public int[] bestPath;
    public int bestCost = Integer.MAX_VALUE;
    Random rand = new Random();
    private final long timeLimit;
    private final NeighborOperator neighborOperator;

    public TabuSearch(Matrix graph, long timeLimit, int neighbor) {
        if (neighbor == 1) {
            neighborOperator = new SwapOperator();
        } else if (neighbor == 2) {
            neighborOperator = new InsertOperator();
        } else if (neighbor == 3) {
            neighborOperator = new MakeReverseOperator();
        } else {
            throw new IllegalArgumentException("Invalid neighbor value");
        }
        this.graph = graph;
        this.timeLimit = timeLimit;
    }

    public void solve() {
        int[] currentPath;
        int[] nextPath;
        int[][] tabuMatrix = new int[graph.size][graph.size];
        int iterations = 10 * graph.size;
        int nextCost;
        int currentCost;
        int[] savePath;
        bestCost = Integer.MAX_VALUE;
        long startTime = System.currentTimeMillis();

        // Generowanie początkowego rozwiązania za pomocą algorytmu zachłannego
        currentPath = generateGreedyPath();
        currentCost = calculatePathCost(currentPath);
        System.out.println("Tabu search:");
        System.out.println("Koszt ścieżki według algorytmu zachłannego: " + currentCost);

        while (true) {
            nextPath = currentPath.clone();
            for (int a = 0; a < iterations; a++) {
                currentPath = nextPath.clone();
                nextCost = currentCost;
                savePath = currentPath.clone();

                for (int i = 1; i < graph.size; i++) {
                    for (int j = i + 1; j < graph.size; j++) {
                        neighborOperator.apply(i, j, currentPath);
                        currentCost = calculatePathCost(currentPath);

                        if (currentCost < bestCost) {
                            bestPath = currentPath.clone();
                            bestCost = currentCost;
                            bestSolutionTime = System.currentTimeMillis() - startTime;
                            if (tabuMatrix[i][j] == 0) {
                                nextCost = currentCost;
                                nextPath = currentPath.clone();
                            }
                        }

                        if (currentCost < nextCost && tabuMatrix[i][j] < a) {
                            nextCost = currentCost;
                            nextPath = currentPath.clone();
                            tabuMatrix[i][j] += graph.size;
                        }

                        currentPath = savePath.clone();
                    }
                }

                for (int x = 0; x < graph.size; x++) {
                    for (int y = 0; y < graph.size; y++) {
                        if (tabuMatrix[x][y] > 0) {
                            tabuMatrix[x][y] -= 1;
                        }
                    }
                }

                long executionTime = System.currentTimeMillis() - startTime;
                if (executionTime >= timeLimit) {
                    System.out.print("Najlepsza znaleziona ścieżka: ");
                    System.out.print("[");
                    for (int j : bestPath) {
                        System.out.print(j + " ");
                    }
                    System.out.println("0]");
                    System.out.println("Koszt ścieżki: " + bestCost);
                    System.out.println("Najlepsze rozwiązanie znaleziono w: " + bestSolutionTime + " ms");
                    return;
                }
            }
        }
    }

    private int[] generateGreedyPath() {
        int[] greedyPath = new int[graph.size];
        boolean[] visited = new boolean[graph.size];
        greedyPath[0] = 0; // Początkowe miasto
        visited[0] = true;

        for (int i = 1; i < graph.size; i++) {
            int bestNext = -1;
            int minDistance = Integer.MAX_VALUE;

            for (int j = 0; j < graph.size; j++) {
                if (!visited[j] && graph.getDistanceMatrix()[greedyPath[i - 1]][j] < minDistance) {
                    bestNext = j;
                    minDistance = graph.getDistanceMatrix()[greedyPath[i - 1]][j];
                }
            }

            greedyPath[i] = bestNext;
            visited[bestNext] = true;
        }

        return greedyPath;
    }

    private int[] generateRandomPath() { //generowanie losowej ścieżki
        int[] randomPath = new int[graph.size];
        for (int i = 0; i < graph.size; i++) {
            randomPath[i] = i;
        }
        for (int i = 1; i < randomPath.length; i++) {//funkcja losująca kolejność
            int randomIndexToSwap = rand.nextInt(randomPath.length - 1) + 1;
            int temp = randomPath[randomIndexToSwap];
            randomPath[randomIndexToSwap] = randomPath[i];
            randomPath[i] = temp;
        }

        return randomPath;
    }

    private int calculatePathCost(int[] path) {
        int cost = 0;
        for (int i = 0; i < path.length - 1; i++) {
            cost += graph.getDistanceMatrix()[path[i]][path[i + 1]];
        }
        cost += graph.getDistanceMatrix()[(path[(path.length - 1)])][path[0]];
        return cost;
    }
}
