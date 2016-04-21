package imperiumnet.gradleplease.activites;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import imperiumnet.gradleplease.R;
import imperiumnet.gradleplease.adapters.RecyclerAdapter;
import imperiumnet.gradleplease.fragments.DialogCount;
import imperiumnet.gradleplease.models.MCModel;
import imperiumnet.gradleplease.network.NetworkUtilz;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class SearchActivity extends AppCompatActivity implements DialogCount.Communicator {

    private SearchView searchView;
    private RelativeLayout relativeLayout;
    private RecyclerAdapter adapter;
    private static final String themeKey = "theme_one_0101011";
    private static final String single = "single_key_14890672";
    private static final String count = "count_192391027";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ArrayList<MCModel> list;
    private String queryy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("Theme Settings", Context.MODE_PRIVATE);
        if (preferences.getString(themeKey, "default").equals("theme1"))
            super.setTheme(R.style.shadow);
        setContentView(R.layout.activity_search);
        if (getSupportActionBar() == null) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        searchView = (SearchView) findViewById(R.id.search_view);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative);
        RecyclerView mRecycler = (RecyclerView) findViewById(R.id.recycler);
        adapter = new RecyclerAdapter();
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        alphaAdapter.setDuration(450);
        alphaAdapter.setFirstOnly(false);
        ScaleInAnimationAdapter ScaleInAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        ScaleInAdapter.setFirstOnly(false);
        ScaleInAdapter.setDuration(150);
        if (mRecycler != null) {
            mRecycler.setLayoutManager(new LinearLayoutManager(this));
            mRecycler.setAdapter(ScaleInAdapter);
            mRecycler.setItemAnimator(new SlideInUpAnimator());
            mRecycler.getItemAnimator().setAddDuration(400);
            mRecycler.getItemAnimator().setRemoveDuration(700);
        }
        searchView.setIconified(false);
        initListeners();
    }

    public void parseJson(final String query) throws JSONException, ExecutionException, InterruptedException {
        new NetworkUtilz(new NetworkUtilz.AsyncResponse() {
            @Override
            public void processFinish(String output) throws JSONException, ParseException {
                if (output != null) {
                    JSONArray array;
                    JSONObject obj;
                    String dataNeeded;
                    String timestamp;
                    MCModel model;
                    list = new ArrayList<>();
                    obj = new JSONObject(output);
                    array = obj.getJSONObject("response").getJSONArray("docs");
                    int x = 0;
                    while (x != array.length()) {
                        model = new MCModel();
                        obj = (JSONObject) array.get(x);
                        dataNeeded = obj.getString("id");
                        model.setLibrary(dataNeeded);
                        dataNeeded = obj.getString("latestVersion");
                        model.setLatestVersion(dataNeeded);
                        timestamp = String.valueOf(new SimpleDateFormat("MMddyyHHmmss", Locale.US)
                                .parse(String.valueOf(obj.getLong("timestamp"))));
                        model.setTimestamp(timestamp);
                        list.add(model);
                        x++;
                    }
                    if (preferences.getBoolean(single, false)) {
                        adapter.update(null, list.get(0));
                    } else {
                        adapter.update(list, null);
                    }
                    queryy = query;
                }
            }
        }).execute("https://search.maven.org/solrsearch/select?q=" + query
                .replace(" ", "")
                .replace("  ", "")
                .toLowerCase()
                .trim() + "&rows=" + preferences.getString(count, "40") + "&wt=json");
    }

    public void initListeners() {
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                try {
                    parseJson(query);
                } catch (NullPointerException | InterruptedException | ExecutionException | JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    queryy = null;
                    adapter.clearAll();
                }
                return false;
            }
        });

        searchView.setOnCloseListener(new android.support.v7.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                queryy = null;
                searchView.clearFocus();
                adapter.clearAll();
                return false;
            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.clearFocus();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.enable_single) {
            if (!preferences.getBoolean(single, false)) {
                filterSingular(true);
            }
            return true;
        } else if (id == R.id.disable_single) {
            if (preferences.getBoolean(single, false)) {
                filterSingular(false);
            }
            return true;
        } else if (id == R.id.choose_results) {
            new DialogCount().show(getSupportFragmentManager(), "dialogCount");
        } else if (id == R.id.remove_results) {
            removeResult();
        }
        return super.onOptionsItemSelected(item);
    }

    public void filterSingular(boolean isEnabled) {
        if (editor == null)
            editor = preferences.edit();
        editor.remove(single);
        if (isEnabled) {
            editor.remove(count);
            editor.putString(count, "1");
        }
        editor.putBoolean(single, isEnabled);
        editor.apply();
        if (list != null && !searchView.getQuery().toString().isEmpty() && !isEnabled)
            adapter.update(list, null);
        else if (list != null && !searchView.getQuery().toString().isEmpty() && isEnabled)
            adapter.update(null, list.get(0));
    }

    @Override
    public void hideFrag() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag("dialogCount"))
                .commit();
    }

    @Override
    public void setResult(String number) {
        if (editor == null)
            editor = preferences.edit();
        if (number == null) {
            editor.remove(count);
            editor.apply();
        } else {
            if (!preferences.getString(count, "40").equals(number)) {
                if (editor == null)
                    editor = preferences.edit();
                editor.remove(count);
                editor.putString(count, number);
                if (number.equals("1") && preferences.getBoolean(single, true)) {
                    editor.remove(single);
                    editor.putBoolean(single, true);
                }
                editor.apply();
                adapter.clearAll();
                if (list != null && !searchView.getQuery().toString().isEmpty()) {
                    for (int i = 0; i <= Integer.parseInt(number) - 1; i++) {
                        if (i <= list.size() - 1)
                            adapter.updateCount(list.get(i));
                    }
                }
            }
            hideFrag();
        }
    }

    public void removeResult() {
        if (editor == null)
            editor = preferences.edit();
        editor.remove(count);
        editor.apply();
        if(queryy != null) {
            try {
                parseJson(queryy);
            } catch (JSONException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            adapter.update(list, null);
        }
    }
}

    /*public File getTempFile(Context context, String fName) {
        File file;
        try {
            file = File.createTempFile(fName, null, context.getCacheDir());
            return file;
        } catch (IOException e) {
                // Error while creating file
            }
            return null;

    } */
