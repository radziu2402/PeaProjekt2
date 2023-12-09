package algorithm;

class MakeReverseOperator implements NeighborOperator {
    @Override
    public void apply(int i, int j, int[] path) {
        int temp;
        while (i < j) {
            temp = path[i];
            path[i] = path[j];
            path[j] = temp;
            i++;
            j--;
        }
    }
}