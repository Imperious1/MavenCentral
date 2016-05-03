package imperiumnet.gradleplease.activites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

import imperiumnet.gradleplease.R;
import imperiumnet.gradleplease.callbacks.Listeners;
import imperiumnet.gradleplease.fragments.PreferenceFrag;
import imperiumnet.gradleplease.utils.Constant;

public class SettingsActivity extends AppCompatActivity implements Listeners.ThemeSettingsChangeListener, Listeners.DataSetChangedListener {

    private PreferenceFrag mPrefFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(GradleSearch.swapTheme());
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

    public void resetActivity(int style) {
        super.setTheme(style);
        initialize();
    }

    @Override
    public void changeTheme(String data) {
        resetActivity(GradleSearch.swapTheme(data));
        initializePref(true);
    }

    @Override
    public void clearData(String data, boolean value, boolean isSwitch) {
        if (!isSwitch)
            SearchActivity.setmRows(Integer.parseInt(data));
        else SearchActivity.setSingle(String.valueOf(value));
        if (SearchActivity.getRecyclerAdapter() != null)
            SearchActivity.getRecyclerAdapter().clearAll();
        if (SearchActivity.getQuery() != null) {
            try {
                SearchActivity.parseJson(SearchActivity.getQuery());
            } catch (JSONException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

