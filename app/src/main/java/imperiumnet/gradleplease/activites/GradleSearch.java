package imperiumnet.gradleplease.activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import imperiumnet.gradleplease.R;
import imperiumnet.gradleplease.constants.Constant;

public class GradleSearch extends AppCompatActivity {


    FloatingActionButton mFab;
    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gradle_search, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.color_one) {
            if (!mPreferences.getString(Constant.THEME_KEY, Constant.DEFAULT).equals(Constant.THEME_ONE)) {
                swapTheme("theme1");
            }
            return true;
        } else if (id == R.id.color_two) {
            if (!mPreferences.getString(Constant.THEME_KEY, Constant.DEFAULT).equals(Constant.THEME_TWO)) {
                swapTheme("themeDefault");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void swapTheme(String theme) {
        if (mEditor == null)
            mEditor = mPreferences.edit();
        mEditor.remove(Constant.THEME_KEY);
        mEditor.putString(Constant.THEME_KEY, theme);
        mEditor.apply();
        initialize();
    }

    public void initialize() {
        mPreferences = getSharedPreferences("Theme Settings", Context.MODE_PRIVATE);
        if (mPreferences.getString(Constant.THEME_KEY, "mDefault").equals(Constant.THEME_ONE))
            super.setTheme(R.style.shadow);
        else if (mPreferences.getString(Constant.THEME_KEY, "mDefault").equals(Constant.THEME_TWO))
            super.setTheme(R.style.shadowDefault);
        setContentView(R.layout.activity_gradle_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GradleSearch.this, SearchActivity.class);
                startActivity(i);
            }
        });
    }

}
