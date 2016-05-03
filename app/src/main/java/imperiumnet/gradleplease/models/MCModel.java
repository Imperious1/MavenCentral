package imperiumnet.gradleplease.models;

//MCModel = MavenCentralModel
public class MCModel {
    private String mLatestVersion;
    private String mLibrary;
    private String mTimestamp;

    public String getLatestVersion() {
        return mLatestVersion;
    }

    public void setLatestVersion(String mLatestVersion) {
        this.mLatestVersion = mLatestVersion;
    }

    public String getLibrary() {
        return mLibrary;
    }

    public void setLibrary(String mLibrary) {
        this.mLibrary = mLibrary;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(String timestamp) {
        this.mTimestamp = timestamp;
    }
}
