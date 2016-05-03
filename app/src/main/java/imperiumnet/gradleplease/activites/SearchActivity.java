package imperiumnet.gradleplease.activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
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
import imperiumnet.gradleplease.callbacks.Listeners;
import imperiumnet.gradleplease.fragments.DialogResultInfo;
import imperiumnet.gradleplease.models.MCModel;
import imperiumnet.gradleplease.network.NetworkUtilsJson;
import imperiumnet.gradleplease.utils.Constant;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class SearchActivity extends AppCompatActivity implements Listeners.DialogClickListeners, Listeners.ThemeSearchChangeListener {

    private static SearchView mSearchView;
    private static RelativeLayout mRelativeLay;
    private static RecyclerAdapter mRecyclerAdapter;
    private static SharedPreferences mPreferences;
    private static String mQuery;
    private static int mRows;
    private static String mSingle;
    private static Context mContext;

    public static void parseJson(final String query) throws JSONException, ExecutionException, InterruptedException {
        if (mRows == 0)
            new NetworkUtilsJson(new Listeners.TaskFinishedListener() {
                @Override
                public void processFinish(String output) throws JSONException, ParseException {
                    test(output, query);
                }
            }).execute(Constant.DENY, "https://search.maven.org/solrsearch/select?q=" + query
                    .replace(" ", "")
                    .replace("  ", "")
                    .toLowerCase()
                    .trim() + "&rows=" + mPreferences.getString(Constant.COUNT_KEY, "25") + "&wt=json");
        else new NetworkUtilsJson(new Listeners.TaskFinishedListener() {
            @Override
            public void processFinish(String output) throws JSONException, ParseException {
                test(output, query);
            }
        }).execute(Constant.DENY, "https://search.maven.org/solrsearch/select?q=" + query
                .replace(" ", "")
                .replace("  ", "")
                .toLowerCase()
                .trim() + "&rows=" + mRows + "&wt=json");
    }

    private static void test(final String output, final String query) throws JSONException, ParseException {
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
            mQuery = query;
            if (mSingle == null) {
                if (mPreferences.getBoolean(Constant.SINGLE_KEY, false)) {
                    mRecyclerAdapter.update(null, mDataList.get(0));
                } else {
                    mRecyclerAdapter.update(mDataList, null);
                }
            } else {
                if (mSingle.equals("true")) {
                    mRecyclerAdapter.update(null, mDataList.get(0));
                } else {
                    mRecyclerAdapter.update(mDataList, null);
                }
            }
        }
    }

    public static void setmRows(int mRows) {
        SearchActivity.mRows = mRows;
    }

    public static RecyclerAdapter getRecyclerAdapter() {
        return mRecyclerAdapter;
    }

    public static String getQuery() {
        return mQuery;
    }

    public static void setSingle(String mSingle) {
        SearchActivity.mSingle = mSingle;
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(GradleSearch.swapTheme());
        initialize();
        mContext = this;
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
                    mRecyclerAdapter.clearAll();
                }
                return false;
            }
        });

        mSearchView.setOnCloseListener(new android.support.v7.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mSearchView.clearFocus();
                mRecyclerAdapter.clearAll();
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
                .remove(getSupportFragmentManager().findFragmentByTag("tagfrag"))
                .commit();
    }

    public void initialize() {
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
        mRecyclerAdapter = new RecyclerAdapter() {
            @Override
            public void onClick1(View v, int position) {
                DialogResultInfo info = new DialogResultInfo();
                info.show(getSupportFragmentManager(), "tagfrag");
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
        mSearchView.setIconified(false);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        initListeners();
    }

    @Override
    public void changeTheme(String data) {
        super.setTheme(GradleSearch.swapTheme(data));
        initialize();
    }
}

//TODO 1. if you change theme and press "back" to go to the search, the home as up button disappears, fix it.