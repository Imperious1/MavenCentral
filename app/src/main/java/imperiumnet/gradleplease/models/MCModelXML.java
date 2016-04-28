package imperiumnet.gradleplease.models;

public class MCModelXML {
    private String mFolder;
    private String mCompanyName;
    private String mPackageName;
    private String mVersion;

    public String getmFolder() {
        return mFolder;
    }

    public MCModelXML setmFolder(String mFolder) {
        this.mFolder = mFolder;
        return this;
    }

    public String getmCompanyName() {
        return mCompanyName;
    }

    public void setmCompanyName(String mCompanyName) {
        this.mCompanyName = mCompanyName;
    }

    public String getmPackageName() {
        return mPackageName;
    }

    public void setmPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    public String getmVersion() {
        return mVersion;
    }

    public void setmVersion(String mVersion) {
        this.mVersion = mVersion;
    }
}
