package data;

public class Matrix {
    private final int[][] distanceMatrix;
    public final int size;

    public Matrix(int[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
        this.size = distanceMatrix.length;
    }

    public int[][] getDistanceMatrix() {
        return distanceMatrix;
    }

}