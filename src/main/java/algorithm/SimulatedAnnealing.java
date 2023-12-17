package algorithm;

import data.Matrix;
import data.TimeResultPair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SimulatedAnnealing {

    private final Matrix matrix;
    private final int timeLimit;
    private final int problemSize;
    private final double coolingRate;
    private long bestSolutionTime;

    public SimulatedAnnealing(Matrix matrix, int timeLimit, double temperatureCoefficient) {
        this.matrix = matrix;
        this.timeLimit = timeLimit;
        this.problemSize = matrix.getDistanceMatrix().length;
        this.coolingRate = temperatureCoefficient;
    }


    public void solve() {
        double temperature = calculateInitialTemperature();
        List<TimeResultPair> solutions = new ArrayList<>();
        long startTime = System.currentTimeMillis();

        // Generowanie początkowego rozwiązania za pomocą algorytmu zachłannego
        List<Integer> greedySolution = generateGreedyPath();
        int bestCost = calculatePathCost(greedySolution);
        List<Integer> bestPath = new ArrayList<>(greedySolution);
        List<Integer> currentPath = new ArrayList<>(greedySolution);
        solutions.add(new TimeResultPair(System.currentTimeMillis() - startTime, bestCost));
        System.out.println("Symulowane wyżarzanie:");
        System.out.println("Koszt ścieżki według algorytmu zachłannego: " + bestCost);

        while (System.currentTimeMillis() - startTime < timeLimit) {
            for (int i = 0; i < 3; ++i) {
                List<Integer> neighbour = getNeighbours(currentPath);
                int neighbourCost = calculatePathCost(neighbour);

                if (acceptMove(bestCost, neighbourCost, temperature)) {
                    currentPath = new ArrayList<>(neighbour);

                    if (neighbourCost < bestCost) {
                        bestPath = new ArrayList<>(neighbour);
                        bestCost = neighbourCost;
                        bestSolutionTime = System.currentTimeMillis() - startTime;
                        solutions.add(new TimeResultPair(bestSolutionTime, bestCost));
                    }
                }
            }
            temperature *= coolingRate;
        }

        System.out.println("Najlepsza znaleziona ścieżka: " + bestPath);
        bestPath.add(0);
        System.out.println("Koszt ścieżki: " + bestCost);
        System.out.println("Najlepsze rozwiązanie znaleziono w: " + bestSolutionTime + " ms");
        for (TimeResultPair solution : solutions) {
            System.out.println("Czas: " + solution.getTime() + " ms, Wynik: " + solution.getResult());
        }
    }

    private List<Integer> generateGreedyPath() {
        List<Integer> greedyPath = new ArrayList<>();
        boolean[] visited = new boolean[matrix.size];
        greedyPath.add(0); // Początkowe miasto
        visited[0] = true;

        for (int i = 1; i < matrix.size; i++) {
            int bestNext = -1;
            int minDistance = Integer.MAX_VALUE;

            for (int j = 0; j < matrix.size; j++) {
                if (!visited[j] && matrix.getDistanceMatrix()[greedyPath.get(i - 1)][j] < minDistance) {
                    bestNext = j;
                    minDistance = matrix.getDistanceMatrix()[greedyPath.get(i - 1)][j];
                }
            }

            greedyPath.add(bestNext);
            visited[bestNext] = true;
        }

        return greedyPath;
    }

    private int calculatePathCost(List<Integer> path) {
        int cost = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            cost += matrix.getDistanceMatrix()[path.get(i)][path.get(i + 1)];
        }
        cost += matrix.getDistanceMatrix()[path.get(path.size() - 1)][path.get(0)];
        return cost;
    }

    private List<Integer> getNeighbours(List<Integer> solution) {
        List<Integer> neighbour = new ArrayList<>(solution);
        int moveIndex = randomInt(0, 2);

        switch (moveIndex) {
            case 0:
                makeSwapCitiesMove(neighbour);
                break;

            case 1:
                makeInverseMove(neighbour);
                break;

            case 2:
                makeInsertMove(neighbour);
                break;
        }
        return neighbour;
    }

    private int randomInt(int minimum, int maximum) {
        Random random = new Random();
        return random.nextInt(maximum - minimum + 1) + minimum;
    }

    private void makeSwapCitiesMove(List<Integer> neighbour) {
        int firstPosition = randomInt(1, problemSize - 2);
        int secondPosition = randomInt(1, problemSize - 2);

        while (firstPosition == secondPosition) {
            secondPosition = randomInt(1, problemSize - 2);
        }

        Collections.swap(neighbour, firstPosition, secondPosition);
    }

    private void makeInverseMove(List<Integer> neighbour) {
        int firstIndex = randomInt(1, problemSize - 2);
        int secondIndex = randomInt(1, problemSize - 2);

        while (firstIndex == secondIndex) {
            secondIndex = randomInt(1, problemSize - 2);
        }

        int indexFromBegin = Math.min(firstIndex, secondIndex);
        int indexFromEnd = Math.max(firstIndex, secondIndex);

        Collections.reverse(neighbour.subList(indexFromBegin, indexFromEnd));
    }

    private void makeInsertMove(List<Integer> neighbour) {
        int positionToRemove = randomInt(1, problemSize - 2);
        int selectedCity = neighbour.remove(positionToRemove);
        int positionToInsert = randomInt(1, problemSize - 2);

        while (positionToRemove == positionToInsert) {
            positionToInsert = randomInt(1, problemSize - 2);
        }

        neighbour.add(positionToInsert, selectedCity);
    }

    private boolean acceptMove(int currentBestCost, int neighbourCost, double temperature) {
        if (currentBestCost > neighbourCost) {
            return true;
        }

        double probability = Math.exp((currentBestCost - neighbourCost) / temperature);
        return Math.random() < probability;
    }

    private double calculateInitialTemperature() {
        final int SAMPLE_NUMBER = 10000;
        List<Double> deltas = new ArrayList<>(SAMPLE_NUMBER);

        for (int i = 0; i < SAMPLE_NUMBER; ++i) {
            List<Integer> solution = generateRandomSolution();
            List<Integer> neighbour = getNeighbours(solution);
            int currentCost = calculatePathCost(solution);
            int neighbourCost = calculatePathCost(neighbour);
            deltas.add((double) Math.abs(currentCost - neighbourCost));
        }

        Collections.sort(deltas);
        double medianDelta = deltas.get(SAMPLE_NUMBER / 2);

        final double P = 0.55;
        return -medianDelta / Math.log(P);
    }

    private List<Integer> generateRandomSolution() {
        List<Integer> solution = new ArrayList<>();
        for (int i = 0; i < problemSize; ++i) {
            solution.add(i);
        }

        Collections.shuffle(solution.subList(1, problemSize - 1));
        solution.add(0);
        return solution;
    }
}
