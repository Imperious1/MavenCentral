package imperiumnet.gradleplease.activites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

import imperiumnet.gradleplease.R;
import imperiumnet.gradleplease.callbacks.Listeners;
import imperiumnet.gradleplease.fragments.PreferenceFrag;

public class SettingsActivity extends AppCompatActivity implements Listeners.ThemeChangeListener, Listeners.DataSetChangedListener {

    private PreferenceFrag frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(GradleSearch.swapTheme());
        initialize();
        if (frag == null)
            initializePref(false);
        else initializePref(true);
    }

    public void initializePref(boolean isExisting) {
        frag = new PreferenceFrag();
        if (!isExisting)
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.coordinator, frag, "fragPref")
                    .commit();
        else
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.coordinator, frag, "fragPref")
                    .commit();
        if (!isExisting)
            getFragmentManager()
                    .beginTransaction()
                    .show(frag)
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
            SearchActivity.setNumber(Integer.parseInt(data));
        else SearchActivity.setData(String.valueOf(value));
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

