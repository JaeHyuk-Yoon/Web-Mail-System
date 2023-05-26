package deu.cse.spring_webmail.control;

public class Components {
    private final String status;
    private final DiskSpaceDetails diskSpace;

    public Components(String status, DiskSpaceDetails diskSpace) {
        this.status = status;
        this.diskSpace = diskSpace;
    }

    public String getStatus() {
        return status;
    }

    public DiskSpaceDetails getDiskSpace() {
        return diskSpace;
    }
}
