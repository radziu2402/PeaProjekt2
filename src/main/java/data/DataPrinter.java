package data;

public class DataPrinter {
    public void displayData(Matrix data) {
        if (data == null) {
            System.out.println("Brak danych do wyświetlenia.");
        } else {
            int[][] distanceMatrix = data.getDistanceMatrix();
            for (int[] matrix : distanceMatrix) {
                for (int i : matrix) {
                    System.out.print(i + "\t");
                }
                System.out.println();
            }
        }
    }
}
