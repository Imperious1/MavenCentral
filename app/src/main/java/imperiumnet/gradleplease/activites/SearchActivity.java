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
import imperiumnet.gradleplease.callbacks.listeners;
import imperiumnet.gradleplease.fragments.DialogCount;
import imperiumnet.gradleplease.fragments.DialogResultInfo;
import imperiumnet.gradleplease.models.MCModel;
import imperiumnet.gradleplease.network.NetworkUtilsJson;
import imperiumnet.gradleplease.utils.Constant;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class SearchActivity extends AppCompatActivity implements listeners.DialogClickListeners {

    private SearchView mSearchView;
    private RelativeLayout mRelativeLay;
    private RecyclerAdapter mRecyclerAdapter;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mPEditor;
    private ArrayList<MCModel> mDataList;
    private String mQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = getSharedPreferences("Theme Settings", Context.MODE_PRIVATE);
        if (mPreferences.getString(Constant.THEME_KEY, Constant.DEFAULT).equals(Constant.THEME_ONE))
            super.setTheme(R.style.shadow);
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
        initListeners();
    }

    public void parseJson(final String query) throws JSONException, ExecutionException, InterruptedException {
        new NetworkUtilsJson(new listeners.TaskFinishedListener() {
            @Override
            public void processFinish(String output) throws JSONException, ParseException {
                if (output != null) {
                    JSONArray mJsonArray;
                    JSONObject mJsonObj;
                    String mObjData;
                    String mTimestamp;
                    String[] attempt2;
                    MCModel mMCModel;
                    mDataList = new ArrayList<>();
                    mJsonObj = new JSONObject(output);
                    mJsonArray = mJsonObj.getJSONObject("response").getJSONArray("docs");
                    int x = 0;
                    while (x != mJsonArray.length()) {
                        mMCModel = new MCModel();
                        mJsonObj = (JSONObject) mJsonArray.get(x);
                        mObjData = mJsonObj.getString("id");
                        mMCModel.setLibrary(mObjData);
                        attempt2 = mObjData.split(":");
                        mMCModel.setPackageName(attempt2[1]);
                        mObjData = mJsonObj.getString("latestVersion");
                        mMCModel.setLatestVersion(mObjData);
                        mTimestamp = String.valueOf(new SimpleDateFormat("MMddyyHHmmss", Locale.US)
                                .parse(String.valueOf(mJsonObj.getLong("timestamp"))));
                        mMCModel.setTimestamp(mTimestamp);
                        mDataList.add(mMCModel);
                        x++;
                    }
                    if (mPreferences.getBoolean(Constant.SINGLE_KEY, false)) {
                        mRecyclerAdapter.update(null, mDataList.get(0));
                    } else {
                        mRecyclerAdapter.update(mDataList, null);
                    }
                    mQuery = query;
                }
            }
        }).execute(Constant.DENY, "https://search.maven.org/solrsearch/select?q=" + query
                .replace(" ", "")
                .replace("  ", "")
                .toLowerCase()
                .trim() + "&rows=" + mPreferences.getString(Constant.COUNT_KEY, "40") + "&wt=json");
    }

    public void initListeners() {
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
                    mQuery = null;
                    mRecyclerAdapter.clearAll();
                }
                return false;
            }
        });

        mSearchView.setOnCloseListener(new android.support.v7.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mQuery = null;
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
            if (!mPreferences.getBoolean(Constant.SINGLE_KEY, false)) {
                filterSingular(true);
            }
            return true;
        } else if (id == R.id.disable_single) {
            if (mPreferences.getBoolean(Constant.SINGLE_KEY, false)) {
                filterSingular(false);
            }
            return true;
        } else if (id == R.id.choose_results) {
            mSearchView.clearFocus();
            new DialogCount().show(getSupportFragmentManager(), "dialogCount");
        } else if (id == R.id.remove_results) {
            removeResult();
        }
        return super.onOptionsItemSelected(item);
    }

    public void filterSingular(boolean isEnabled) {
        if (mPEditor == null)
            mPEditor = mPreferences.edit();
        mPEditor.remove(Constant.SINGLE_KEY);
        if (isEnabled) {
            mPEditor.remove(Constant.COUNT_KEY);
            mPEditor.putString(Constant.SINGLE_KEY, "1");
        }
        mPEditor.putBoolean(Constant.SINGLE_KEY, isEnabled);
        mPEditor.apply();
        if (mDataList != null && !mSearchView.getQuery().toString().isEmpty() && !isEnabled)
            mRecyclerAdapter.update(mDataList, null);
        else if (mDataList != null && !mSearchView.getQuery().toString().isEmpty() && isEnabled)
            mRecyclerAdapter.update(null, mDataList.get(0));
    }

    @Override
    public void hideFrag(boolean isResult) {
        if (!isResult)
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(getSupportFragmentManager().findFragmentByTag("dialogCount"))
                    .commit();
        else
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(getSupportFragmentManager().findFragmentByTag("tagfrag"))
                    .commit();
    }

    @Override
    public void setResult(String number) {
        if (mPEditor == null)
            mPEditor = mPreferences.edit();
        if (number == null) {
            mPEditor.remove(Constant.COUNT_KEY);
            mPEditor.apply();
        } else {
            if (!mPreferences.getString(Constant.COUNT_KEY, "40").equals(number)) {
                if (mPEditor == null)
                    mPEditor = mPreferences.edit();
                mPEditor.remove(Constant.COUNT_KEY);
                mPEditor.putString(Constant.COUNT_KEY, number);
                if (number.equals("1") && mPreferences.getBoolean(Constant.SINGLE_KEY, true)) {
                    mPEditor.remove(Constant.SINGLE_KEY);
                    mPEditor.putBoolean(Constant.SINGLE_KEY, true);
                }
                mPEditor.apply();
                mRecyclerAdapter.clearAll();
                if (mDataList != null && !mSearchView.getQuery().toString().isEmpty()) {
                    for (int i = 0; i <= Integer.parseInt(number) - 1; i++) {
                        if (i <= mDataList.size() - 1)
                            mRecyclerAdapter.updateCount(mDataList.get(i));
                    }
                }
            }
            hideFrag(false);
        }
    }

    public void removeResult() {
        if (mPEditor == null)
            mPEditor = mPreferences.edit();
        mPEditor.remove(Constant.COUNT_KEY);
        mPEditor.apply();
        if (mQuery != null) {
            try {
                parseJson(mQuery);
            } catch (JSONException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            mRecyclerAdapter.update(mDataList, null);
        }
    }
}
