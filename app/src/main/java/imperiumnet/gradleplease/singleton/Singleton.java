package imperiumnet.gradleplease.singleton;

import android.app.Application;
import android.content.SharedPreferences;

import imperiumnet.gradleplease.adapters.RecyclerAdapter;
import imperiumnet.gradleplease.callbacks.Listeners;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Singleton extends Application {

    private static Realm realm;
    private static RealmConfiguration mConfig;
    private static SharedPreferences mPreferences;
    private static String mSingle;
    private static int mRows;
    private static String mQuery;
    private static RecyclerAdapter mRecyclerAdapter;
    private static Listeners.OnHistoryClickListener listener;
    private static Singleton instance;

    public Singleton() {
        instance = this;
    }

    public static Listeners.OnHistoryClickListener getListener() {
        return listener;
    }

    public static void setListener(Listeners.OnHistoryClickListener listener) {
        Singleton.listener = listener;
    }

    public static RecyclerAdapter getRecyclerAdapter() {
        return mRecyclerAdapter;
    }

    public static void setRecyclerAdapter(RecyclerAdapter mRecyclerAdapter) {
        Singleton.mRecyclerAdapter = mRecyclerAdapter;
    }

    public static String getQuery() {
        return mQuery;
    }

    public static void setQuery(String mQuery) {
        Singleton.mQuery = mQuery;
    }

    public static int getRows() {
        return mRows;
    }

    public static void setRows(int mRows) {
        Singleton.mRows = mRows;
    }

    public static SharedPreferences getPreferences() {
        return mPreferences;
    }

    public static void setPreferences(SharedPreferences mPreferences) {
        Singleton.mPreferences = mPreferences;
    }

    public static String getSingle() {
        return mSingle;
    }

    public static void setSingle(String mSingle) {
        Singleton.mSingle = mSingle;
    }

    public static Realm getRealm() {
        return realm;
    }

    public static void setRealm(Realm realm) {
        Singleton.realm = realm;
    }

    public static RealmConfiguration getConfig() {
        return mConfig;
    }

    public static void setConfig(RealmConfiguration mConfig) {
        Singleton.mConfig = mConfig;
    }

    public static Singleton getInstance() {
        return instance;
    }
}
