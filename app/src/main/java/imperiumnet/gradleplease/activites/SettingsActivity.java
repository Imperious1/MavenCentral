package imperiumnet.gradleplease.activites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

import imperiumnet.gradleplease.R;
import imperiumnet.gradleplease.callbacks.Listeners;
import imperiumnet.gradleplease.fragments.PreferenceFrag;
import imperiumnet.gradleplease.singleton.Singleton;
import imperiumnet.gradleplease.utils.Constant;

public class SettingsActivity extends AppCompatActivity implements Listeners.DataSetChangedListener {

    private PreferenceFrag mPrefFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(GradleSearch.swapTheme(null));
        initialize();
        if (mPrefFragment == null)
            initializePref(false);
        else initializePref(true);
    }

    public void initializePref(boolean isExisting) {
        mPrefFragment = new PreferenceFrag();
        if (!isExisting)
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.coordinator, mPrefFragment, Constant.PREF_TAG)
                    .commit();
        else
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.coordinator, mPrefFragment, Constant.PREF_TAG)
                    .commit();
        if (!isExisting)
            getFragmentManager()
                    .beginTransaction()
                    .show(mPrefFragment)
                    .commit();
    }

    public void initialize() {
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void clearData(String data, boolean value, boolean isSwitch) {
        if (!isSwitch)
            Singleton.setRows(Integer.parseInt(data));
        else Singleton.setSingle(String.valueOf(value));
        if (Singleton.getRecyclerAdapter() != null)
            Singleton.getRecyclerAdapter().clearAll();
        if (Singleton.getQuery() != null) {
            try {
                SearchActivity.parseJson(Singleton.getQuery());
            } catch (JSONException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


//TODO 1. add FAB that does *input here*
