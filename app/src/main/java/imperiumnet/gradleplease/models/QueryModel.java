package imperiumnet.gradleplease.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class QueryModel extends RealmObject {
    private String mQuery;
    @PrimaryKey
    private String mFirstResult;
    private Date mCreationTime;

    public Date getmCreationTime() {
        return mCreationTime;
    }

    public void setmCreationTime(Date mCreationTime) {
        this.mCreationTime = mCreationTime;
    }

    public String getmQuery() {
        return mQuery;
    }

    public void setmQuery(String mQuery) {
        this.mQuery = mQuery;
    }

    public String getmFirstResult() {
        return mFirstResult;
    }

    public void setmFirstResult(String mFirstResult) {
        this.mFirstResult = mFirstResult;
    }
}
