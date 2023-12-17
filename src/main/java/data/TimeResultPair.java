package data;

public class TimeResultPair {
    private long time;
    private int result;

    public TimeResultPair(long time, int result) {
        this.time = time;
        this.result = result;
    }

    public long getTime() {
        return time;
    }

    public int getResult() {
        return result;
    }
}