package data;

import java.util.Random;

public class RandomDataGenerator {
    public Matrix generateData(int numCities) {
        int[][] distanceMatrix = new int[numCities][numCities];
        Random random = new Random();

        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                if (i == j) {
                    distanceMatrix[i][j] = -1;
                } else {
                    distanceMatrix[i][j] = random.nextInt(100) + 1; // Zakres od 1 do 100 dla przykładu
                }
            }
        }

        return new Matrix(distanceMatrix);
    }
}
