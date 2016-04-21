package imperiumnet.gradleplease.models;

/**
 * Created by overlord on 4/18/16.
 */
//MCModel = MavenCentralModel
public class MCModel {
    private String latestVersion;
    private String library;
    private String timestamp;

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public String getLibrary() {
        return library;
    }

    public void setLibrary(String library) {
        this.library = library;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
