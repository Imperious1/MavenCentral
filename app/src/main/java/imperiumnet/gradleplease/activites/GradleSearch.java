package imperiumnet.gradleplease.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import imperiumnet.gradleplease.R;
import imperiumnet.gradleplease.adapters.ItemTouchSwipeHelper;
import imperiumnet.gradleplease.adapters.RecyclerAdapter;
import imperiumnet.gradleplease.callbacks.Listeners;
import imperiumnet.gradleplease.models.QueryModel;
import imperiumnet.gradleplease.singleton.Singleton;
import imperiumnet.gradleplease.utils.Constant;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.Sort;

public class GradleSearch extends AppCompatActivity {

    Listeners.OnHistoryClickListener listener;

    public static int swapTheme(@Nullable String value) {
        switch ((value != null) ? value : Singleton.getPreferences().getString(Constant.COLOR_KEY, Constant.DEFAULT_THEME)) {
            case "color_deep_orange":
                return R.style.DeepOrangeTheme;
            case "color_teal":
                return R.style.TealTheme;
            case "color_red":
                return R.style.RedTheme;
            case "color_pink":
                return R.style.PinkTheme;
            case "color_purple":
                return R.style.PurpleTheme;
            case "color_deep_purple":
                return R.style.IndigoTheme;
            case "color_blue":
                return R.style.BlueTheme;
            case "color_light_blue":
                return R.style.LightBlueTheme;
            case "color_cyan":
                return R.style.CyanTheme;
            case "color_green":
                return R.style.GreenTheme;
            case "color_light_green":
                return R.style.LightGreenTheme;
            case "color_lime":
                return R.style.LimeTheme;
            case "color_yellow":
                return R.style.YellowTheme;
            case "color_amber":
                return R.style.AmberTheme;
            case "color_orange":
                return R.style.OrangeTheme;
            default:
                return R.style.TealTheme;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Singleton.getInstance();
        Singleton.setConfig(new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().build());
        Singleton.setRealm(Realm.getInstance(Singleton.getConfig()));
        Singleton.setPreferences(PreferenceManager.getDefaultSharedPreferences(this));
        super.setTheme(swapTheme(null));
        setContentView(R.layout.activity_gradle_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView mRecycler = (RecyclerView) findViewById(R.id.recycler_history);
        assert mRecycler != null;
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        this.listener = Singleton.getListener();
        final RecyclerAdapter mAdapter = new RecyclerAdapter(false, this) {
            @Override
            public void onClick1(View view, int position) {
                startActivity(new Intent(GradleSearch.this, SearchActivity.class));
                if (Singleton.getListener() != null)
                    listener.onHistoryClick(Singleton.getRealm().allObjectsSorted(QueryModel.class, "mCreationTime", Sort.DESCENDING).get(position).getmQuery());
            }

            @Override
            public void handleSwipe(int position) {
                Singleton.getRealm().beginTransaction();
                Singleton.getRealm().allObjects(QueryModel.class).get(position).removeFromRealm();
                Singleton.getRealm().commitTransaction();
                notifyDataSetChanged();
            }
        };
        ItemTouchSwipeHelper callback = new ItemTouchSwipeHelper(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        mRecycler.setAdapter(mAdapter);
        helper.attachToRecyclerView(mRecycler);
        FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.fab);
        assert mFab != null;
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GradleSearch.this, SearchActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gradle_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

//TODO ADD SEARCH HISTORY IN RECYCLER VIEW + ARTIFACT CARDS FOR QUICK SEARCH