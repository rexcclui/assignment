package service;

public class AvgPxService {

    
    private final int x;
    private long duration;
    private double px;

    
    public AvgPxService(int x) {
        this.x = x;
        long start = System.currentTimeMillis();
        px = PxSvr.retrieveAvgPx(x);
        setDuration(System.currentTimeMillis() -start);
    }

    public int getX() {
        return x;
    }

    public double getPx() {
        return px;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
