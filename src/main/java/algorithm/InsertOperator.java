package algorithm;

class InsertOperator implements NeighborOperator {
    @Override
    public void apply(int i, int j, int[] path) {
        int[] tempTab = new int[path.length];
        int x;
        for (x = 0; x < i; x++) {
            tempTab[x] = path[x];
        }
        tempTab[i] = path[j];
        for (x = x + 1; x < j + 1; x++) {
            tempTab[x] = path[x - 1];
        }

        for (; x < path.length; x++) {
            tempTab[x] = path[x];
        }

        System.arraycopy(tempTab, 0, path, 0, path.length);
    }
}