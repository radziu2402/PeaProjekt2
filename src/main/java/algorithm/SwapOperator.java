package algorithm;

class SwapOperator implements NeighborOperator {
    @Override
    public void apply(int i, int j, int[] path) {
        int temp = path[i];
        path[i] = path[j];
        path[j] = temp;
    }
}