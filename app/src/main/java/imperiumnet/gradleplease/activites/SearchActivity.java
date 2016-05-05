package imperiumnet.gradleplease.activites;

import android.content.Intent;
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
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import imperiumnet.gradleplease.R;
import imperiumnet.gradleplease.adapters.RecyclerAdapter;
import imperiumnet.gradleplease.callbacks.Listeners;
import imperiumnet.gradleplease.fragments.DialogResultInfo;
import imperiumnet.gradleplease.models.MCModel;
import imperiumnet.gradleplease.models.QueryModel;
import imperiumnet.gradleplease.network.NetworkUtilsJson;
import imperiumnet.gradleplease.singleton.Singleton;
import imperiumnet.gradleplease.utils.Constant;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class SearchActivity extends AppCompatActivity implements Listeners.DialogClickListeners, Listeners.OnHistoryClickListener {

    private SearchView mSearchView;
    private RelativeLayout mRelativeLay;

    public static void parseJson(final String query) throws JSONException, ExecutionException, InterruptedException {
        if (Singleton.getRows() == 0)
            new NetworkUtilsJson(new Listeners.TaskFinishedListener() {
                @Override
                public void processFinish(String output) throws JSONException, ParseException {
                    parseMaven(output, query);
                    addHistory(output, query);
                }
            }).execute(Constant.DENY, "https://search.maven.org/solrsearch/select?q=" + query
                    .replace(" ", "")
                    .replace("  ", "")
                    .toLowerCase()
                    .trim() + "&rows=" + Singleton.getPreferences().getString(Constant.COUNT_KEY, "25") + "&wt=json");
        else new NetworkUtilsJson(new Listeners.TaskFinishedListener() {
            @Override
            public void processFinish(String output) throws JSONException, ParseException {
                parseMaven(output, query);
                addHistory(output, query);
            }
        }).execute(Constant.DENY, "https://search.maven.org/solrsearch/select?q=" + query
                .replace(" ", "")
                .replace("  ", "")
                .toLowerCase()
                .trim() + "&rows=" + Singleton.getRows() + "&wt=json");
    }

    private static void parseMaven(final String output, final String query) throws JSONException, ParseException {
        if (output != null) {
            JSONArray mJsonArray;
            JSONObject mJsonObj;
            String mObjData;
            String mTimestamp;
            MCModel mMCModel;
            ArrayList<MCModel> mDataList = new ArrayList<>();
            mJsonObj = new JSONObject(output);
            mJsonArray = mJsonObj.getJSONObject("response").getJSONArray("docs");
            int x = 0;
            while (x != mJsonArray.length()) {
                mMCModel = new MCModel();
                mJsonObj = (JSONObject) mJsonArray.get(x);
                mObjData = mJsonObj.getString("id");
                mMCModel.setLibrary(mObjData);
                mObjData = mJsonObj.getString("latestVersion");
                mMCModel.setLatestVersion(mObjData);
                mTimestamp = String.valueOf(new SimpleDateFormat("MMddyyHHmmss", Locale.US)
                        .parse(String.valueOf(mJsonObj.getLong("timestamp"))));
                mMCModel.setTimestamp(mTimestamp);
                mDataList.add(mMCModel);
                x++;
            }
            Singleton.setQuery(query);
            if (Singleton.getSingle() == null) {
                if (Singleton.getPreferences().getBoolean(Constant.SINGLE_KEY, false)) {
                    Singleton.getRecyclerAdapter().update(null, mDataList.get(0));
                } else {
                    Singleton.getRecyclerAdapter().update(mDataList, null);
                }
            } else {
                if (Singleton.getSingle().equals("true")) {
                    Singleton.getRecyclerAdapter().update(null, mDataList.get(0));
                } else {
                    Singleton.getRecyclerAdapter().update(mDataList, null);
                }
            }
        }
    }

    public static String parseSingleResult(String result) throws JSONException {
        JSONObject mObject;
        JSONArray mArray;
        String mData;
        mObject = new JSONObject(result);
        mObject = mObject.getJSONObject("response");
        mArray = mObject.getJSONArray("docs");
        mObject = (JSONObject) mArray.get(0);
        mData = mObject.getString("id");
        return mData;
    }

    public static void addHistory(String output, String query) throws JSONException {
        if (output != null) {
            QueryModel model = new QueryModel();
            model.setmQuery(query);
            model.setmFirstResult(parseSingleResult(output));
            model.setmCreationTime(Calendar.getInstance().getTime());
            addToRealm(model);
        }
    }

    public static void addToRealm(QueryModel model) {
        Singleton.getRealm().beginTransaction();
        Singleton.getRealm().copyToRealmOrUpdate(model);
        Singleton.getRealm().commitTransaction();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(GradleSearch.swapTheme(null));
        setContentView(R.layout.activity_search);
        if (getSupportActionBar() == null) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mRelativeLay = (RelativeLayout) findViewById(R.id.relative);
        RecyclerView mRecycler = (RecyclerView) findViewById(R.id.recycler);
        RecyclerAdapter mRecyclerAdapter = new RecyclerAdapter(true, this) {
            @Override
            public void handleSwipe(int position) {

            }

            @Override
            public void onClick1(View v, int position) {
                DialogResultInfo info = new DialogResultInfo();
                info.show(getSupportFragmentManager(), Constant.FRAGMENT_RESULT);
            }
        };
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mRecyclerAdapter);
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
        Singleton.setRecyclerAdapter(mRecyclerAdapter);
        Singleton.setListener(this);
        mSearchView.setIconified(false);
        initListeners();
    }

    private void initListeners() {
        mSearchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
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
                    Singleton.getRecyclerAdapter().clearAll();
                }
                return false;
            }
        });

        mSearchView.setOnCloseListener(new android.support.v7.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mSearchView.clearFocus();
                Singleton.getRecyclerAdapter().clearAll();
                return false;
            }
        });

        mRelativeLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.clearFocus();
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

    @Override
    public void hideFrag(boolean isResult) {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag(Constant.FRAGMENT_RESULT))
                .commit();
    }

    @Override
    public void onHistoryClick(String query) {
        try {
            parseJson(query);
        } catch (JSONException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

//TODO 1. if you change theme and press "back" to go to the search, the home as up button disappears, fix it.
//TODO 2. Save valid searches to Realm