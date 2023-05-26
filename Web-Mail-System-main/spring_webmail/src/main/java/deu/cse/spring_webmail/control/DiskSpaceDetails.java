package deu.cse.spring_webmail.control;

public class DiskSpaceDetails {
    private final long total;
    private final long free;
    private final long threshold;
    private final boolean exists;

    public DiskSpaceDetails(long total, long free, long threshold, boolean exists) {
        this.total = total;
        this.free = free;
        this.threshold = threshold;
        this.exists = exists;
    }

    public long getTotal() {
        return total;
    }

    public long getFree() {
        return free;
    }

    public long getThreshold() {
        return threshold;
    }

    public boolean isExists() {
        return exists;
    }
}
